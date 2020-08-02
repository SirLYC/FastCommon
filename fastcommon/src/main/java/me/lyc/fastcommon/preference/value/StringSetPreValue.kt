package me.lyc.fastcommon.preference.value

import me.lyc.fastcommon.preference.IPreference

/**
 * Created by Liu Yuchuan on 2020/2/14.
 */
class StringSetPreValue(
    key: String,
    defaultValue: Set<String>,
    preference: IPreference,
    validator: ((value: Set<String>) -> Set<String>)? = null
) : PrefValue<Set<String>>(key, defaultValue, preference, { k, defVal ->
    preference.getStringSet(
        k,
        defVal
    ) ?: defVal
}, { k, v ->
    preference.putStringSet(
        k,
        v
    )
}, validator, null)
