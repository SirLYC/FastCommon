package me.lyc.fastcommon.arch

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * Created by Liu Yuchuan on 2020/1/30.
 */
open class LiveState<T>(initState: T) {
    protected val liveState = NonNullLiveData(initState)
    protected val liveEvent = SingleLiveEvent<T>().apply {
        liveState.observeForever { value = it }
    }

    protected var privateState: T
        @MainThread
        get() = liveState.value!!
        @MainThread
        set(value) {
            liveState.value = value
        }

    val state = privateState

    /**
     * 注册对状态变化的观察者
     */
    fun observeEvent(owner: LifecycleOwner, observer: Observer<in T>) {
        liveEvent.observe(owner, observer)
    }

    /**
     * 注册对状态的观察者
     */
    fun observeState(owner: LifecycleOwner, observer: Observer<in T>) {
        liveState.observe(owner, observer)
    }

    fun observeEventForever(observer: Observer<in T>) {
        liveEvent.observeForever(observer)
    }

    fun observeStateForever(observer: Observer<in T>) {
        liveState.observeForever(observer)
    }
}
