package me.lyc.fastcommon.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import java.io.File
import kotlin.math.max
import kotlin.math.min

/**
 * Created by Liu Yuchuan on 2020/8/29.
 */

fun decodeBitmap(
    file: File,
    maxWidth: Int = Int.MAX_VALUE,
    maxHeight: Int = Int.MAX_VALUE,
    config: Bitmap.Config = Bitmap.Config.RGB_565
): Bitmap? {
    return decodeBitmap(FileBitmapSource(file), maxWidth, maxHeight, config)
}

fun decodeBitmap(
    uri: Uri,
    maxWidth: Int = Int.MAX_VALUE,
    maxHeight: Int = Int.MAX_VALUE,
    config: Bitmap.Config = Bitmap.Config.RGB_565
): Bitmap? {
    return decodeBitmap(UriBitmapSource(uri), maxWidth, maxHeight, config)
}

internal fun decodeBitmap(
    source: BitmapSource,
    maxWidth: Int,
    maxHeight: Int,
    config: Bitmap.Config
): Bitmap? {
    val outlineStream = source.openBitmapInputStream()
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    outlineStream.use {
        BitmapFactory.decodeStream(outlineStream, null, options)
    }
    val orientation = when (source.exifInterface()
        ?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
        ExifInterface.ORIENTATION_NORMAL -> 0
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }
    options.inPreferredConfig = config
    options.inJustDecodeBounds = false
    options.inSampleSize = calInSampleSize(maxWidth, maxHeight, options, orientation)
    val tmpBitmap = source.openBitmapInputStream().use {
        BitmapFactory.decodeStream(it, null, options)
    }
    if (tmpBitmap == null) {
        return tmpBitmap
    }

    val bitmapWidth: Int
    val bitmapHeight: Int
    if (orientation % 180 == 90) {
        bitmapWidth = tmpBitmap.height
        bitmapHeight = tmpBitmap.width
    } else {
        bitmapWidth = tmpBitmap.width
        bitmapHeight = tmpBitmap.height
    }

    val sizeOk = bitmapWidth <= maxWidth && bitmapHeight <= maxHeight
    if (sizeOk && orientation == 0) {
        return tmpBitmap
    }

    val scale = if (sizeOk) 1f else min(
        maxWidth.toFloat() / bitmapWidth.toFloat(),
        maxHeight.toFloat() / bitmapHeight.toFloat()
    )

    val matrix = Matrix()
    matrix.reset()
    if (orientation != 0) {
        matrix.setRotate(orientation.toFloat(), tmpBitmap.width / 2f, tmpBitmap.height / 2f)
    }
    matrix.postScale(scale, scale)
    return Bitmap.createBitmap(tmpBitmap, 0, 0, tmpBitmap.width, tmpBitmap.height, matrix, false)
}

private fun calInSampleSize(
    maxWidth: Int,
    maxHeight: Int,
    options: BitmapFactory.Options,
    orientation: Int
): Int {
    if (options.outWidth <= 0 || options.outHeight <= 0 || maxHeight <= 0 || maxWidth <= 0) {
        return 1
    }

    val picWidth: Int
    val picHeight: Int
    if (orientation % 180 == 90) {
        picWidth = options.outHeight
        picHeight = options.outWidth
    } else {
        picWidth = options.outWidth
        picHeight = options.outHeight
    }

    if (picWidth <= maxWidth && picHeight <= maxHeight) {
        return 1
    }

    val scale = max(
        picWidth.toFloat() / maxWidth.toFloat(),
        picHeight.toFloat() / maxHeight.toFloat()
    )

    if (scale <= 1) {
        return 1
    }

    var rate = 1
    while (rate < scale) {
        rate *= 2
    }

    if (rate <= 1) {
        return rate
    }

    return rate / 2
}
