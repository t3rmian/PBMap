package io.github.t3r1jj.pbmap

import android.app.Application
import android.preference.PreferenceManager
import com.yariksoffice.lingver.Lingver
import io.github.t3r1jj.pbmap.logging.Config
import io.github.t3r1jj.pbmap.model.dictionary.MeasurementSystem
import io.github.t3r1jj.pbmap.settings.LanguageSettingChangeActivityCallbacks
import io.github.t3r1jj.pbmap.settings.SettingsLocaleStore
import java.util.*

class MapApplication : Application() {
    companion object {
        const val LANG = "io.github.t3r1jj.pbmap.MapApplication.LANG"
        const val DEBUG = "io.github.t3r1jj.pbmap.MapApplication.DEBUG"
        const val UNIT_SYSTEM = "io.github.t3r1jj.pbmap.MapApplication.UNIT_SYSTEM"
    }

    override fun onCreate() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val config = Config.getInstance()
        config.isDebug = preferences.getBoolean(DEBUG, false)
        Lingver.init(this, SettingsLocaleStore(this))
        registerActivityLifecycleCallbacks(LanguageSettingChangeActivityCallbacks())
        config.initPreferences(this, Lingver.getInstance().getLocale())
        super.onCreate()
    }
}