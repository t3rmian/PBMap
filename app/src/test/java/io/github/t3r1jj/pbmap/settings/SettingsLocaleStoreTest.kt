package io.github.t3r1jj.pbmap.settings

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import java.util.*

class SettingsLocaleStoreTest {

    @Test
    fun getDefaultLocale() {
        val defaultLocale = mock<Locale>()
        val prefs = mock<SharedPreferences>()
        val editor = mock<SharedPreferences.Editor>()
        `when`(prefs.edit()).thenReturn(editor)
        `when`(editor.putString(any(), anyOrNull())).thenReturn(editor)
        val store = SettingsLocaleStore(mock(), defaultLocale, prefs)
        val locale = store.getLocale()
        assertEquals(defaultLocale, locale)
        verify(editor).apply()
    }
}