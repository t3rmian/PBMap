package io.github.t3r1jj.pbmap;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import io.github.t3r1jj.pbmap.model.PBMap;
import io.github.t3r1jj.pbmap.view.PBMapView;

public class Controller extends ContextWrapper {
    PBMap map;

    public Controller(Context base, String assetsMapPath) {
        super(base);
        Serializer serializer = new Persister();
        try {
            map = serializer.read(PBMap.class, getAssets().open(assetsMapPath));
            Log.d("MainActivity", map.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public PBMapView loadMap() {
        return map.createView(this);
    }
}
