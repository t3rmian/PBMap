package io.github.t3r1jj.pbmap.model.map;

import android.os.Parcel;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CoordinateIT {

    @Test
    public void describeContents() {
        assertEquals(0, new Coordinate(1d, 2d, 3d).describeContents());
        assertEquals(0, new Coordinate(1d, 2d).describeContents());
    }

    @Test
    public void writeToParcel() {
        Coordinate coordinate = new Coordinate(1d, 2d, 3d);
        Parcel parcel = Parcel.obtain();
        coordinate.writeToParcel(parcel, coordinate.describeContents());
        parcel.setDataPosition(0);
        Coordinate coordinateFromParcel = Coordinate.CREATOR.createFromParcel(parcel);
        assertArrayEquals(new Coordinate[3], Coordinate.CREATOR.newArray(3));
        assertEquals(coordinate, coordinateFromParcel);
    }

    @Test
    public void writeToParcel_NoAltitude() {
        Coordinate coordinate = new Coordinate(1d, 2d);
        Parcel parcel = Parcel.obtain();
        coordinate.writeToParcel(parcel, coordinate.describeContents());
        parcel.setDataPosition(0);
        Coordinate coordinateFromParcel = Coordinate.CREATOR.createFromParcel(parcel);
        assertArrayEquals(new Coordinate[2], Coordinate.CREATOR.newArray(2));
        assertEquals(coordinate, coordinateFromParcel);
    }
}