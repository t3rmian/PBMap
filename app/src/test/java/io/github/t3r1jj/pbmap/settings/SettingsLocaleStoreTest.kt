package io.github.t3r1jj.pbmap.settings

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

@RunWith(PowerMockRunner::class)
@PrepareForTest(value = [Locale::class])
class SettingsLocaleStoreTest {

    @Test
    fun getDefaultLocale() {
        val defaultLocale = PowerMockito.mock(Locale::class.java)
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