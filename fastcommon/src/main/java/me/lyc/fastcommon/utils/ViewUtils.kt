package me.lyc.fastcommon.utils

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.IntRange

/**
 * Created by Liu Yuchuan on 2020/1/18.
 */
fun Drawable.changeToColor(newColor: Int) {
    colorFilter = PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_ATOP)
}

fun Int.addColorAlpha(@IntRange(from = 0, to = 0xff) alpha: Int) =
    this and ((alpha shl 24) or 0xFFFFFFFF.toInt())

fun blendColor(bg: Int, fg: Int): Int {
    val scr = Color.red(fg)
    val scg = Color.green(fg)
    val scb = Color.blue(fg)
    val sa = fg ushr 24
    val dcr = Color.red(bg)
    val dcg = Color.green(bg)
    val dcb = Color.blue(bg)
    val colorR = dcr * (0xff - sa) / 0xff + scr * sa / 0xff
    val colorG = dcg * (0xff - sa) / 0xff + scg * sa / 0xff
    val colorB = dcb * (0xff - sa) / 0xff + scb * sa / 0xff
    return ((colorR shl 16) + (colorG shl 8) + colorB) or (0xff000000.toInt())
}

fun getCenterColor(color1: Int, color2: Int): Int {
    val r1 = Color.red(color1)
    val g1 = Color.green(color1)
    val b1 = Color.blue(color1)
    val r2 = Color.red(color2)
    val g2 = Color.green(color2)
    val b2 = Color.blue(color2)
    return Color.rgb((r1 + r2) / 2, (g1 + g2) / 2, (b1 + b2) / 2)
}

fun isLightColor(color: Int): Boolean {
    // RGB 转 YUV
    val darkness: Double =
        1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(
            color
        )) / 255
    return darkness < 0.5
}

private val paint by lazy { Paint() }
fun measureSingleLineTextHeight(size: Float): Float {
    paint.textSize = size
    return paint.fontMetrics.run { descent - ascent }
}

var TextView.textSizeInPx
    get() = textSize
    set(value) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
    }

var TextView.textSizeInDp
    get() = px2dpf(textSizeInPx)
    set(value) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, dp2pxf(value))
    }

fun View.addSystemUiVisibility(vararg flags: Int) {
    var result = systemUiVisibility
    flags.forEach {
        result = result or it
    }
    systemUiVisibility = result
}

fun View.clearSystemUiVisibility(vararg flags: Int) {
    var result = systemUiVisibility
    flags.forEach {
        result = result and it.inv()
    }
    systemUiVisibility = result
}
