package io.github.t3r1jj.pbmap.search;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.util.ObjectsCompat;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.i18n.Translator;
import io.github.t3r1jj.pbmap.model.map.Coordinate;

public class SearchSuggestion implements Comparable<SearchSuggestion> {
    private final String placeId;
    private final String mapPath;
    private String mapId;
    private Coordinate coordinate;
    private String logoName;
    private int rank;

    SearchSuggestion(@NotNull String placeId, @NotNull String mapPath) {
        this(placeId, mapPath, null);
    }

    SearchSuggestion(@NotNull String placeId, @NotNull String mapPath, @Nullable String mapId) {
        this.placeId = Objects.requireNonNull(placeId);
        this.mapPath = Objects.requireNonNull(mapPath);
        this.mapId = mapId;
    }

    /**
     * @param searchIntent with non-null data and extra mandatory non-null string with {@link SearchManager#EXTRA_DATA_KEY} key
     */
    public SearchSuggestion(@NotNull Intent searchIntent) {
        this.placeId = Objects.requireNonNull(searchIntent.getDataString());
        this.mapPath = Objects.requireNonNull(searchIntent.getStringExtra(SearchManager.EXTRA_DATA_KEY).split("@")[0]);
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

        return placeId.equals(that.placeId)
                && ObjectsCompat.equals(mapId, that.mapId)
                && ObjectsCompat.equals(coordinate, that.coordinate);
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(placeId, mapId, coordinate);
    }

    public String getName(Context context) {
        String name = new Translator(context.getResources()).translateName(placeId);
        return Translator.postFormat(name);
    }

    int getNameResId(Context context) {
        String packageName = context.getPackageName();
        return context.getResources().getIdentifier(Translator.getResIdString(placeId, Translator.NAME_PREFIX), "string", packageName);
    }

    String getMapName(Context context) {
        if (mapId == null) {
            return context.getString(R.string.map);
        }
        String name = new Translator(context.getResources()).translateName(mapId);
        return Translator.postFormat(name);
    }

    @SuppressLint("ResourceType")
    @DrawableRes
    public int getLogoId(Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(logoName, "drawable", packageName);
        if (resId == 0) {
            return android.R.drawable.ic_dialog_map;
        } else {
            return resId;
        }
    }

    void setLogoName(String logoName) {
        this.logoName = logoName;
    }

    void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public int compareTo(SearchSuggestion o) {
        int rankDiff = rank - o.rank;
        return rankDiff != 0 ? rankDiff : mapPath.compareTo(o.getMapPath());
    }
}
