package io.github.t3r1jj.pbmap.model;

import android.content.Context;

import org.simpleframework.xml.Attribute;

import io.github.t3r1jj.pbmap.view.PlaceView;
import io.github.t3r1jj.pbmap.view.SpaceView;

public class Space extends Place {
    @Attribute(name = "reference_map_path", required = false)
    private String referenceMapPath;

    @Override
    public PlaceView createView(Context context) {
        return new SpaceView(context, this);
    }

    public String getReferenceMapPath() {
        return referenceMapPath;
    }
}
