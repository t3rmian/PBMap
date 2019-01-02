package io.github.t3r1jj.pbmap.model

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.core.os.ConfigurationCompat
import java.util.*

class Translator(base: Context) : ContextWrapper(base) {
    var locale: Locale = ConfigurationCompat.getLocales(resources.configuration)[0]

    fun translateDescription(resStr: String, vararg formatArgs: Any): String {
        return translate(resStr + "_description", formatArgs)
    }

    fun translateDescription(resStr: String): String {
        return translate(resStr + "_description")
    }

    fun translateName(resStr: String, vararg formatArgs: Any): String {
        return translate(resStr + "_name", formatArgs)
    }

    fun translateName(resStr: String): String {
        return translate(resStr + "_name")
    }

    fun translate(resStr: String, vararg formatArgs: Any): String {
        val resId = getStringResource(resStr)
        return if (resId == 0) {
            Log.w("i18n", String.format("Unable to translate %s, missing resource string"))
            resStr
        } else translate(resId, formatArgs)
    }

    fun translate(resStr: String): String {
        val resId = getStringResource(resStr)
        return if (resId == 0) {
            Log.w("i18n", String.format("Unable to translate %s, missing resource string"))
            resStr
        } else translate(resId)
    }

    private fun getStringResource(aString: String): Int {
        return resources.getIdentifier(aString, "string", packageName)
    }

    fun translate(resId: Int): String {
        return this.getString(resId)
    }

    fun translate(resId: Int, vararg formatArgs: Any): String {
        return this.getString(resId, formatArgs)
    }
}