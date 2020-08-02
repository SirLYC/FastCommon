package me.lyc.fastcommon.arch

import androidx.annotation.MainThread

/**
 * Created by Liu Yuchuan on 2020/1/20.
 */
class FuncCallLiveEvent : SingleLiveEvent<Void>() {
    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}
