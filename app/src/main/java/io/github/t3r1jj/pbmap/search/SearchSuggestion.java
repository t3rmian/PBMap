package io.github.t3r1jj.pbmap.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

import io.github.t3r1jj.pbmap.model.map.Place;

public class SearchSuggestion {
    public String placeId;
    public String mapPath;

    SearchSuggestion(String placeId, String mapPath) {
        this.placeId = placeId;
        this.mapPath = mapPath;
    }

    public SearchSuggestion(Intent searchIntent) {
        this.placeId = searchIntent.getDataString();
        this.mapPath = searchIntent.getStringExtra(SearchManager.EXTRA_DATA_KEY);
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
            return placeId;
        }
        return translatedName;
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
}
