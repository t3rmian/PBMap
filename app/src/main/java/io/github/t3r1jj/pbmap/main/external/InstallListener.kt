package io.github.t3r1jj.pbmap.main.external

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.preference.PreferenceManager

/**
 * To test a referrer broadcast from ADB:
 *
 * adb shell
 *
 * am broadcast -a com.android.vending.INSTALL_REFERRER -n io.github.t3r1jj.pbmap/.main.external.InstallListener --es "referrer" "https%3A%2F%2Fpbmap.termian.dev%2Fmobile%2Fpb_acs_l2"
 */
class InstallListener : BroadcastReceiver() {
    companion object {
        const val REFERRER = "io.github.t3r1jj.pbmap.main.external.InstallListener.REFERRER"
        private const val REFERRER_EXTRA_INTENT = "referrer"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit()
                .putString(REFERRER, Uri.decode(intent.getStringExtra(REFERRER_EXTRA_INTENT)))
                .apply()
    }
}