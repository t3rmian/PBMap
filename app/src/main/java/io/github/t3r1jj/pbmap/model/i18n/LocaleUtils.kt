package io.github.t3r1jj.pbmap.model.i18n

import java.util.*

class LocaleUtils {
    companion object {
        fun toLocale(language_country: String): Locale {
            val lc = language_country.split("_")
            return if (lc.size > 1) {
                Locale(lc[0], lc[1])
            } else {
                Locale(lc[0])
            }
        }

        fun toString(locale: Locale): String {
            return locale.toString().replace("-", "_");
        }
    }
}