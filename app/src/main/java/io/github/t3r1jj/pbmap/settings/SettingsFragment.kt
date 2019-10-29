package io.github.t3r1jj.pbmap.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.yariksoffice.lingver.Lingver
import io.github.t3r1jj.pbmap.MapApplication
import io.github.t3r1jj.pbmap.R
import io.github.t3r1jj.pbmap.model.Dictionary

class SettingsFragment : PreferenceFragmentCompat(), PreferenceGroupListener.PreferenceActivationListener {
    private val dictionary = Dictionary()

    override fun onPreferenceActivate(key: String) {
        Lingver.getInstance().setLocale(context!!, key)
        activity!!.recreate()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val languageCategory = preferenceManager.preferenceScreen.findPreference<PreferenceCategory>(MapApplication.LANG)!!
        val langPrefs = mutableListOf<CheckBoxPreference>()
        dictionary.getLanguages().forEach {
            val prefs = preferenceManager.sharedPreferences
            val langPref = createLanguagePreference(it, prefs, langPrefs)
            languageCategory.addPreference(langPref)
        }
    }

    private fun createLanguagePreference(lang: String, sharedPreferences: SharedPreferences,
                                         langPrefs: MutableList<CheckBoxPreference>): CheckBoxPreference {
        val langPref = CheckBoxPreference(context)
        langPref.key = lang
        langPref.title = dictionary.getI18nLanguage(context!!, lang)
        langPref.isEnabled = langPref.key != sharedPreferences.getString(MapApplication.LANG, null)
        langPref.isChecked = langPref.key == sharedPreferences.getString(MapApplication.LANG, null)
        langPref.onPreferenceChangeListener = PreferenceGroupListener(langPrefs, this)
        langPrefs.add(langPref)
        return langPref
    }
}