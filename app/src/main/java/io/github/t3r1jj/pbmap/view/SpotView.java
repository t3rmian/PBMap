package io.github.t3r1jj.pbmap.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;

import com.qozix.tileview.markers.MarkerLayout;

import io.github.t3r1jj.pbmap.BuildConfig;
import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.Place;

@SuppressLint("ViewConstructor")
public class SpotView extends MarkerLayout implements PlaceView {

    private final TextView textView;
    private final Coordinate center;

    public SpotView(Context context, Place place) {
        super(context);
        textView = new TextView(context);
        textView.setText(place.getName(context));
        Resources resources = context.getResources();
        textView.setTextColor(resources.getColor(R.color.spot_text));
        textView.setTextSize(resources.getDimension(R.dimen.spot_text));
        setTypeFace(context);
        center = place.getCenter();
    }

    private void setTypeFace(Context context) {
        final AssetManager assetManager = context.getAssets();
        final Typeface typeface = Typeface.createFromAsset(assetManager, BuildConfig.FONT_PATH);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        textView.setTypeface(typeface);
    }

    @Override
    public void addToMap(MapView pbMapView) {
        pbMapView.addMarker(textView, center.lng, center.lat, -0.5f, -1.0f);
    }

}
