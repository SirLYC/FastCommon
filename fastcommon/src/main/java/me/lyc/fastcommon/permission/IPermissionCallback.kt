package me.lyc.fastcommon.permission

/**
 * Created by Liu Yuchuan on 2020/8/2.
 */
interface IPermissionCallback {
    fun onPermissionsGranted(permissions: Array<out String>)

    fun onPermissionRejected(permissions: Array<out String>, rejectedPermissions: Array<out String>)
}
