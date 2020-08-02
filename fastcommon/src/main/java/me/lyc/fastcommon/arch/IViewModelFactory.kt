package me.lyc.fastcommon.arch

import androidx.lifecycle.ViewModel

/**
 * Created by Liu Yuchuan on 2020/1/18.
 */
interface IViewModelFactory {

    fun <T : ViewModel> createViewMode(modelClass: Class<T>): T?

    // 如果返回true，则下次遇到这个ViewModel会直接查表用这个Factory构建
    // 而不会再遍历Extensions
    fun shouldCache() = false
}
