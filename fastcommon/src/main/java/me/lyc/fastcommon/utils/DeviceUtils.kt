package me.lyc.fastcommon.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.view.Surface
import android.view.View
import android.view.Window
import android.view.WindowManager
import me.lyc.fastcommon.FastCommonLib
import me.lyc.fastcommon.log.LogUtils
import me.lyc.fastcommon.thread.doOnMainThread
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.math.roundToInt

/**
 * Created by Liu Yuchuan on 2020/1/8.
 */
private const val TAG = "DeviceUtils"

inline fun copyPlainText(text: String, callback: () -> Unit = {}) {
    (FastCommonLib.appContext
        .getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.run {
        setPrimaryClip(ClipData.newPlainText(null, text))
        callback()
    }
}

fun deviceWidth() = FastCommonLib.appContext.resources.displayMetrics.widthPixels

fun deviceHeight() = FastCommonLib.appContext.resources.displayMetrics.heightPixels

/**
 * Permission needed:
 * [android.Manifest.permission.VIBRATE]
 */
@SuppressLint("MissingPermission")
fun vibrate(millis: Long) {
    (FastCommonLib.appContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator)?.run {
        if (Build.VERSION.SDK_INT < 26) {
            vibrate(millis)
        } else {
            val effect =
                VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrate(effect)
        }
    }
}

fun isAutoBrightness(): Boolean {
    var isAuto = false
    try {
        isAuto = Settings.System.getInt(
            FastCommonLib.appContext.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE
        ) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
    } catch (e: SettingNotFoundException) {
        e.printStackTrace()
    }
    return isAuto
}

fun setBrightness(activity: Activity, brightness: Int) {
    doOnMainThread(Runnable {
        try {
            val lp = activity.window.attributes
            //将 0~255 范围内的数据，转换为 0~1
            lp.screenBrightness = brightness.toFloat() * (1f / 255f)
            activity.window.attributes = lp
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    })
}

fun getScreenBrightness(): Int {
    return if (isAutoBrightness()) {
        getAutoScreenBrightness()
    } else {
        getManualScreenBrightness()
    }
}

private fun getManualScreenBrightness(): Int {
    var nowBrightnessValue = 0
    val resolver = FastCommonLib.appContext.contentResolver
    try {
        nowBrightnessValue = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return nowBrightnessValue
}

private fun getAutoScreenBrightness(): Int {
    var nowBrightnessValue = 0f
    //获取自动调节下的亮度范围在 0~1 之间
    val resolver = FastCommonLib.appContext.contentResolver
    try {
        nowBrightnessValue = Settings.System.getFloat(resolver, Settings.System.SCREEN_BRIGHTNESS)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    //转换范围为 (0~255)
    val fValue = nowBrightnessValue * 255.0f
    return fValue.toInt()
}

fun setDefaultBrightness(activity: Activity) {
    try {
        val lp = activity.window.attributes
        lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        activity.window.attributes = lp
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
    }
}

fun getScreenOrientation(): Int {
    val appContext = FastCommonLib.appContext
    return (appContext.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.run {
        defaultDisplay.rotation
    }.let {
        return@let when (it) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }
    }
}


fun Window?.navigationBarBlackText(isBlack: Boolean) {
    if (Build.VERSION.SDK_INT >= 26) {
        this?.let { window ->
            window.decorView.run {
                if (isBlack) {
                    addSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
                } else {
                    clearSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
                }
            }
        }
    }
}

// -------------------------------------------- status bar -------------------------------------------- //

private val statusBarHeightLock = ReentrantLock()

@Volatile
private var statusBarHeightCache = -1

fun statusBarHeight(forceRetrieve: Boolean = false): Int {
    var result = statusBarHeightCache
    if (forceRetrieve || result <= 0) {
        val resources = FastCommonLib.appContext.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        } else {
            LogUtils.e(TAG, "getStatusBarHeight resourceId=$resourceId")
        }
        statusBarHeightLock.withLock {
            if (forceRetrieve || statusBarHeightCache <= 0) {
                statusBarHeightCache = result
            }
        }
    }

    return result
}

fun Window?.statusBarBlackText(isBlack: Boolean) {
    if (Build.VERSION.SDK_INT >= 23) {
        this?.let { window ->
            window.decorView.run {
                if (isBlack) {
                    addSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                } else {
                    clearSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }
            }
        }
    }
}

// -------------------------------------------- dimens -------------------------------------------- //

fun dp2px(dpVal: Int): Int {
    val resources = FastCommonLib.appContext.resources
    return (dpVal * resources.displayMetrics.density).roundToInt()
}

fun dp2pxf(dpVal: Float): Float {
    val resources = FastCommonLib.appContext.resources
    return dpVal * resources.displayMetrics.density
}

fun px2dp(dpVal: Int): Int {
    val resources = FastCommonLib.appContext.resources
    return (dpVal / resources.displayMetrics.density).roundToInt()
}

fun px2dpf(dpVal: Float): Float {
    val resources = FastCommonLib.appContext.resources
    return dpVal / resources.displayMetrics.density
}

fun sp2px(spVal: Int): Int {
    val resources = FastCommonLib.appContext.resources
    return (spVal * resources.displayMetrics.scaledDensity).roundToInt()
}

fun sp2pxf(spVal: Float): Float {
    val resources = FastCommonLib.appContext.resources
    return spVal * resources.displayMetrics.scaledDensity
}
