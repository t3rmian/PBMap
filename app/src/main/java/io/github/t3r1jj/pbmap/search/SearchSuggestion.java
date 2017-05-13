package io.github.t3r1jj.pbmap.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.Place;

public class SearchSuggestion {
    private final String placeId;
    private final String mapPath;
    private String mapId;
    private Coordinate coordinate;

    SearchSuggestion(String placeId, String mapPath) {
        this.placeId = placeId;
        this.mapPath = mapPath;
    }

    public SearchSuggestion(Intent searchIntent) {
        this.placeId = searchIntent.getDataString();
        this.mapPath = searchIntent.getStringExtra(SearchManager.EXTRA_DATA_KEY);
    }

    String getMapId() {
        return mapId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getMapPath() {
        return mapPath;
    }

    void setMapId(String mapId) {
        this.mapId = mapId;
    }

    @Nullable
    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setLocationCoordinate(Location location) {
        this.coordinate = new Coordinate(location.getLatitude(), location.getLongitude(), location.getAltitude());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchSuggestion that = (SearchSuggestion) o;

        return placeId.equals(that.placeId) && mapId.equals(that.mapId);

    }

    @Override
    public int hashCode() {
        return placeId.hashCode();
    }

    public String getName(Context context) {
        String translatedName = getNameRes(context);
        if (translatedName == null) {
            return placeId.toUpperCase().replace('_', ' ');
        }
        return translatedName.replace("\n", " ");
    }

    private String getNameRes(Context context) {
        int resId = getNameResId(context);
        if (resId == 0) {
            return null;
        }
        return context.getString(resId);
    }

    int getNameResId(Context context) {
        String packageName = context.getPackageName();
        return context.getResources().getIdentifier(Place.getNameResIdString(placeId), "string", packageName);
    }

    String getMapName(Context context) {
        if (mapId == null) {
            return context.getString(R.string.map);
        }
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(Place.getNameResIdString(mapId), "string", packageName);
        if (resId == 0) {
            return mapId.toUpperCase().replace('_', ' ');
        }
        return context.getString(resId);
    }
}
