package me.lyc.fastcommon.bitmap

import android.media.ExifInterface
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * Created by Liu Yuchuan on 2020/8/29.
 */
class FileBitmapSource(private val file: File) : BitmapSource {
    override fun openBitmapInputStream(): InputStream {
        return FileInputStream(file)
    }

    override fun exifInterface(): ExifInterface? {
        return ExifInterface(file.absolutePath)
    }
}
