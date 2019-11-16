package io.github.t3r1jj.pbmap.settings

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.preference.PreferenceManager
import com.yariksoffice.lingver.Lingver
import io.github.t3r1jj.pbmap.MapApplication
import io.github.t3r1jj.pbmap.model.i18n.LocaleUtils

class LanguageSettingChangeActivityCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        sharedPreferences.edit()
                .putString(getCurrentActivityLangKey(activity), LocaleUtils.toString(Lingver.getInstance().getLocale()))
                .apply()
    }

    override fun onActivityResumed(activity: Activity) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val prevLang = sharedPreferences.getString(getCurrentActivityLangKey(activity), null)
        prevLang?.let {
            if (prevLang != LocaleUtils.toString(Lingver.getInstance().getLocale())) {
                activity.recreate()
            }
        }
    }

    private fun getCurrentActivityLangKey(activity: Activity) =
            activity::class.java.name + "_" + MapApplication.LANG

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        sharedPreferences.edit()
                .remove(getCurrentActivityLangKey(activity))
                .apply()
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

}