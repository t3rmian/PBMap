package io.github.t3r1jj.pbmap

import android.app.Application
import android.preference.PreferenceManager
import com.yariksoffice.lingver.Lingver
import io.github.t3r1jj.pbmap.model.Dictionary
import io.github.t3r1jj.pbmap.settings.LanguageSettingChangeActivityCallbacks

class MapApplication : Application() {
    companion object {
        const val LANG = "io.github.t3r1jj.pbmap.MapApplication.LANG"
    }

    override fun onCreate() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val savedLang = preferences.getString(LANG, null)
        val appLangs = Dictionary().getLanguages()
        if (appLangs.contains(savedLang)) {
            Lingver.init(this, savedLang!!)
        } else {
            Lingver.init(this)
            preferences.edit()
                    .putString(LANG, Lingver.getInstance().getLanguage())
                    .apply()
        }
        registerActivityLifecycleCallbacks(LanguageSettingChangeActivityCallbacks())
        super.onCreate()
    }
}