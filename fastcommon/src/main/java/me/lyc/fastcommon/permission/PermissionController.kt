package me.lyc.fastcommon.permission

import android.content.pm.PackageManager
import android.util.SparseArray
import me.lyc.fastcommon.log.LogUtils

/**
 * Created by Liu Yuchuan on 2020/8/2.
 */
internal object PermissionController {
    private const val TAG = "PermissionController"

    private val requestMap = SparseArray<PermissionRequest>()

    fun registerRequest(request: PermissionRequest) {
        requestMap.put(request.requestCode, request)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val request = requestMap[requestCode]
        if (request == null) {
            LogUtils.w(TAG, "Cannot find a request for requestCode=$requestCode")
            return
        }
        val rejectedPermissions = permissions.filterIndexed { index, _ ->
            grantResults[index] != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
        if (rejectedPermissions.isNotEmpty()) {
            request.callback.onPermissionRejected(permissions, rejectedPermissions)
        } else {
            request.callback.onPermissionsGranted(permissions)
        }
    }
}
