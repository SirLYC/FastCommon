package me.lyc.fastcommon.imagepick

import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * Created by Liu Yuchuan on 2020/8/29.
 */
class ImagePickFragment : Fragment() {
    internal companion object {
        const val TAG = "ImagePickFragment"
    }

    internal fun requestPick(code: Int, callback: IImagePickCallback) {
        val activity = activity
        if (host == null || activity == null) {
            callback.onImagePickError(ERROR_CODE_INTERNAL, "host == null || activity == null")
            return
        }
        if (ImagePickController.registerImagePick(ImagePickRequest(code, callback))) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            val resolveActivity = intent.resolveActivity(activity.packageManager)
            if (resolveActivity == null) {
                callback.onImagePickError(ERROR_CODE_NO_PICKER, "No picker can handle request.")
                ImagePickController.cancelRequest(code)
            } else {
                startActivityForResult(intent, code)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ImagePickController.handleResult(requestCode, resultCode, data)
    }
}
