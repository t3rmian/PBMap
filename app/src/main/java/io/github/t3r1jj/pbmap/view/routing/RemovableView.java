package io.github.t3r1jj.pbmap.view.routing;

import io.github.t3r1jj.pbmap.view.MapView;
import io.github.t3r1jj.pbmap.view.PlaceView;

public interface RemovableView extends PlaceView {
    void removeFromMap(MapView pbMapView);
}
