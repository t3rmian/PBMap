package io.github.t3r1jj.pbmap.model.i18n

import android.content.res.Resources
import android.util.Log
import io.github.t3r1jj.pbmap.BuildConfig
import java.util.Locale

/**
 * Translates prefixed and un-prefixed strings to a language defined in the [Resources]
 */
open class Translator(val resources: Resources) {
    companion object {

        /**
         * @param id resource name
         * @return pre-formatted resource name with underscores
         */
        @JvmStatic
        fun preFormat(id: String): String {
            return id.toLowerCase(Locale.ROOT)
                    .replace("/", "_")
                    .replace(" ", "_")
                    .replace("-", "_")
        }

        /**
         * @param text to format
         * @return trimmed text with newlines and line breaks replaced by a space
         */
        @JvmStatic
        fun postFormat(text: String): String {
            return text.replace("\n", " ")
                    .replace('_', ' ')
                    .trim()
        }

        /**
         * @param id name of the data string resource
         * @return name res id with special characters replaced by _ and with prepended prefix
         */
        @JvmStatic
        fun getResIdString(id: String, prefix: String): String {
            return prefix + preFormat(id)
        }

        const val NAME_PREFIX = "name_"
        const val ADDRESS_PREFIX = "address_"
        const val DESCRIPTION_PREFIX = "description_"
    }

    /**
     * @return translation of resource with [DESCRIPTION_PREFIX] name prefix and preFormat arguments
     * @param formatArgs see [Resources.getString]
     */
    open fun translateDescription(resStr: String, vararg formatArgs: Any): String {
        return translate(getResIdString(resStr, DESCRIPTION_PREFIX), formatArgs) ?: resStr
    }

    /**
     * @return translation of resource with [DESCRIPTION_PREFIX] name prefix
     */
    open fun translateDescription(resStr: String): String {
        return translate(getResIdString(resStr, DESCRIPTION_PREFIX)) ?: resStr
    }

    /**
     * @return translation of resource with [NAME_PREFIX] name prefix and preFormat arguments
     * @param formatArgs see [Resources.getString]
     */
    open fun translateName(resStr: String, vararg formatArgs: Any): String {
        return translate(getResIdString(resStr, NAME_PREFIX), formatArgs) ?: resStr
    }

    /**
     * @return translation of resource with [NAME_PREFIX] name prefix
     */
    open fun translateName(resStr: String): String {
        return translate(getResIdString(resStr, NAME_PREFIX)) ?: resStr
    }

    /**
     * @return translation of resource with given name and formatting
     * @param resStr resource name
     * @param formatArgs see [Resources.getString]
     */
    open fun translate(resStr: String, vararg formatArgs: Any): String? {
        val resId = getStringResource(resStr)
        return if (resId == 0) {
            Log.w("i18n", String.format("Unable to translate %s, missing resource string", resStr))
            null
        } else translate(resId, formatArgs)
    }

    /**
     * @return translation of resource with given name
     * @param resStr resource name
     */
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