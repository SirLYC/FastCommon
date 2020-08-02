package me.lyc.fastcommon.arch

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Liu Yuchuan on 2020/1/18.
 */
inline fun <reified T : ViewModel> FragmentActivity.provideViewModel(): T {
    return ViewModelProvider(
        this,
        FastCommonViewModelFactory
    ).get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.provideViewModel(): T {
    return ViewModelProvider(
        this,
        FastCommonViewModelFactory
    ).get(T::class.java)
}

