package io.github.t3r1jj.pbmap.view;

class MapViewPosition {
    int centerX;
    int centerY;
    float zoom;

    MapViewPosition(int centerX, int centerY, float zoom) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.zoom = zoom;
    }

    @Override
    public String toString() {
        return "MapViewPosition{" +
                "centerX=" + centerX +
                ", centerY=" + centerY +
                ", zoom=" + zoom +
                '}';
    }
}
