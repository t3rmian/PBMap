package io.github.t3r1jj.pbmap.main.external

import android.content.Intent
import android.preference.PreferenceManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InstallListenerIT {

    @Test
    @SmallTest
    fun onReceive() {
        val installListener = InstallListener()
        val intent = Intent()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        installListener.onReceive(context, intent)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        assertEquals(null, preferences.getString(InstallListener.REFERRER, null))

        intent.putExtra(InstallListener.REFERRER, null as String?)
        installListener.onReceive(context, intent)
        assertEquals(null, preferences.getString(InstallListener.REFERRER, null))

        intent.putExtra(InstallListener.REFERRER, "abc")
        installListener.onReceive(context, intent)
        assertEquals("abc", preferences.getString(InstallListener.REFERRER, null))

        intent.putExtra(InstallListener.REFERRER, "https%3A%2F%2Fpbmap.termian.dev%2Fmobile%2Fpb_acs%2Fb02")
        installListener.onReceive(context, intent)
        assertEquals("https%3A%2F%2Fpbmap.termian.dev%2Fmobile%2Fpb_acs%2Fb02", preferences.getString(InstallListener.REFERRER, null))
    }
}