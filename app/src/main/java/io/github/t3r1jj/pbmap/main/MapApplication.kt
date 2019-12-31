package io.github.t3r1jj.pbmap.main

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.preference.PreferenceManager
import com.yariksoffice.lingver.Lingver
import io.github.t3r1jj.pbmap.main.external.LocaleListener
import io.github.t3r1jj.pbmap.settings.Config
import io.github.t3r1jj.pbmap.settings.LanguageSettingChangeActivityCallbacks
import io.github.t3r1jj.pbmap.settings.SettingsLocaleStore

/**
 * Implements application lifecycle language reload
 */
class MapApplication : Application() {
    companion object {
        const val LANG = "io.github.t3r1jj.pbmap.main.MapApplication.LANG"
        const val DEBUG = "io.github.t3r1jj.pbmap.main.MapApplication.DEBUG"
        const val UNIT_SYSTEM = "io.github.t3r1jj.pbmap.main.MapApplication.UNIT_SYSTEM"
    }

    override fun onCreate() {
        val localeStore = SettingsLocaleStore(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val config = Config.getInstance()
        config.isDebug = preferences.getBoolean(DEBUG, false)
        Lingver.init(this, localeStore)
        registerActivityLifecycleCallbacks(LanguageSettingChangeActivityCallbacks())
        config.initPreferences(this, Lingver.getInstance().getLocale())
        val systemLocaleFilter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
        registerReceiver(LocaleListener(localeStore), systemLocaleFilter)
        super.onCreate()
    }
}