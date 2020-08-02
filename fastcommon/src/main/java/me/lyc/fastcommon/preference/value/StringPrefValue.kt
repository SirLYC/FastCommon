package me.lyc.fastcommon.preference.value

import me.lyc.fastcommon.preference.IPreference

/**
 * Created by Liu Yuchuan on 2020/2/7.
 */
class StringPrefValue(
    key: String,
    defaultValue: String,
    preference: IPreference,
    validator: ((value: String) -> String)? = null
) : PrefValue<String>(key, defaultValue, preference, { k, defVal ->
    preference.getString(
        k,
        defVal
    ) ?: defVal
}, { k, v ->
    preference.putString(
        k,
        v
    )
}, validator, null)
