package io.github.t3r1jj.pbmap.main.external

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import com.yariksoffice.lingver.Lingver
import io.github.t3r1jj.pbmap.settings.SettingsLocaleStore

class LocaleListener(private val localeStore: SettingsLocaleStore) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val locale = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)
        localeStore.defaultLocale = locale
        Lingver.getInstance().setLocale(context, locale)
    }
}