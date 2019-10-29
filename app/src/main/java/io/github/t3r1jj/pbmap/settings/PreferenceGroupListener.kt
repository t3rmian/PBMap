package io.github.t3r1jj.pbmap.settings

import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import io.github.t3r1jj.pbmap.MapApplication

internal class PreferenceGroupListener(private val group: List<CheckBoxPreference>,
                                       private val listener: PreferenceActivationListener) : Preference.OnPreferenceChangeListener {
    override fun onPreferenceChange(pref: Preference, newValue: Any?): Boolean {
        pref.isEnabled = false
        group.filter { it.key != pref.key }
                .forEach { it.isChecked = false; it.isEnabled = true }
        pref.preferenceManager.sharedPreferences.edit()
                .putString(MapApplication.LANG, pref.key)
                .apply()
        listener.onPreferenceActivate(pref.key)
        return true
    }

    interface PreferenceActivationListener {
        fun onPreferenceActivate(key: String)
    }
}