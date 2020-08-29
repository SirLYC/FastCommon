package me.lyc.fastcommon.imagepick

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import me.lyc.fastcommon.nextActivityRequestCode

/**
 * Created by Liu Yuchuan on 2020/8/29.
 */
const val ERROR_CODE_INTERNAL = -1
const val ERROR_CODE_NO_PICKER = -3

fun FragmentActivity.pickImage(
    callback: IImagePickCallback
): Int {
    val fm = supportFragmentManager
    val fragment = fm.findFragmentByTag(ImagePickFragment.TAG) as? ImagePickFragment
        ?: ImagePickFragment().apply {
            fm.beginTransaction()
                .add(this, ImagePickFragment.TAG)
                .commitNow()
        }
    val code = nextActivityRequestCode()
    fragment.requestPick(code, callback)
    return code
}

fun FragmentActivity.pickImage(
    onImagePicked: (uri: Uri) -> Unit,
    onImagePickCancel: () -> Unit = {},
    onImagePickError: (code: Int, reason: String) -> Unit = { _, _ -> }
): Int {
    val callback = object : IImagePickCallback {
        override fun onImagePicked(uri: Uri) {
            onImagePicked(uri)
        }

        override fun onImagePickCancel() {
            onImagePickCancel()
        }

        override fun onImagePickError(code: Int, reason: String) {
            onImagePickError(code, reason)
        }
    }
    return pickImage(callback)
}

/**
 * This method can avoid memory leak
 * @param code  returned by [pickImage]
 */
fun cancelPickImage(code: Int) {
    ImagePickController.cancelRequest(code)
}
