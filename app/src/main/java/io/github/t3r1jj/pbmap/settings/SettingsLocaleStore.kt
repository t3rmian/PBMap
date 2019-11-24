package io.github.t3r1jj.pbmap.settings

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.yariksoffice.lingver.store.LocaleStore
import io.github.t3r1jj.pbmap.BuildConfig
import io.github.t3r1jj.pbmap.MapApplication
import io.github.t3r1jj.pbmap.model.dictionary.Dictionary
import io.github.t3r1jj.pbmap.model.i18n.LocaleUtils
import java.util.Locale

class SettingsLocaleStore @JvmOverloads constructor(
        context: Context,
        var defaultLocale: Locale = Locale.getDefault(),
        private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) : LocaleStore {

    override fun getLocale(): Locale {
        val savedLang = prefs.getString(MapApplication.LANG, null)
        val appLangs = Dictionary().getLanguages()
        return if (appLangs.contains(savedLang ?: null) && BuildConfig.DEFAULT_LANGUAGE != savedLang) {
            LocaleUtils.toLocale(savedLang!!)
        } else {
            prefs.edit().putString(MapApplication.LANG, BuildConfig.DEFAULT_LANGUAGE).apply()
            defaultLocale
        }
    }

    override fun persistLocale(locale: Locale) {
        prefs.edit().putString(MapApplication.LANG, LocaleUtils.toString(locale)).apply()
    }
}