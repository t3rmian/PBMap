package io.github.t3r1jj.pbmap.model

import io.github.t3r1jj.pbmap.BuildConfig


class Dictionary {
    fun getLanguages(): Array<String> {
        return BuildConfig.LANGUAGES
    }
}