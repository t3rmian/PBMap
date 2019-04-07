package io.github.t3r1jj.pbmap.logging;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.stream.DoubleStream;

import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.Place;
import io.github.t3r1jj.pbmap.view.map.PlaceView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class MessageTest {

    private static final String RESERVED_JSON_CHARACTERS_STRING = "\b\f\n\r\t\"\\";
    private final String mapName = "json_map";
    private final String placeId = "json_place";
    private final String id = "json_id";
    private final String description = "json_description";
    private final long currentTimeMillis = System.currentTimeMillis();
    private Message message;

    @Before
    public void setUp() {
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
        message.setId(id + " " + RESERVED_JSON_CHARACTERS_STRING);
        message.setDescription(description + " " + RESERVED_JSON_CHARACTERS_STRING);
        message.setEpochMs(currentTimeMillis);
    }

    @Test
    public void toJson_valid() throws JSONException {
        String json = message.toJson();
        new JSONObject(json);
    }

    @Test
    public void toJson_containsImportantFields() {
        String json = message.toJson();
        assertThat(json, containsString(mapName));
        assertThat(json, containsString(placeId));
        assertThat(json, containsString(id));
        assertThat(json, containsString(description));
        assertThat(json, containsString(String.valueOf(currentTimeMillis)));
        DoubleStream.of(0.1, 0.2, 0.3, 0.4, 0.5, 0.6)
                .forEach(coordinate -> assertThat(json, containsString(String.valueOf(coordinate))));
    }

    @Test
    public void toJson_containsNotImportantFields() {
        String json = message.toJson();
        double ddRouteValue = new Coordinate(0.3, 0.4).distanceTo(new Coordinate(0.1, 0.2));
        assertThat(json, containsString("\"ddRoute\":" + String.valueOf(ddRouteValue)));
        double ddPlaceValue = new Coordinate(0.5, 0.6).distanceTo(new Coordinate(0.1, 0.2));
        assertThat(json, containsString("\"ddPlace\":" + String.valueOf(ddPlaceValue)));
    }

    @Test
    public void toJson_valid_minimalCase() throws JSONException {
        message = new Message(null, null, null, null);
        String json = message.toJson();
        new JSONObject(json);
    }
}