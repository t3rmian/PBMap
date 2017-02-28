package io.github.t3r1jj.pbmap.model.gps;

import android.content.Context;
import android.widget.ImageView;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.Coordinate;

public class Person {
    Coordinate coordinate;

    void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public ImageView getMarker(Context context) {
        ImageView marker = new ImageView(context);
        marker.setImageDrawable(context.getResources().getDrawable(R.drawable.person_marker));
        return marker;
    }
}
