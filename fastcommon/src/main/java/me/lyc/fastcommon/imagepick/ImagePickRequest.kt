package me.lyc.fastcommon.imagepick

import java.lang.ref.WeakReference

/**
 * Created by Liu Yuchuan on 2020/8/29.
 */
internal class ImagePickRequest(
    val requestCode: Int,
    callback: IImagePickCallback
) {
    private val callbackRef = WeakReference(callback)
    val callback
        get() = callbackRef.get()
}
