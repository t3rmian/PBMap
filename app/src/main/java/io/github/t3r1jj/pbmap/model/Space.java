package io.github.t3r1jj.pbmap.model;

import android.content.Context;

import org.simpleframework.xml.Attribute;

import io.github.t3r1jj.pbmap.view.PlaceView;
import io.github.t3r1jj.pbmap.view.SpaceView;

public class Space extends Place {
    @Attribute(name = "map_reference", required = false)
    String mapReference;

    @Override
    public PlaceView createView(Context context) {
        return new SpaceView(context, this);
    }
}
