package io.github.t3r1jj.pbmap.view;

import android.content.Context;
import android.util.AttributeSet;

import com.qozix.tileview.TileView;

public class PBMapView extends TileView implements PlaceView{
    public PBMapView(Context context) {
        super(context);
    }

    public PBMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PBMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addPlaceView(PlaceView place) {
        place.addToMap(this);
    }

    //TODO: Fix broken SOLID principle (LSP)
    @Override
    public void addToMap(PBMapView pbMapView) {
        throw new UnsupportedOperationException("Cannot add map to map (literally), but maybe reference?");
    }
}
