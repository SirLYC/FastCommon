package me.lyc.fastcommon.bitmap

import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.core.net.toFile
import me.lyc.fastcommon.FastCommonLib
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

/**
 * Created by Liu Yuchuan on 2020/8/29.
 */
class UriBitmapSource(
    private val uri: Uri
) : BitmapSource {

    @Throws(IOException::class)
    override fun openBitmapInputStream(): InputStream {
        return FastCommonLib.appContext.contentResolver.openInputStream(uri)
            ?: throw FileNotFoundException("Cannot open uri $uri")
    }

    override fun exifInterface(): ExifInterface? {
        return when {
            Build.VERSION.SDK_INT >= 24 -> {
                ExifInterface(openBitmapInputStream())
            }
            uri.scheme == "file" -> {
                ExifInterface(uri.toFile().absolutePath)
            }
            else -> {
                null
            }
        }
    }
}
