package me.lyc.fastcommon.imagepick

import android.net.Uri

/**
 * Created by Liu Yuchuan on 2020/8/29.
 */
interface IImagePickCallback {
    fun onImagePicked(uri: Uri)

    fun onImagePickCancel()

    fun onImagePickError(code: Int, reason: String)
}
