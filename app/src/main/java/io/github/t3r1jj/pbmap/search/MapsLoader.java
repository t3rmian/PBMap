package io.github.t3r1jj.pbmap.search;

import android.content.Context;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import io.github.t3r1jj.pbmap.model.map.PBMap;
import io.github.t3r1jj.pbmap.model.map.route.RouteGraph;

public class MapsLoader extends MapsDao {
    private final Serializer serializer = new Persister();

    public MapsLoader(Context base) {
        super(base);
    }

    /**
     * @return default map
     */
    public PBMap loadMap() {
        return loadMap(mapsPath + "/" + firstMapFilename);
    }

    public PBMap loadMap(String assetsPath) {
        PBMap map = null;
        try {
            map = serializer.read(PBMap.class, openAsset(assetsPath));
            map.setReferenceMapPath(assetsPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public RouteGraph loadGraph(PBMap map) {
        RouteGraph routeGraph = null;
        try {
            routeGraph = serializer.read(RouteGraph.class, openAsset(map.getGraphPath()));
            routeGraph.setPath(map.getGraphPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routeGraph;
    }

}
