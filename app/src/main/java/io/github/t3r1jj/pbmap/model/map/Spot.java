package io.github.t3r1jj.pbmap.model.map;

import android.content.Context;

import io.github.t3r1jj.pbmap.view.PlaceView;
import io.github.t3r1jj.pbmap.view.SpotView;

public class Spot extends Place {

    @Override
    public PlaceView createView(Context context) {
        return new SpotView(context, this);
    }
}
