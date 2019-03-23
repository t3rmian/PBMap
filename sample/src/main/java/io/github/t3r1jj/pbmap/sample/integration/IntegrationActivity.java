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
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class IntegrationActivity extends AppCompatActivity {

    private Location customLocation;
    private TextView searchQueryText;
    private TextView mapIdText;
    private TextView latText;
    private TextView lngText;
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

}
