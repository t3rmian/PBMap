package io.github.t3r1jj.pbmap.logging;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.Place;
import io.github.t3r1jj.pbmap.view.map.PlaceView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class MessageTest {

    private static final String RESERVED_JSON_CHARACTERS_STRING = "\b\f\n\r\t\"\\";
    private final String placeId = "json_place";
    private final long currentTimeMillis = System.currentTimeMillis();
    private Message message;

    @Before
    public void setUp() {
        String mapName = "json_map";
        message = new Message(mapName + " " + RESERVED_JSON_CHARACTERS_STRING,
                new Coordinate(0.1, 0.2),
                new Coordinate(0.3, 0.4),
                new Place() {
                    {
                        id = placeId + " " + RESERVED_JSON_CHARACTERS_STRING;
                        coordinates = Collections.singletonList(new Coordinate(0.5, 0.6));
                    }

                    @Override
                    public PlaceView createView(Context context) {
                        return null;
                    }
                });
        String id = "json_id";
        message.setId(id + " " + RESERVED_JSON_CHARACTERS_STRING);
        String description = "json_description";
        message.setDescription(description + " " + RESERVED_JSON_CHARACTERS_STRING);
        message.setEpochMs(currentTimeMillis);
    }

    @Test
    public void toJson_valid() throws JSONException {
        String json = message.toJson();
        new JSONObject(json);
    }

    @Test
    public void toJson_containsNotImportantFields() {
        String json = message.toJson();
        double ddRouteValue = new Coordinate(0.3, 0.4).distanceTo(new Coordinate(0.1, 0.2));
        assertThat(json, containsString("\"ddRoute\":" + ddRouteValue));
        double ddPlaceValue = new Coordinate(0.5, 0.6).distanceTo(new Coordinate(0.1, 0.2));
        assertThat(json, containsString("\"ddPlace\":" + ddPlaceValue));
    }

    @Test
    public void toJson_valid_minimalCase() throws JSONException {
        message = new Message(null, null, null, null);
        String json = message.toJson();
        new JSONObject(json);
    }
}