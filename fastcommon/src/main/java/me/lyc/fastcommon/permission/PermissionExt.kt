package me.lyc.fastcommon.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Created by Liu Yuchuan on 2020/8/2.
 */
fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.isPermissionsGranted(permissions: Array<String>): Boolean {
    return permissions.all { isPermissionGranted(it) }
}

fun FragmentActivity.checkAndRequestPermissions(
    permissions: Array<String>,
    callback: IPermissionCallback
) {
    if (isPermissionsGranted(permissions)) {
        callback.onPermissionsGranted(permissions)
        return
    }

    val fm = supportFragmentManager
    val fragment = fm.findFragmentByTag(PermissionFragment.TAG) as? PermissionFragment
        ?: PermissionFragment().apply {
            fm.beginTransaction()
                .add(this, PermissionFragment.TAG)
                .commitNow()
        }
    fragment.requestPermissions(PermissionRequest(permissions, callback))
}

fun FragmentActivity.checkAndRequestPermissions(
    permissions: Array<String>,
    onGranted: (permissions: Array<out String>) -> Unit,
    onRejected: (permissions: Array<out String>, rejectedPermissions: Array<out String>) -> Unit
) {
    checkAndRequestPermissions(permissions, object : IPermissionCallback {
        override fun onPermissionsGranted(permissions: Array<out String>) {
            onGranted(permissions)
        }

        override fun onPermissionRejected(
            permissions: Array<out String>,
            rejectedPermissions: Array<out String>
        ) {
            onRejected(permissions, rejectedPermissions)
        }
    })
}

private var currentRequestCode = 0

@MainThread
internal fun generateNewPermissionRequestCode(): Int {
    // Fragment和FragmentActivity支持RequestCode最大为0xffff
    if (currentRequestCode == 0xffff) {
        currentRequestCode = 1
        return 1
    }

    return ++currentRequestCode
}
