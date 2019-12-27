package io.github.t3r1jj.pbmap.view.map.routing;

import io.github.t3r1jj.pbmap.view.map.MapView;
import io.github.t3r1jj.pbmap.view.map.PlaceView;

interface RemovableView extends PlaceView {
    void removeFromMap(MapView pbMapView);
}
