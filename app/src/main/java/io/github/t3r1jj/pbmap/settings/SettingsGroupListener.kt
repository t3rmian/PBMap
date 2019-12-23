package io.github.t3r1jj.pbmap.settings

import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import io.github.t3r1jj.pbmap.MapApplication

/**
 * Implements preferences group
 */
internal class SettingsGroupListener(private val groupKey: String, private val group: List<CheckBoxPreference>,
                                     private val listener: PreferenceActivationListener) : Preference.OnPreferenceChangeListener {
    override fun onPreferenceChange(pref: Preference, newValue: Any?): Boolean {
        pref.isEnabled = false
        group.filter { it.key != pref.key }
                .forEach { it.isChecked = false; it.isEnabled = true }
        pref.preferenceManager.sharedPreferences.edit()
                .putString(groupKey, pref.key)
                .apply()
        listener.onPreferenceActivate(pref.key)
        return true
    }

    /**
     * Listener for a preference that is a part of the group
     */
    interface PreferenceActivationListener {
        /**
         * Callback for an activation of a group preference identified by a key
         */
        fun onPreferenceActivate(key: String)
    }
}