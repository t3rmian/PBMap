package io.github.t3r1jj.pbmap.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager

/**
 * To test a referrer broadcast from ADB:
 *
 * adb shell
 *
 * am broadcast -a com.android.vending.INSTALL_REFERRER -n com.example.android.custom.referrer.receiver/.ReferrerReceiver --es "referrer"
 */
class InstallListener : BroadcastReceiver() {
    companion object {
        const val REFERRER = "io.github.t3r1jj.pbmap.main.InstallListener.REFERRER"
        private const val REFERRER_EXTRA_INTENT = "referrer"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit()
                .putString(REFERRER, intent.getStringExtra(REFERRER_EXTRA_INTENT))
                .apply()
    }
}