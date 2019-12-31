package io.github.t3r1jj.pbmap.model.dictionary

import android.content.Context
import io.github.t3r1jj.pbmap.BuildConfig
import io.github.t3r1jj.pbmap.model.i18n.Translator

/**
 * Implements settings dictionaries
 */
class Dictionary {

    /**
     * @return languages defined in [BuildConfig]
     */
    fun getLanguages(): List<String> {
        return BuildConfig.LANGUAGES
                .sorted()
    }

    /**
     * @return unit systems defined in [MeasurementSystem]
     */
    fun getUnitSystems(): List<String> {
        return MeasurementSystem.values()
                .map { it.toString() }
                .sorted()
    }

    /**
     * @return translated name for a unit system received from [getUnitSystems]
     */
    fun getI18nUnitSystem(context: Context, unitSystem: String): String {
        return Translator(context.resources)
                .translate("${MeasurementSystem::class.java.name}.$unitSystem")!!
    }

    /**
     * @return translated name for a language received from [getLanguages]
     */
    fun getI18nLanguage(context: Context, lang: String): String {
        return Translator(context.resources)
                .translate("LANGUAGES.$lang")!!
    }
}