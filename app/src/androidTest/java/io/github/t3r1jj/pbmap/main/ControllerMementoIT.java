package io.github.t3r1jj.pbmap.main;

import android.os.Parcel;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import io.github.t3r1jj.pbmap.model.map.Coordinate;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ControllerMementoIT {

    @Test
    @SmallTest
    public void testCreator() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException, NoSuchFieldException {
        Coordinate source = new Coordinate();
        Coordinate destination = new Coordinate(1, 2, 3);
        Constructor<Controller.Memento> constructor =
                Controller.Memento.class.getDeclaredConstructor(Coordinate.class, Coordinate.class, String.class);
        constructor.setAccessible(true);
        Controller.Memento memento = constructor.newInstance(source, destination, "map");
        Parcel parcel = Parcel.obtain();
        memento.writeToParcel(parcel, memento.describeContents());
        parcel.setDataPosition(0);

        Controller.Memento mementoFromParcel = Controller.Memento.CREATOR.createFromParcel(parcel);

        String comparisonField = "source";
        assertEquals(getMementoValue(memento, comparisonField), getMementoValue(mementoFromParcel, comparisonField));
        comparisonField = "destination";
        assertEquals(getMementoValue(memento, comparisonField), getMementoValue(mementoFromParcel, comparisonField));
        comparisonField = "mapReferencePath";
        assertEquals(getMementoValue(memento, comparisonField), getMementoValue(mementoFromParcel, comparisonField));
    }

    @Test
    @SmallTest
    public void testCreator_Empty() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException, NoSuchFieldException {
        Constructor<Controller.Memento> constructor =
                Controller.Memento.class.getDeclaredConstructor(Coordinate.class, Coordinate.class, String.class);
        constructor.setAccessible(true);
        Controller.Memento memento = constructor.newInstance(null, null, null);
        Parcel parcel = Parcel.obtain();
        memento.writeToParcel(parcel, memento.describeContents());
        parcel.setDataPosition(0);

        Controller.Memento mementoFromParcel = Controller.Memento.CREATOR.createFromParcel(parcel);

        String comparisonField = "source";
        assertEquals(getMementoValue(memento, comparisonField), getMementoValue(mementoFromParcel, comparisonField));
        comparisonField = "destination";
        assertEquals(getMementoValue(memento, comparisonField), getMementoValue(mementoFromParcel, comparisonField));
        comparisonField = "mapReferencePath";
        assertEquals(getMementoValue(memento, comparisonField), getMementoValue(mementoFromParcel, comparisonField));
    }

    public Object getMementoValue(Controller.Memento memento, String field) throws NoSuchFieldException, IllegalAccessException {
        Field sourceField = memento.getClass().getDeclaredField(field);
        sourceField.setAccessible(true);
        return sourceField.get(memento);
    }

}