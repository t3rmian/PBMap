package io.github.t3r1jj.pbmap.search

import android.content.Context
import io.github.t3r1jj.pbmap.BuildConfig
import io.github.t3r1jj.pbmap.model.map.PBMap
import io.github.t3r1jj.pbmap.model.map.route.RouteGraph
import org.simpleframework.xml.core.Persister

/**
 * Loads map model from assets
 */
class MapsLoader(base: Context) : MapsDao(base) {

    companion object {
        const val firstMapFilename = BuildConfig.FIRST_MAP_FILENAME
    }

    private val serializer = Persister()

    /**
     * @return specific map from assets or a default one
     */
    @JvmOverloads
    fun loadMap(assetsPath: String = "$mapsPath/$firstMapFilename"): PBMap? {
        val map = serializer.read(PBMap::class.java, openAsset(assetsPath))
        map!!.referenceMapPath = assetsPath
        return map
    }

    /**
     * @return route graph for the map
     */
    fun loadGraph(map: PBMap): RouteGraph? {
        val routeGraph = serializer.read(RouteGraph::class.java, openAsset(map.graphPath))
        routeGraph!!.path = map.graphPath
        return routeGraph
    }

}