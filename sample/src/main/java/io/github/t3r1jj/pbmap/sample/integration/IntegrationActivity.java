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

import android.content.ActivityNotFoundException;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class IntegrationActivity extends AppCompatActivity {

    private Location customLocation;
    private TextView searchQueryText;
    private TextView mapIdText;
    private TextView latText;
    private TextView lngText;
    private BottomSheetDialog dialog;
    PBMapIntegrator pbMapIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integration);
        customLocation = new Location("");
        searchQueryText = findViewById(R.id.search_query_text);
        mapIdText = findViewById(R.id.map_id_text);
        latText = findViewById(R.id.lat_text);
        lngText = findViewById(R.id.lng_text);
        dialog = new BottomSheetDialog(this);
        pbMapIntegrator = new PBMapIntegrator(this);
    }

    public void onDefinedPinpoint(View view) {
        try {
            pbMapIntegrator.startActivity(searchQueryText.getText().toString());
        } catch (ActivityNotFoundException e) { // PBMap not found and could not resolve intent for google play
            Toast.makeText(this, getString(R.string.could_not_open_android_market), Toast.LENGTH_SHORT).show();
        }
    }

    public void onCustomPinpoint(View view) {
        try {
            customLocation.setLatitude(Double.parseDouble(latText.getText().toString()));
            customLocation.setLongitude(Double.parseDouble(lngText.getText().toString()));
            try {
                pbMapIntegrator.startActivity(mapIdText.getText().toString(), customLocation);
            } catch (ActivityNotFoundException e) { // PBMap not found and could not resolve intent for google play
                Toast.makeText(this, getString(R.string.could_not_open_android_market), Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException ex) {
            Toast.makeText(this, getString(R.string.incorrect_location_format), Toast.LENGTH_SHORT).show();
        }
    }

    public void onShowPlaceIds(View view) {
        showPlaces("id", R.string.places_by_id);
    }

    public void onShowPlaceNames(View view) {
        showPlaces(null, R.string.places_by_name);
    }

    /**
     *
     * @param selection pass "id" to get id results, otherwise a translated list will be returned
     * @param titleResId just for displaying the data
     */
    private void showPlaces(String selection, @StringRes int titleResId) {
        Toast.makeText(this, getString(R.string.loading), Toast.LENGTH_LONG).show();
        new Thread(() -> {
            ContentProviderClient contentProvider = pbMapIntegrator.getContentProvider();
            try (Cursor cursor = contentProvider.query(Uri.parse(PBMapIntegrator.PBMAP_CONTENT_URI),
                    null, selection, new String[]{".*@.*"}, null)) {    // It's fine to use regex
                showPlaces(titleResId, cursor);
            } catch (RemoteException e) {
                runOnUiThread(this::openGooglePlay);
            }
        }).start();
    }

    public void showPlaces(@StringRes int titleResId, Cursor cursor) {
        View dialogView = getLayoutInflater().inflate(R.layout.places_dialog, findViewById(R.id.activity_integration), false);
        dialog.setContentView(dialogView);
        TextView placesTitle = dialogView.findViewById(R.id.places_title);
        placesTitle.setText(titleResId);
        TableLayout table = dialogView.findViewById(R.id.places);
        table.removeAllViews();
        fillTable(table, cursor);
        runOnUiThread(dialog::show);
    }

    private void fillTable(TableLayout table, Cursor cursor) {
        boolean initialized = false;
        while (cursor != null && cursor.moveToNext()) {
            if (!initialized) {
                TableRow row = createRow(cursor, true);
                table.addView(row);
                initialized = true;
            }
            TableRow row = createRow(cursor, false);
            table.addView(row);
        }
    }

    private TableRow createRow(Cursor cursor, boolean isHeader) {
        TableRow row = new TableRow(this);
        for (PBMapIntegrator.ContentMapping mapping : PBMapIntegrator.ContentMapping.values()) {
            TextView column = new TextView(this);
            if (isHeader) {
                column.setText(mapping.getColumnName());
                column.setTypeface(column.getTypeface(), Typeface.BOLD_ITALIC);
                column.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            } else {
                column.setText(cursor.getString(cursor.getColumnIndex(mapping.getColumnName())));
                column.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            }
            column.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
            row.addView(column);
        }
        return row;
    }

    private void openGooglePlay() {
        try {
            pbMapIntegrator.openGooglePlay();
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, getString(R.string.incorrect_location_format), Toast.LENGTH_SHORT).show();
        }
    }
}
