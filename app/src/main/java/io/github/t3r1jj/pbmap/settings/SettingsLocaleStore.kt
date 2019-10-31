package io.github.t3r1jj.pbmap.settings

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.yariksoffice.lingver.store.LocaleStore
import io.github.t3r1jj.pbmap.MapApplication
import io.github.t3r1jj.pbmap.model.Dictionary
import java.util.*

class SettingsLocaleStore @JvmOverloads constructor(
        context: Context,
        private val defaultLocale: Locale = Locale.getDefault(),
        private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) : LocaleStore {

    override fun getLocale(): Locale {
        val savedLang = prefs.getString(MapApplication.LANG, null)
        val appLangs = Dictionary().getLanguages()
        return if (appLangs.contains(savedLang)) {
            Locale(savedLang)
        } else {
            persistLocale(defaultLocale)
            defaultLocale
        }
    }

    override fun persistLocale(locale: Locale) {
        prefs.edit().putString(MapApplication.LANG, locale.language).apply()
    }
}