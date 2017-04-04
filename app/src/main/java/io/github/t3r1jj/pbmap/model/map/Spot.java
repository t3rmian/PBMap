package io.github.t3r1jj.pbmap.model.map;

import android.content.Context;

import io.github.t3r1jj.pbmap.view.map.PlaceView;
import io.github.t3r1jj.pbmap.view.map.SpotView;

public class Spot extends Place {

    @Override
    public PlaceView createView(Context context) {
        return hidden ? null : new SpotView(context, this);
    }
}
