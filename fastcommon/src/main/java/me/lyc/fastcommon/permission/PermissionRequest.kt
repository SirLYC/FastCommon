package me.lyc.fastcommon.permission

/**
 * Created by Liu Yuchuan on 2020/8/2.
 */
class PermissionRequest(
    val permissions: Array<String>,
    val callback: IPermissionCallback
) {
    val requestCode = generateNewPermissionRequestCode()
}
