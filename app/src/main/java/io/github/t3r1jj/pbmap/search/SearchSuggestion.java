package io.github.t3r1jj.pbmap.search;

import android.app.SearchManager;
import android.content.Intent;

import io.github.t3r1jj.pbmap.model.Place;

public class SearchSuggestion {
    public String place;
    public String mapPath;

    public SearchSuggestion(String place, String mapPath) {
        this.place = place;
        this.mapPath = mapPath;
    }

    public SearchSuggestion(Intent searchIntent) {
        this.place = searchIntent.getDataString();
        this.mapPath = searchIntent.getStringExtra(SearchManager.EXTRA_DATA_KEY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchSuggestion that = (SearchSuggestion) o;

        return place.equals(that.place);

    }

    @Override
    public int hashCode() {
        return place.hashCode();
    }
}
