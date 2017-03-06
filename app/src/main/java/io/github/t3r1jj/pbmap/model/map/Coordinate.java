package io.github.t3r1jj.pbmap.model.map;

import org.simpleframework.xml.Attribute;

public class Coordinate {
    /**
     * x coordinate [deg]
     */
    @Attribute
    public double lng;
    /**
     * y coordinate [deg]
     */
    @Attribute
    public double lat;
    /**
     * meters [m] above mean sea level
     */
    @Attribute(required = false)
    public double alt;

    private boolean altitude = true;

    public Coordinate() {
    }

    public Coordinate(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public Coordinate(double lng, double lat, double alt) {
        this.lng = lng;
        this.lat = lat;
        this.alt = alt;
    }

    public double distance(Coordinate end) {
        double a = end.lng - lng;
        double b = end.lat - lat;
        double h = end.alt - alt;
        double base = Math.sqrt(a * a + b * b);
        return Math.sqrt(base * base + h * h);
    }

    /**
     * Use for extended (point not in defined route graph) routing, should not require teleporting through floors for considered buildings
     *
     * @param end second coordinate
     * @return adds multiplied penalty distance for excessive height difference (gt 1m)
     */
    public double flatDistance(Coordinate end) {
        double a = end.lng - lng;
        double b = end.lat - lat;
        double h = end.alt - alt;
        if (Math.abs(h) >= 1d) {
            h *= 1000d;
        }
        double base = Math.sqrt(a * a + b * b);
        return Math.sqrt(base * base + h * h);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "lng=" + lng +
                ", lat=" + lat +
                ", alt=" + alt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (Double.compare(that.lng, lng) != 0) return false;
        if (Double.compare(that.lat, lat) != 0) return false;
        return Double.compare(that.alt, alt) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lng);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(alt);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public boolean hasAltitude() {
        return altitude;
    }

    public void setAltitude(boolean altitude) {
        this.altitude = altitude;
    }
}
