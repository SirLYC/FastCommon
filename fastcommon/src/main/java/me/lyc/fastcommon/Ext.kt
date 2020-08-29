package me.lyc.fastcommon

/**
 * Created by Liu Yuchuan on 2020/8/29.
 */

private var currentRequestCode = 1

internal fun nextActivityRequestCode(): Int {
    val result = currentRequestCode
    currentRequestCode++
    if (currentRequestCode == 0xffff) {
        currentRequestCode = 1
    }
    return result
}
