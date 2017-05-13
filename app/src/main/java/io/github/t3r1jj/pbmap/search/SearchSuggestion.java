package io.github.t3r1jj.pbmap.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.Place;

public class SearchSuggestion {
    public final String placeId;
    public final String mapPath;
    private String mapId;

    SearchSuggestion(String placeId, String mapPath) {
        this.placeId = placeId;
        this.mapPath = mapPath;
    }

    public SearchSuggestion(Intent searchIntent) {
        this.placeId = searchIntent.getDataString();
        this.mapPath = searchIntent.getStringExtra(SearchManager.EXTRA_DATA_KEY);
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchSuggestion that = (SearchSuggestion) o;

        return placeId.equals(that.placeId);

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
