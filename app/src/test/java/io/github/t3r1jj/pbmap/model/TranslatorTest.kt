package io.github.t3r1jj.pbmap.model

import android.content.res.Resources
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner.Silent::class)
internal class TranslatorTest {

    @Mock
    lateinit var resources: Resources

    @Test
    fun translateName() {
        val resourceId = 123
        val resourceName = "resourceName"
        val resourceValue = "resourceValue"
        `when`(resources.getIdentifier(eq(resourceName + "_name"), eq("string"), any())).thenReturn(resourceId)
        `when`(resources.getString(resourceId)).thenReturn(resourceValue)
        val translator = Translator(resources)
        assertEquals(resourceValue, translator.translateName(resourceName))
    }

    @Test
    fun translateName_notFound() {
        val resourceName = "resourceName"
        val translator = Translator(resources)
        assertEquals(resourceName, translator.translateName(resourceName))
    }

    @Test
    fun translateName_notFoundBySuffix() {
        val resourceId = 123
        val resourceName = "resourceName"
        val resourceValue = "resourceValue"
        `when`(resources.getIdentifier(eq(resourceName), eq("string"), any())).thenReturn(resourceId)
        `when`(resources.getString(resourceId)).thenReturn(resourceValue)
        val translator = Translator(resources)
        assertEquals(resourceName, translator.translateName(resourceName))
    }

    @Test
    fun translateDescription() {
        val resourceId = 123
        val resourceName = "resourceName"
        val resourceValue = "resourceValue"
        `when`(resources.getIdentifier(eq(resourceName + "_description"), eq("string"), any())).thenReturn(resourceId)
        `when`(resources.getString(resourceId)).thenReturn(resourceValue)
        val translator = Translator(resources)
        assertEquals(resourceValue, translator.translateDescription(resourceName))
    }

    @Test
    fun translateDescription_notFound() {
        val resourceName = "resourceName"
        val translator = Translator(resources)
        assertEquals(resourceName, translator.translateDescription(resourceName))
    }

    @Test
    fun translateDescription_notFoundBySuffix() {
        val resourceId = 123
        val resourceName = "resourceName"
        val resourceValue = "resourceValue"
        `when`(resources.getIdentifier(eq(resourceName), eq("string"), any())).thenReturn(resourceId)
        `when`(resources.getString(resourceId)).thenReturn(resourceValue)
        val translator = Translator(resources)
        assertEquals(resourceName, translator.translateDescription(resourceName))
    }

    @Test
    fun translateNameWithArgs() {
        val resourceId = 123
        val resourceName = "resourceName"
        val resourceValue = "resourceValue"
        val args = arrayOf("arg1", "arg2")
        `when`(resources.getIdentifier(eq(resourceName + "_name"), eq("string"), any())).thenReturn(resourceId)
        `when`(resources.getString(eq(resourceId), any())).thenReturn(resourceValue)
        val translator = spy(Translator(resources))
        assertEquals(resourceValue, translator.translateName(resourceName, *args))
        argumentCaptor<String>().apply {
            verify(translator).translateName(eq(resourceName), capture())
            assertEquals(listOf(*args), allValues)
        }
    }

    @Test
    fun translateNameWithArgs_notFound() {
        val resourceName = "resourceName"
        val translator = spy(Translator(resources))
        val args = arrayOf("arg1", "arg2")
        assertEquals(resourceName, translator.translateName(resourceName, *args))
        argumentCaptor<String>().apply {
            verify(translator).translateName(eq(resourceName), capture())
            assertEquals(listOf(*args), allValues)
        }
    }

    @Test
    fun translateNameWithArgs_notFoundBySuffix() {
        val resourceId = 123
        val resourceName = "resourceName"
        val resourceValue = "resourceValue"
        `when`(resources.getIdentifier(eq(resourceName), eq("string"), any())).thenReturn(resourceId)
        `when`(resources.getString(resourceId)).thenReturn(resourceValue)
        val translator = spy(Translator(resources))
        val args = arrayOf("arg1", "arg2")
        assertEquals(resourceName, translator.translateName(resourceName, *args))
        argumentCaptor<String>().apply {
            verify(translator).translateName(eq(resourceName), capture())
            assertEquals(listOf(*args), allValues)
        }
    }

    @Test
    fun translateDescriptionWithArgs() {
        val resourceId = 123
        val resourceName = "resourceName"
        val resourceValue = "resourceValue"
        `when`(resources.getIdentifier(eq(resourceName + "_description"), eq("string"), any())).thenReturn(resourceId)
        `when`(resources.getString(eq(resourceId), any())).thenReturn(resourceValue)
        val translator = spy(Translator(resources))
        val args = arrayOf("arg1", "arg2")
        assertEquals(resourceValue, translator.translateDescription(resourceName, *args))
        argumentCaptor<String>().apply {
            verify(translator).translateDescription(eq(resourceName), capture())
            assertEquals(listOf(*args), allValues)
        }
    }

    @Test
    fun translateDescriptionWithArgs_notFound() {
        val resourceName = "resourceName"
        val translator = spy(Translator(resources))
        val args = arrayOf("arg1", "arg2")
        assertEquals(resourceName, translator.translateDescription(resourceName, *args))
        argumentCaptor<String>().apply {
            verify(translator).translateDescription(eq(resourceName), capture())
            assertEquals(listOf(*args), allValues)
        }
    }

    @Test
    fun translateDescriptionWithArgs_notFoundBySuffix() {
        val resourceId = 123
        val resourceName = "resourceName"
        val resourceValue = "resourceValue"
        val args = arrayOf("arg1", "arg2")
        `when`(resources.getIdentifier(eq(resourceName), eq("string"), any())).thenReturn(resourceId)
        `when`(resources.getString(resourceId)).thenReturn(resourceValue)
        val translator = spy(Translator(resources))
        assertEquals(resourceName, translator.translateDescription(resourceName, *args))
        argumentCaptor<String>().apply {
            verify(translator).translateDescription(eq(resourceName), capture())
            assertEquals(listOf(*args), allValues)
        }
    }

    @Test
    fun translate() {
        val resourceId = 123
        val resourceName = "resourceName"
        val resourceValue = "resourceValue"
        `when`(resources.getIdentifier(eq(resourceName), eq("string"), any())).thenReturn(resourceId)
        `when`(resources.getString(resourceId)).thenReturn(resourceValue)
        val translator = Translator(resources)
        assertEquals(resourceValue, translator.translate(resourceName))
    }

    @Test
    fun translateWithArgs() {
        val resourceId = 123
        val resourceName = "resourceName"
        val resourceValue = "resourceValue"
        `when`(resources.getIdentifier(eq(resourceName), eq("string"), any())).thenReturn(resourceId)
        `when`(resources.getString(eq(resourceId), any())).thenReturn(resourceValue)
        val translator = spy(Translator(resources))
        val args = arrayOf("arg1", "arg2")
        assertEquals(resourceValue, translator.translate(resourceName, *args))
        argumentCaptor<String>().apply {
            verify(translator).translate(eq(resourceName), capture())
            assertEquals(listOf(*args), allValues)
        }
    }

    @Test
    fun translate_null() {
        val translator = Translator(resources)
        assertNull(translator.translate("unknown", "arg"))
    }

    @Test
    fun translateWithArgs_null() {
        val resourceName = "unknown"
        val translator = spy(Translator(resources))
        val args = arrayOf("arg1", "arg2")
        assertNull(translator.translate(resourceName, *args))
        argumentCaptor<String>().apply {
            verify(translator).translate(eq(resourceName), capture())
            assertEquals(listOf(*args), allValues)
        }
    }

}