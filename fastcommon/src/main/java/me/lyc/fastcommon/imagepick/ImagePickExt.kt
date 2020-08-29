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
) {
    val fm = supportFragmentManager
    val fragment = fm.findFragmentByTag(ImagePickFragment.TAG) as? ImagePickFragment
        ?: ImagePickFragment().apply {
            fm.beginTransaction()
                .add(this, ImagePickFragment.TAG)
                .commitNow()
        }
    fragment.requestPick(nextActivityRequestCode(), callback)
}

fun FragmentActivity.pickImage(
    onImagePicked: (uri: Uri) -> Unit,
    onImagePickCancel: () -> Unit = {},
    onImagePickError: (code: Int, reason: String) -> Unit = { _, _ -> }
) {
    val fm = supportFragmentManager
    val fragment = fm.findFragmentByTag(ImagePickFragment.TAG) as? ImagePickFragment
        ?: ImagePickFragment().apply {
            fm.beginTransaction()
                .add(this, ImagePickFragment.TAG)
                .commitNow()
        }
    fragment.requestPick(nextActivityRequestCode(), object : IImagePickCallback {
        override fun onImagePicked(uri: Uri) {
            onImagePicked(uri)
        }

        override fun onImagePickCancel() {
            onImagePickCancel()
        }

        override fun onImagePickError(code: Int, reason: String) {
            onImagePickError(code, reason)
        }
    })
}
