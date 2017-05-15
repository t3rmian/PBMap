package io.github.t3r1jj.pbmap.model.map;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;

public class Coordinate implements Parcelable {
    /**
     * y coordinate [deg]
     */
    @Attribute
    public double lat;
    /**
     * x coordinate [deg]
     */
    @Attribute
    public double lng;
    /**
     * meters [m] above mean sea level
     * TODO: Not implemented yet, source/dest will be set based on current map
     */
    @Attribute(required = false)
    public double alt;

    private boolean altitude = true;

    public Coordinate() {
    }

    public Coordinate(double lat, double lng) {
        this.lng = lng;
        this.lat = lat;
    }

    public Coordinate(double lat, double lng, double alt) {
        this.lng = lng;
        this.lat = lat;
        this.alt = alt;
    }

    private Coordinate(Parcel source) {
        double[] coordinates = new double[3];
        source.readDoubleArray(coordinates);
        this.lat = coordinates[0];
        this.lng = coordinates[1];
        this.alt = coordinates[2];
        setAltitude(source.readByte() == 1);
    }

    private double[] getCoordinates() {
        return new double[]{lat, lng, alt};
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
        StringBuilder sb = new StringBuilder("<spot id=\"")
                .append("_")
                .append("\">")
                .append("\t<coordinates>");
        sb.append("\t\t<coordinate ")
                .append("lat=\"")
                .append(lat)
                .append("\" lng=\"")
                .append(lng)
                .append("\" alt=\"")
                .append(alt)
                .append("\"/>");
        sb.append("\t</coordinates>")
                .append("</spot>");
        return "Coordinate{" +
                "lng=" + lng +
                ", lat=" + lat +
                ", alt=" + alt +
                '}'
                + "\nstart = new Coordinate(" + lng + ", " + lat + ");\n" + sb.toString();
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

    /**
     * @param altitude not implemented yet
     */
    @Deprecated
    public void setAltitude(boolean altitude) {
        this.altitude = altitude;
        this.altitude = false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDoubleArray(getCoordinates());
        dest.writeByte((byte) (hasAltitude() ? 1 : 0));
    }

    public static final Parcelable.Creator<Coordinate> CREATOR = new Parcelable.Creator<Coordinate>() {

        @Override
        public Coordinate createFromParcel(Parcel source) {
            return new Coordinate(source);
        }

        @Override
        public Coordinate[] newArray(int size) {
            return new Coordinate[size];
        }

    };
}
