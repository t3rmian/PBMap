package io.github.t3r1jj.pbmap.search

import android.content.Context
import android.content.ContextWrapper
import android.database.Cursor
import android.database.MatrixCursor
import io.github.t3r1jj.pbmap.BuildConfig
import java9.util.stream.Collectors
import java9.util.stream.IntStream
import java9.util.stream.Stream
import java9.util.stream.StreamSupport
import org.w3c.dom.Attr
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.InputStream
import java.util.LinkedList
import java.util.Locale
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

/**
 * Provides a way to query data files for a list of maps and places
 */
open class MapsDao(base: Context) : ContextWrapper(base) {
    companion object {
        @JvmStatic
        var CACHE: List<SearchSuggestion>? = null
        var CACHE_NO_MAPS: List<SearchSuggestion>? = null
        const val mapsPath = BuildConfig.ASSETS_MAP_DIR
        const val firstMapFilename = BuildConfig.FIRST_MAP_FILENAME
    }

    /**
     * @return a Cursor with columns matching definition of [io.github.t3r1jj.pbmap.search.SearchListProvider.tableColumns]
     *
     * @param columns column names for the cursor
     * @param selectionArgs an array of place and map or only a place
     * @param searchById if true - includes maps with map_id@map_id convention, if false the search is done on translated places
     */
    fun query(columns: Array<String>, selectionArgs: Array<String>, searchById: Boolean): Cursor {
        val results = MatrixCursor(columns)
        prepareQueryArguments(selectionArgs, searchById)
        streamQueryResults(selectionArgs, searchById)
                .sorted { o1, o2 ->
                    val byName = o1[1].toString().compareTo(o2[1].toString())
                    if (byName == 0) o1[2].toString().compareTo(o2[2].toString()) else byName
                }.forEachOrdered {
                    results.addRow(it)
                }
        return results
    }

    private fun prepareQueryArguments(selectionArgs: Array<String>, searchById: Boolean) {
        if (!searchById) {
            for (i in selectionArgs.indices) {
                selectionArgs[i] = ".*" + escape(selectionArgs[i]) + ".*"
            }
        }
    }

    private fun escape(selectionArg: String): String {
        return selectionArg.trim().replace("[", "\\[").replace("]", "\\]")
    }

    private fun streamQueryResults(selectionArgs: Array<String>, searchById: Boolean): Stream<Array<Any>> {
        return StreamSupport.stream(getSearchSuggestions(!searchById))
                .parallel()
                .map {
                    val name = if (searchById) it.placeId.toUpperCase(Locale.ROOT) else it.getName(baseContext).toUpperCase(Locale.ROOT)
                    val map = if (searchById) it.mapId.toUpperCase(Locale.ROOT) else it.getMapName(baseContext).toUpperCase(Locale.ROOT)
                    Triple(it, name, map)
                }
                .filter {
                    queryMatchesSuggestion(selectionArgs, it.second, it.third)
                }
                .map { triple ->
                    val it = triple.first
                    arrayOf(it.getNameResId(baseContext), triple.second, triple.third,
                            it.placeId, it.mapPath, it.placeId.toUpperCase(Locale.ROOT) + "@" + it.mapId.toUpperCase(Locale.ROOT))
                }
    }

    private fun queryMatchesSuggestion(selectionArgs: Array<String>, place: String, map: String): Boolean {
        val placeAndMap = arrayOf(place, map)
        for (i in selectionArgs.indices) {
            if (!placeAndMap[i].matches(selectionArgs[i].toRegex())) {
                return false
            }
        }
        return true
    }

    /**
     * @param ignoreMaps if false, a map_id@map_id entries are added
     * @return list of all queryable places defined in the database
     */
    fun getSearchSuggestions(ignoreMaps: Boolean): List<SearchSuggestion> {
        synchronized(this) {
            if (CACHE == null) {
                CACHE = loadSearchSuggestions()
                CACHE_NO_MAPS = CACHE!!.filter { it.mapId != it.placeId }
            }
        }
        return if (ignoreMaps) {
            CACHE_NO_MAPS!!
        } else {
            CACHE!!
        }
    }

    /**
     * @return search suggestions consisting solely of maps
     */
    fun getMapSuggestions(): List<SearchSuggestion> {
        val searchSuggestions = LinkedList<SearchSuggestion>()
        for (mapPath in getMapFilenames()) {
            val assetsPath = "$mapsPath/$mapPath"
            val factory = XPathFactory.newInstance()
            val xPath = factory.newXPath()
            val name = xPath.evaluate("(//*/@id)[1]", InputSource(openAsset(assetsPath)), XPathConstants.NODE) as Attr
            val element = name.ownerElement
            if ("true" != element.getAttribute("hidden")) {
                val searchSuggestion = SearchSuggestion(name.value, assetsPath)
                searchSuggestion.setLogoName(element.getAttribute("logo_path"))
                if (element.hasAttribute("rank")) {
                    searchSuggestion.setRank(Integer.parseInt(element.getAttribute("rank")))
                }
                searchSuggestions.add(searchSuggestion)
            }

        }
        return searchSuggestions
    }

    private fun loadSearchSuggestions(): List<SearchSuggestion> {
        return StreamSupport.stream(getMapFilenames())
                .parallel()
                .flatMap { mapPath ->
                    val assetsPath = "$mapsPath/$mapPath"
                    val factory = XPathFactory.newInstance()
                    val xPath = factory.newXPath()
                    val names = xPath.evaluate("//*/@id", InputSource(openAsset(assetsPath)), XPathConstants.NODESET) as NodeList
                    val mapId: String? = if (names.length > 0) names.item(0).nodeValue else null
                    IntStream.range(0, names.length)
                            .boxed()
                            .map {
                                SearchSuggestion(names.item(it).nodeValue, assetsPath, mapId)
                            }
                }
                .collect(Collectors.toList())
    }


    private fun getMapFilenames(): List<String> {
        return assets.list(mapsPath)!!.toMutableList()
    }

    fun openAsset(assetsPath: String): InputStream {
        return assets.open(assetsPath)
    }
}