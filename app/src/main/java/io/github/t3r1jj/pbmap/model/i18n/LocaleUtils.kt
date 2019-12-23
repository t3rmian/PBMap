package io.github.t3r1jj.pbmap.model.i18n

import java.util.*

/**
 * Utils for conversion between [String] and [Locale]
 */
class LocaleUtils {
    companion object {

        /**
         * @return [Locale] from a [String] containing country or language_country
         */
        fun toLocale(language_country: String): Locale {
            val lc = language_country.split("_")
            return if (lc.size > 1) {
                Locale(lc[0], lc[1])
            } else {
                Locale(lc[0])
            }
        }

        /**
         * @return [String] of a [Locale] with underscore between language and country
         */
        fun toString(locale: Locale): String {
            return locale.toString().replace("-", "_");
        }
    }
}