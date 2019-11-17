package io.github.t3r1jj.pbmap.model.dictionary

import android.content.Context
import io.github.t3r1jj.pbmap.BuildConfig
import io.github.t3r1jj.pbmap.model.i18n.Translator


class Dictionary {

    fun getLanguages(): List<String> {
        return BuildConfig.LANGUAGES
                .sorted()
    }

    fun getUnitSystems(): List<String> {
        return MeasurementSystem.values()
                .map { it.toString() }
                .sorted()
    }

    fun getI18nUnitSystem(context: Context, unitSystem: String): String {
        return Translator(context.resources)
                .translate("${MeasurementSystem::class.java.name}.$unitSystem")!!
    }

    fun getI18nLanguage(context: Context, lang: String): String {
        return Translator(context.resources)
                .translate("LANGUAGES.$lang")!!
    }
}