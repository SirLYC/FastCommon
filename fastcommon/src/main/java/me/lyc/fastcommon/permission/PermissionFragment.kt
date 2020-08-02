package me.lyc.fastcommon.permission

import androidx.fragment.app.Fragment

/**
 * Created by Liu Yuchuan on 2020/8/2.
 */
internal class PermissionFragment : Fragment() {
    internal companion object {
        const val TAG = "PermissionFragment"
    }

    fun requestPermissions(request: PermissionRequest) {
        PermissionController.registerRequest(request)
        requestPermissions(request.permissions, request.requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionController.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
