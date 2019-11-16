package io.github.t3r1jj.pbmap.model.i18n

import android.content.res.Resources
import android.util.Log
import io.github.t3r1jj.pbmap.BuildConfig

open class Translator(val resources: Resources) {
    companion object {
        const val DESCRIPTION_SUFFIX = "_description"
        const val NAME_SUFFIX = "_name"
    }

    open fun translateDescription(resStr: String, vararg formatArgs: Any): String {
        return translate(resStr + DESCRIPTION_SUFFIX, formatArgs) ?: resStr
    }

    open fun translateDescription(resStr: String): String {
        return translate(resStr + DESCRIPTION_SUFFIX) ?: resStr
    }

    open fun translateName(resStr: String, vararg formatArgs: Any): String {
        return translate(resStr + NAME_SUFFIX, formatArgs) ?: resStr
    }

    open fun translateName(resStr: String): String {
        return translate(resStr + NAME_SUFFIX) ?: resStr
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