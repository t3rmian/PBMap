package io.github.t3r1jj.pbmap.model.i18n

import android.content.res.Resources
import android.util.Log
import io.github.t3r1jj.pbmap.BuildConfig

open class Translator(val resources: Resources) {
    companion object {
        const val DESCRIPTION_PREFIX = "description_"
        const val NAME_PREFIX = "name_"
    }

    open fun translateDescription(resStr: String, vararg formatArgs: Any): String {
        return translate(DESCRIPTION_PREFIX + resStr, formatArgs) ?: resStr
    }

    open fun translateDescription(resStr: String): String {
        return translate(DESCRIPTION_PREFIX + resStr) ?: resStr
    }

    open fun translateName(resStr: String, vararg formatArgs: Any): String {
        return translate(NAME_PREFIX + resStr, formatArgs) ?: resStr
    }

    open fun translateName(resStr: String): String {
        return translate(NAME_PREFIX + resStr) ?: resStr
    }

    open fun translate(resStr: String, vararg formatArgs: Any): String? {
        val resId = getStringResource(resStr)
        return if (resId == 0) {
            Log.w("i18n", String.format("Unable to translate %s, missing resource string", resStr))
            null
        } else translate(resId, formatArgs)
    }

    open fun translate(resStr: String): String? {
        val resId = getStringResource(resStr)
        return if (resId == 0) {
            Log.w("i18n", String.format("Unable to translate %s, missing resource string", resStr))
            null
        } else translate(resId)
    }

    private fun getStringResource(aString: String): Int {
        return resources.getIdentifier(aString, "string", BuildConfig.APPLICATION_ID)
    }

    private fun translate(resId: Int): String {
        return resources.getString(resId)
    }

    private fun translate(resId: Int, vararg formatArgs: Any): String {
        return resources.getString(resId, formatArgs)
    }
}