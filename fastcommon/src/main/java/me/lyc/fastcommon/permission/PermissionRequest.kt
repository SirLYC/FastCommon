package me.lyc.fastcommon.permission

import java.lang.ref.WeakReference

/**
 * Created by Liu Yuchuan on 2020/8/2.
 */
class PermissionRequest(
    val permissions: Array<String>,
    callback: IPermissionCallback
) {
    val callback
        get() = callbackRef.get()
    private val callbackRef = WeakReference(callback)
    val requestCode = generateNewPermissionRequestCode()
}
