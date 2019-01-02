package io.github.t3r1jj.pbmap.model

import android.content.Context
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

internal class TranslatorIT {

    @Test
    fun getLocale() {
    }

    @Test
    fun setLocale() {
    }

    @Test
    fun translateDescription() {
    }

    @Test
    fun translateDescription1() {
    }

    @Test
    fun translateName() {
    }

    @Test
    fun translateName1() {
    }

    @Test
    fun translate() {
        val context = mock(Context::class.java)
        `when`(context.getString(1)).thenReturn("mock")
        val translator = Translator(context)
        assertEquals("mock", translator.translate(1))
    }

    @Test
    fun translate1() {
    }

    @Test
    fun translate2() {
    }

    @Test
    fun translate3() {
    }
}