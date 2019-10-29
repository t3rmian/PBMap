package io.github.t3r1jj.pbmap.model

import android.content.Context
import io.github.t3r1jj.pbmap.BuildConfig


class Dictionary {
    fun getLanguages(): Array<String> {
        return BuildConfig.LANGUAGES
    }

    fun getI18nLanguage(context: Context, lang: String): String {
        return Translator(context.resources)
                .translate("LANGUAGES.$lang")!!
    }
}