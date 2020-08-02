package me.lyc.fastcommon.arch

import androidx.annotation.AnyThread
import androidx.annotation.MainThread

/**
 * Created by Liu Yuchuan on 2020/1/30.
 */
class MutableLiveState<T>(initState: T) : LiveState<T>(initState) {

    @MainThread
    fun setState(value: T) {
        privateState = value
    }

    fun changeState(newVal: T): Boolean {
        if (privateState != newVal) {
            privateState = newVal
            return true
        }
        return false
    }

    @AnyThread
    fun postState(value: T) {
        liveState.postValue(value)
    }
}
