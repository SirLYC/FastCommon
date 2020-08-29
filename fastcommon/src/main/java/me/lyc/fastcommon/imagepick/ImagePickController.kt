package me.lyc.fastcommon.imagepick

import android.app.Activity
import android.content.Intent
import android.util.SparseArray
import androidx.core.util.contains
import me.lyc.fastcommon.log.LogUtils

/**
 * Created by Liu Yuchuan on 2020/8/29.
 */
internal object ImagePickController {
    private const val TAG = "ImagePickController"

    private val requestMap = SparseArray<ImagePickRequest>()

    fun registerImagePick(request: ImagePickRequest): Boolean {
        if (requestMap.contains(request.requestCode)) {
            return false
        }

        requestMap.put(request.requestCode, request)
        return true
    }

    fun handleResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val request = requestMap[requestCode]
        if (request == null) {
            LogUtils.w(TAG, "No request found for code=$requestCode")
            return
        }
        if (resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                request.callback?.onImagePicked(uri)
            } else {
                request.callback?.onImagePickError(ERROR_CODE_INTERNAL, "Receive null uri!")
            }
        } else {
            request.callback?.onImagePickCancel()
        }
    }

    fun cancelRequest(code: Int) {
        requestMap.remove(code)
    }
}
