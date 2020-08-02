package me.lyc.fastcommon.preference.value

import me.lyc.fastcommon.preference.IPreference

/**
 * Created by Liu Yuchuan on 2020/2/7.
 */
class IntPrefValue(
    key: String,
    defaultValue: Int,
    preference: IPreference,
    validator: ((value: Int) -> Int)? = null
) : PrefValue<Int>(key, defaultValue, preference, { k, defVal ->
    preference.getInt(
        k,
        defVal
    )
}, { k, v ->
    preference.putInt(
        k,
        v
    )
}, validator, null) {
    fun inc() {
        value++
    }

    fun dec() {
        value--
    }
}
