package me.lyc.fastcommon.preference.value

import me.lyc.fastcommon.preference.IPreference

/**
 * Created by Liu Yuchuan on 2020/2/7.
 */
class FloatPrefValue(
    key: String,
    defaultValue: Float,
    preference: IPreference,
    validator: ((value: Float) -> Float)? = null
) : PrefValue<Float>(key, defaultValue, preference, { k, defVal ->
    preference.getFloat(
        k,
        defVal
    )
}, { k, v -> preference.putFloat(k, v) }, validator, null)
