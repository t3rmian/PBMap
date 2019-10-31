package io.github.t3r1jj.pbmap

import android.app.Application
import android.preference.PreferenceManager
import com.yariksoffice.lingver.Lingver
import com.yariksoffice.lingver.store.LocaleStore
import io.github.t3r1jj.pbmap.logging.Config
import io.github.t3r1jj.pbmap.model.Dictionary
import io.github.t3r1jj.pbmap.settings.LanguageSettingChangeActivityCallbacks
import io.github.t3r1jj.pbmap.settings.SettingsLocaleStore

class MapApplication : Application() {
    companion object {
        const val LANG = "io.github.t3r1jj.pbmap.MapApplication.LANG"
        const val DEBUG = "io.github.t3r1jj.pbmap.MapApplication.DEBUG"
    }

    override fun onCreate() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        Config.getInstance().isDebug = preferences.getBoolean(DEBUG, false)
        Lingver.init(this, SettingsLocaleStore(this))
        registerActivityLifecycleCallbacks(LanguageSettingChangeActivityCallbacks())
        super.onCreate()
    }
}