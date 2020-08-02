package me.lyc.fastcommon.toast

import android.widget.Toast
import androidx.annotation.StringRes
import me.lyc.fastcommon.FastCommonLib

/**
 * Created by Liu Yuchuan on 2020/8/2.
 */
fun showToast(text: String) {
    Toast.makeText(FastCommonLib.appContext, text, Toast.LENGTH_SHORT).show()
}

fun showToast(@StringRes textRes: Int) {
    showToast(FastCommonLib.appContext.getString(textRes))
}

fun showLongToast(text: String) {
    Toast.makeText(FastCommonLib.appContext, text, Toast.LENGTH_LONG).show()
}

fun showLongToast(@StringRes textRes: Int) {
    showLongToast(FastCommonLib.appContext.getString(textRes))
}
