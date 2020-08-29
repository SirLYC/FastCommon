package me.lyc.fastcommon.bitmap

import android.media.ExifInterface
import java.io.InputStream

/**
 * Created by Liu Yuchuan on 2020/8/29.
 */
internal interface BitmapSource {

    fun openBitmapInputStream(): InputStream

    fun exifInterface(): ExifInterface?

}
