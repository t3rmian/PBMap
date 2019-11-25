package io.github.t3r1jj.pbmap.search

import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class WebUriParserIT(private val input: String, private val output: String?) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {0} -> {1}")
        fun initParameters(): List<Array<out String?>> {
            return listOf(
                    arrayOf("https://pbmap.termian.dev/mobile/pb_campus", "pb_campus"),
                    arrayOf("https://pbmap.termian.dev/mobile/pb_wi/12b", "12b@pb_wi"),
                    arrayOf("https://pbmap.termian.dev/mobile/pb_wi/12b/incorrect", "12b@pb_wi"),
                    arrayOf("https://pbmap.termian.dev/mobile/pb_campus?some=parameter", "pb_campus"),
                    arrayOf("https://pbmap.termian.dev/mobile/pb_wi/12b?another=parameter", "12b@pb_wi"),
                    arrayOf("https://pbmap.termian.dev/mobile/", null),
                    arrayOf("https://pbmap.termian.dev/", null),
                    arrayOf("https://example.com/", null),
                    arrayOf("https://example.com/mobile/pb_campus", "pb_campus"),
                    arrayOf("https://example.com/mobile/pb_wi/12b", "12b@pb_wi"),
                    arrayOf("https://example.com/something/mobile/pb_wi/12b", "12b@pb_wi"),
                    arrayOf("/something/mobile/pb_wi/12b", "12b@pb_wi")
            )
        }
    }

    @Test
    fun testPlaceUri() {
        val result = WebUriParser.parseIntoCommonFormat(Uri.parse(input))
        assertEquals(output, result)
    }
}