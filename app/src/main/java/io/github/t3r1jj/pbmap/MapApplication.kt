package io.github.t3r1jj.pbmap

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.preference.PreferenceManager
import androidx.core.os.ConfigurationCompat
import com.yariksoffice.lingver.Lingver
import io.github.t3r1jj.pbmap.logging.Config
import io.github.t3r1jj.pbmap.settings.LanguageSettingChangeActivityCallbacks
import io.github.t3r1jj.pbmap.settings.SettingsLocaleStore

class MapApplication : Application() {
    companion object {
        const val LANG = "io.github.t3r1jj.pbmap.MapApplication.LANG"
        const val DEBUG = "io.github.t3r1jj.pbmap.MapApplication.DEBUG"
        const val UNIT_SYSTEM = "io.github.t3r1jj.pbmap.MapApplication.UNIT_SYSTEM"
    }

    lateinit var localeStore: SettingsLocaleStore
    private val localeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val locale = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)
            localeStore.defaultLocale = locale
            Lingver.getInstance().setLocale(context, locale)
        }
    }

    override fun onCreate() {
        localeStore = SettingsLocaleStore(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val config = Config.getInstance()
        config.isDebug = preferences.getBoolean(DEBUG, false)
        Lingver.init(this, localeStore)
        registerActivityLifecycleCallbacks(LanguageSettingChangeActivityCallbacks())
        config.initPreferences(this, Lingver.getInstance().getLocale())
        val systemLocaleFilter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
        registerReceiver(localeReceiver, systemLocaleFilter)
        super.onCreate()
    }
}