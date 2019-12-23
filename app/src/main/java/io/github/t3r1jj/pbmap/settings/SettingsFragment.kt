package io.github.t3r1jj.pbmap.settings

import android.os.Bundle
import androidx.preference.*
import com.yariksoffice.lingver.Lingver
import io.github.t3r1jj.pbmap.MapApplication
import io.github.t3r1jj.pbmap.R
import io.github.t3r1jj.pbmap.Config
import io.github.t3r1jj.pbmap.main.drawer.MapsDrawerFragment
import io.github.t3r1jj.pbmap.model.dictionary.Dictionary
import io.github.t3r1jj.pbmap.model.i18n.LocaleUtils

/**
 * Preferences fragment
 */
class SettingsFragment : PreferenceFragmentCompat(), SettingsGroupListener.PreferenceActivationListener {
    private companion object {
        private const val REFRESH_ACTIVITY_STACK = "refresh"
    }

    private val dictionary = Dictionary()

    override fun onPreferenceActivate(key: String) {
        activity?.let {
            if (REFRESH_ACTIVITY_STACK == key || dictionary.getUnitSystems().contains(key)) {
                it.setResult(MapsDrawerFragment.RECREATE_REQUEST_RESULT_CODE)
                it.finish()
            } else {
                Lingver.getInstance().setLocale(it, LocaleUtils.toLocale(key))
                it.recreate()
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        setUpPreferenceGroup(MapApplication.LANG, dictionary.getLanguages()) { dictionary.getI18nLanguage(context!!, it) }
        setUpPreferenceGroup(MapApplication.UNIT_SYSTEM, dictionary.getUnitSystems()) { dictionary.getI18nUnitSystem(context!!, it) }
        preferenceManager.findPreference<SwitchPreference>(MapApplication.DEBUG)!!
                .onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            Config.getInstance().isDebug = true == newValue
            onPreferenceActivate(REFRESH_ACTIVITY_STACK)
            true
        }
    }

    private fun setUpPreferenceGroup(categoryGroupKey: String, keys: List<String>, translation: (String) -> String) {
        val category = preferenceManager.preferenceScreen.findPreference<PreferenceCategory>(categoryGroupKey)!!
        val prefs = mutableListOf<CheckBoxPreference>()
        keys.forEach {
            val preference = setUpGroupPreference(categoryGroupKey, it, translation(it), prefs)
            category.addPreference(preference)
        }
    }

    private fun setUpGroupPreference(groupKey: String, key: String, label: String,
                                     prefGroup: MutableList<CheckBoxPreference>): CheckBoxPreference {
        val prefs = preferenceManager.sharedPreferences
        val pref = CheckBoxPreference(context)
        pref.key = key
        pref.title = label
        val selectedKey = prefs.getString(groupKey, null)
        pref.isEnabled = pref.key != selectedKey
        pref.isChecked = pref.key == selectedKey
        pref.onPreferenceChangeListener = SettingsGroupListener(groupKey, prefGroup, this)
        prefGroup.add(pref)
        return pref
    }
}