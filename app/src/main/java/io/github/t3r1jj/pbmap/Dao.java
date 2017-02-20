package io.github.t3r1jj.pbmap;

import android.content.Context;
import android.content.ContextWrapper;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.ArrayList;
import java.util.List;

import io.github.t3r1jj.pbmap.model.PBMap;

//TODO: Standardize DAO, db or xml
public class Dao extends ContextWrapper {
    private final String mapsPath = "data";
    private final Serializer serializer = new Persister();

    public Dao(Context base) {
        super(base);
    }

    public List<PBMap> loadMaps() throws Exception {
        List<PBMap> maps = new ArrayList<>();
        for (String mapPath : getAssets().list(mapsPath)) {
            String assetsPath = mapsPath + "/" + mapPath;
            PBMap map = loadMap(assetsPath);
            maps.add(map);
        }
        return maps;
    }

    private PBMap loadMap(String assetsPath) throws Exception {
        PBMap map = serializer.read(PBMap.class, getAssets().open(assetsPath));
        map.setReferenceMapPath(assetsPath);
        return map;
    }

    public String getMapsPath() {
        return mapsPath;
    }
}
