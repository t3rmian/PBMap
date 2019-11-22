/*
 * Copyright 2017 Damian Terlecki.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.t3r1jj.pbmap.sample.integration;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;

/**
 * This is a sample class that provides integration with PBMap by sending a search intent (starting PBMap)
 */
class PBMapIntegrator extends ContextWrapper {

    private static final String PBMAP_PACKAGE_NAME = "io.github.t3r1jj.pbmap";
    private static final String PBMAP_CLASS_NAME = "io.github.t3r1jj.pbmap.main.MapActivity";
    private static final String PBMAP_CONTENT_PROVIDER_URI = "content://io.github.t3r1jj.pbmap.search.SearchListProvider";
    static final String PBMAP_CONTENT_URI = PBMAP_CONTENT_PROVIDER_URI + "/suggestions";

    /**
     * There are in total 5 returned columns at the moment, two most important ones are place and map
     */
    enum ContentMapping {
        PLACE_COLUMN(SearchManager.SUGGEST_COLUMN_TEXT_1),
        MAP_COLUMN(SearchManager.SUGGEST_COLUMN_TEXT_2);

        private final String columnName;

        ContentMapping(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }
    }

    public PBMapIntegrator(Context base) {
        super(base);
    }

    /**
     * Starts PBMap with passed searchQuery. If the activity is not found Google Play will be opened provide user with a possibility to download PBMap.
     * If the device does not have Google Play an ActivityNotFound exception will be thrown.
     *
     * @param searchQuery a place in the following syntax: place_id@map_id, refer to the documentation for ids of places and maps
     */
    public void startActivity(String searchQuery) {
        Intent sendIntent = new Intent();
        sendIntent.putExtra(SearchManager.QUERY, searchQuery);

        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.setClassName(PBMAP_PACKAGE_NAME, PBMAP_CLASS_NAME);
        try {
            startActivity(sendIntent);
        } catch (ActivityNotFoundException notFoundException) {
            openGooglePlay();
        }
    }

    /**
     * Starts PBMap with passed searchQuery. If the activity is not found Google Play will be opened provide user with a possibility to download PBMap.
     * If the device does not have Google Play an ActivityNotFound exception will be thrown.
     *
     * @param mapId    map_id, refer to the documentation for ids of maps
     * @param location custom location to pinpoint with a marker on passed map
     */
    public void startActivity(String mapId, Location location) {
        Intent sendIntent = new Intent();
        sendIntent.putExtra(SearchManager.QUERY, mapId);
        sendIntent.putExtra(SearchManager.EXTRA_DATA_KEY, location);

        sendIntent.setAction(Intent.ACTION_SEARCH);
        sendIntent.setClassName(PBMAP_PACKAGE_NAME, PBMAP_CLASS_NAME);
        try {
            startActivity(sendIntent);
        } catch (ActivityNotFoundException notFoundException) {
            openGooglePlay();
        }
    }

    void openGooglePlay() {
        Intent toAppDistribution = new Intent(Intent.ACTION_VIEW);
        try {
            Uri marketUri = Uri.parse("market://details?id=" + PBMAP_PACKAGE_NAME);
            toAppDistribution.setData(marketUri);
            startActivity(toAppDistribution);
        } catch (ActivityNotFoundException e) {
            Uri googlePlayUri = Uri.parse("https://play.google.com/store/apps/details?id=" + PBMAP_PACKAGE_NAME);
            toAppDistribution.setData(googlePlayUri);
            startActivity(toAppDistribution);
        }
    }

    /**
     * @return read-only content provider for querying maps and places
     */
    public ContentProviderClient getContentProvider() {
        Uri uri = Uri.parse(PBMAP_CONTENT_PROVIDER_URI);
        return getContentResolver().acquireContentProviderClient(uri);
    }
}
