package io.github.t3r1jj.pbmap.search

import android.database.Cursor
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import io.github.t3r1jj.pbmap.search.SearchListProvider.tableColumns
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.lessThan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Assume.assumeThat
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.text.DecimalFormat

@RunWith(AndroidJUnit4::class)
class MapsDaoIT {

    companion object {
        private const val REPEAT_COUNT = 10
        private val DECIMAL_FORMAT = DecimalFormat("##.##")
    }

    @Test
    @SmallTest
    fun querySame() {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val oldMapsDao = OldMapsDao(targetContext)
        val optimizedMapsDao = MapsDao(targetContext)
        val oldResultsById = cursorToList(optimizedMapsDao.query(tableColumns, arrayOf(".*", ".*"), true))
        val optimizedResultsById = cursorToList(oldMapsDao.query(tableColumns, arrayOf(".*", ".*"), true))
        assertEquals(oldResultsById, optimizedResultsById)
        val oldResults = cursorToList(optimizedMapsDao.query(tableColumns, arrayOf(".+", ".+"), false))
        val optimizedResults = cursorToList(oldMapsDao.query(tableColumns, arrayOf(".+", ".+"), false))
        assertEquals(oldResults, optimizedResults)
        val oldResultsWA = cursorToList(optimizedMapsDao.query(tableColumns, arrayOf("WA"), false))
        val optimizedResultsWA = cursorToList(oldMapsDao.query(tableColumns, arrayOf("WA"), false))
        assertEquals(oldResultsWA, optimizedResultsWA)
    }

    private fun cursorToList(cursor: Cursor): List<List<String?>> {
        val rows = mutableListOf<List<String?>>()
        while (cursor.moveToNext()) {
            val columns = mutableListOf<String?>()
            for (i in 0 until cursor.columnCount) {
                try {
                    columns.add(cursor.getString(i))
                } catch (e: IllegalStateException) {
                    columns.add(null)
                }
            }
            rows.add(columns)
        }
        cursor.close()
        return rows
    }

    @Test
    @LargeTest
    fun queryWithoutCache() {
        assumeThat("Multiple processors", Runtime.getRuntime().availableProcessors(), greaterThan(1))
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val oldMapsDao = OldMapsDao(targetContext)
        val optimizedMapsDao = MapsDao(targetContext)
        var oldTime = 0L;
        var optimizedTime = 0L;
        for (i in 0 until REPEAT_COUNT) {
            optimizedTime += timeExecution(Runnable { optimizedMapsDao.query(tableColumns, arrayOf(".*@.*"), true) })
            MapsDao.CACHE_NO_MAPS = null
            MapsDao.CACHE = null
            oldTime += timeExecution(Runnable { oldMapsDao.query(tableColumns, arrayOf(".*@.*"), true) })
        }
        Log.d(MapsDaoIT::class.java.name, "query without caching $REPEAT_COUNT times - old impl: $oldTime")
        Log.d(MapsDaoIT::class.java.name, "query without caching $REPEAT_COUNT times - new impl: $optimizedTime")
        Log.i(MapsDaoIT::class.java.name, "query without caching $REPEAT_COUNT times - optimization coeff: " + DECIMAL_FORMAT.format(oldTime.toDouble() / optimizedTime))
        assertThat(optimizedTime, lessThan(oldTime))
    }

    @Test
    @LargeTest
    fun queryWithCache() {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val oldMapsDao = OldMapsDao(targetContext)
        val optimizedMapsDao = MapsDao(targetContext)
        var oldTime = 0L;
        var optimizedTime = 0L;
        for (i in 0 until REPEAT_COUNT) {
            optimizedTime += timeExecution(Runnable { optimizedMapsDao.query(tableColumns, arrayOf(".*@.*"), true) })
            oldTime += timeExecution(Runnable { oldMapsDao.query(tableColumns, arrayOf(".*@.*"), true) })
        }
        Log.d(MapsDaoIT::class.java.name, "query with caching $REPEAT_COUNT times - old impl: $oldTime")
        Log.d(MapsDaoIT::class.java.name, "query with caching $REPEAT_COUNT times - new impl: $optimizedTime")
        Log.i(MapsDaoIT::class.java.name, "query with caching $REPEAT_COUNT times - optimization coeff: " + DECIMAL_FORMAT.format(oldTime.toDouble() / optimizedTime))
        assertThat(optimizedTime, lessThan(oldTime))
    }

    private fun timeExecution(runnable: Runnable): Long {
        val start = System.currentTimeMillis()
        runnable.run()
        return System.currentTimeMillis() - start
    }
}