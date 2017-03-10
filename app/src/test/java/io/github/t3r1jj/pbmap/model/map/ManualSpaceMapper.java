package io.github.t3r1jj.pbmap.model.map;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import static io.github.t3r1jj.pbmap.model.map.ManualSpaceMapper.DirectionHorizontal.EAST;
import static io.github.t3r1jj.pbmap.model.map.ManualSpaceMapper.DirectionHorizontal.WEST;
import static io.github.t3r1jj.pbmap.model.map.ManualSpaceMapper.StartingCoordinate.NORTH;
import static io.github.t3r1jj.pbmap.model.map.ManualSpaceMapper.StartingCoordinate.SOUTH;

/**
 * Provides test execution which creates space coordinates given by starting coordinate and other mostly static for map parameters
 */
public class ManualSpaceMapper {
    /**
     * based on location
     */
    private static final double LAT_TO_METER = 111288.42973516339d;
    /**
     * based on location
     */
    private static final double LNG_TO_METER = 66955.65028445533d;
    /**
     * based on wall geo angle
     */
    private static double tga = 1 / 2.84742;
    /**
     * starting coordinate
     */
    private Coordinate start;
    /**
     * horizontal direction in which space will be generated
     */
    private DirectionHorizontal horizontal;
    /**
     * starting point position
     */
    private StartingCoordinate starting;
    private double heightMeter;
    private double widthMeter;
    private double lat;
    private String id = "";
    private double[] widthsMeter;

    /**
     * use to calculate required width/height
     */
    private Coordinate end;

    @Before
    public void setUp() {
        starting = SOUTH;
        horizontal = EAST;
        heightMeter = 6;
        widthMeter = 47.8;
//        widthsMeter = new double[]{
//                6.18333333333/2,
//                6.18333333333/2,
//                6.18333333333,
//                6.18333333333,
//                6.18333333333,
//                6.18333333333*2,
//        };
        lat = -11;
        start = new Coordinate(23.145878893359374, 53.11655453350192);
//        end = new Coordinate(23.145788148125, 53.11670482763208);

        id = "___";

        if (end != null) {
            double dLat = start.lat - end.lat;
            double dLng = start.lng - end.lng;
            widthMeter = lngToMeter(Math.sqrt(dLat * dLat + dLng * dLng));
        }
    }

    @Test
    public void mapSpace() {
        if (widthsMeter != null) {
            for (int i = 0; i < widthsMeter.length; i++) {
                widthMeter = widthsMeter[i];
                id = String.valueOf(i);
                Coordinate[] coordinates = generateSpaceCoordinates();
                printSpaceXml(coordinates);
                start = coordinates[1];
            }
        } else {
            Coordinate[] coordinates = generateSpaceCoordinates();
            printSpaceXml(coordinates);
            printNextCoordinate(coordinates);
        }
    }

    @NonNull
    private Coordinate[] generateSpaceCoordinates() {
        Coordinate[] coordinates = new Coordinate[4];
        coordinates[0] = start;
        double dxw = metersToLng(dx(widthMeter));
        double dyw = metersToLat(dy(widthMeter));

        if (horizontal == EAST) {
            dxw = -dxw;
            dyw = -dyw;
        }

        coordinates[1] = new Coordinate(start.lng - dxw, start.lat - dyw);
        double tgaTemp = tga;
        tga = 1d / -tga;
        double dxh = metersToLng(dx(heightMeter));
        double dyh = metersToLat(dy(heightMeter));

        if (starting == NORTH) {
            dxh = -dxh;
            dyh = -dyh;
        }
        coordinates[3] = new Coordinate(start.lng - dxh, start.lat + dyh);
        coordinates[2] = new Coordinate(start.lng - dxw - dxh, start.lat - dyw + dyh);
        tga = tgaTemp;
        return coordinates;
    }


    private double dx(double c) {
        return Math.sqrt(c * c / (tga * tga + 1d));
    }

    private double dy(double c) {
        return Math.sqrt(c * c / (1d + 1d / (tga * tga)));
    }

    private double metersToLat(double meters) {
        return meters / LAT_TO_METER;
    }

    private double latToMeter(double lat) {
        return lat * LAT_TO_METER;
    }

    private double metersToLng(double meters) {
        return meters / LNG_TO_METER;
    }

    private double lngToMeter(double lng) {
        return lng * LNG_TO_METER;
    }

    private void printSpaceXml(Coordinate[] coordinates) {
        StringBuilder sb = new StringBuilder("<space id=\"")
                .append(id)
                .append("\">\n")
                .append("\t<coordinates>\n");
        for (Coordinate coordinate : coordinates) {
            sb.append("\t\t<coordinate ")
                    .append("lat=\"")
                    .append(coordinate.lat)
                    .append("\" lng=\"")
                    .append(coordinate.lng)
                    .append("\" alt=\"")
                    .append(lat)
                    .append("\"/>\n");
        }
        sb.append("\t</coordinates>\n")
                .append("</space>");
        System.out.println(sb.toString());
    }

    private void printNextCoordinate(Coordinate[] coordinates) {
        System.out.println("start = new Coordinate(" + coordinates[1].lng + ", " + coordinates[1].lat + ");");
    }

    enum DirectionHorizontal {
        EAST, WEST
    }

    enum StartingCoordinate {
        NORTH, SOUTH
    }
}
