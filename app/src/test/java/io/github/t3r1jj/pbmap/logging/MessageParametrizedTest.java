package io.github.t3r1jj.pbmap.logging;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.Place;
import io.github.t3r1jj.pbmap.view.map.PlaceView;

@RunWith(Parameterized.class)
public class MessageParametrizedTest {

    private static final Place minimalValidPlace = new Place() {
        {
            id = "";
            coordinates = Collections.singletonList(new Coordinate());
        }

        @Override
        public PlaceView createView(Context context) {
            return null;
        }
    };

    @Parameterized.Parameter
    public String map;
    @Parameterized.Parameter(1)
    public Coordinate coordinate;
    @Parameterized.Parameter(2)
    public Coordinate closestRouteCoordinate;
    @Parameterized.Parameter(3)
    public Place place;

    @Parameterized.Parameters(name = "{index}: Test with map = {0}, coordinate = {1}, closestRouteCoordinate = {2}, place = {3}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {null, null, null, null},
                {"", null, null, null},
                {null, new Coordinate(), null, null},
                {null, null, new Coordinate(), null},
                {null, null, null, minimalValidPlace},
                {"", new Coordinate(), null, null},
                {"", null, new Coordinate(), null},
                {"", null, null, minimalValidPlace},
                {null, new Coordinate(), new Coordinate(), null},
                {null, new Coordinate(), null, minimalValidPlace},
                {null, null, new Coordinate(), minimalValidPlace},
                {null, new Coordinate(), new Coordinate(), minimalValidPlace},
                {"", new Coordinate(), new Coordinate(), null},
                {"", new Coordinate(), null, minimalValidPlace},
                {"", null, new Coordinate(), minimalValidPlace},
                {"", new Coordinate(), new Coordinate(), minimalValidPlace},
        };
        return Arrays.asList(data);
    }

    @Test
    public void toJson_valid_allPossibleCases() throws JSONException {
        Message message = new Message(map, coordinate, closestRouteCoordinate, place);
        String json = message.toJson();
        new JSONObject(json);
    }
}