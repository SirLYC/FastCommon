package me.lyc.fastcommon

import android.content.Context

/**
 * Created by Liu Yuchuan on 2020/8/2.
 */
object FastCommonLib {
    lateinit var appContext: Context
        private set

    fun init(context: Context) {
        appContext = context.applicationContext
    }
}
