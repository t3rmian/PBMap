package io.github.t3r1jj.pbmap.model.map;

/**
 * Represents max / min lng / lat
 */
public class BoundingBox {
    private double maxLng = Double.NEGATIVE_INFINITY;
    private double minLng = Double.POSITIVE_INFINITY;
    private double maxLat = Double.NEGATIVE_INFINITY;
    private double minLat = Double.POSITIVE_INFINITY;

    void add(Coordinate coordinate) {
        maxLng = Math.max(coordinate.lng, maxLng);
        minLng = Math.min(coordinate.lng, minLng);
        maxLat = Math.max(coordinate.lat, maxLat);
        minLat = Math.min(coordinate.lat, minLat);
    }

    /**
     * @return @see {@link Coordinate#lng}
     */
    public double getMaxLng() {
        return maxLng;
    }

    /**
     * @return @see {@link Coordinate#lng}
     */
    public double getMinLng() {
        return minLng;
    }


    /**
     * @return @see {@link Coordinate#lat}
     */
    public double getMaxLat() {
        return maxLat;
    }

    /**
     * @return @see {@link Coordinate#lat}
     */
    public double getMinLat() {
        return minLat;
    }

    @Override
    public String toString() {
        return "(" + minLat + "," + minLng + "," + maxLat + "," + maxLng + ")";
    }
}
