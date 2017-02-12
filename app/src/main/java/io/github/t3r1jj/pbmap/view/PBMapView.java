package io.github.t3r1jj.pbmap.view;

import android.content.Context;

import com.qozix.tileview.TileView;

import io.github.t3r1jj.pbmap.Controller;
import io.github.t3r1jj.pbmap.model.Space;

public class PBMapView extends TileView implements PlaceView {
    Controller controller;

    public PBMapView(Context context) {
        super(context);
    }

    public void addPlaceView(PlaceView place) {
        place.addToMap(this);
    }

    //TODO: Fix broken SOLID principle (LSP)
    @Override
    public void addToMap(PBMapView pbMapView) {
        //throw new UnsupportedOperationException("Cannot add map to map (literally), but maybe reference?");
    }

    public void fireNavigationPerformed(Space space) {
        controller.onNavigationPerformed(space);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
