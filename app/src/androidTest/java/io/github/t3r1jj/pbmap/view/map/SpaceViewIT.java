package io.github.t3r1jj.pbmap.view.map;

import android.content.Context;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.qozix.tileview.geom.CoordinateTranslater;
import com.qozix.tileview.paths.CompositePathView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.lang.reflect.Field;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.Space;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class SpaceViewIT {

    @SmallTest
    @Test
    public void testColor_Off() throws NoSuchFieldException, IllegalAccessException {
        Space space = mock(Space.class);
        when(space.getId()).thenReturn("");
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Paint paint = getPaint(space, targetContext);
        assertEquals(ContextCompat.getColor(targetContext, R.color.space_bounds_off), paint.getColor());
    }

    @SmallTest
    @Test
    public void testColor_Selectable() throws NoSuchFieldException, IllegalAccessException {
        Space space = mock(Space.class);
        when(space.getId()).thenReturn("a");
        when(space.getReferenceMapPath()).thenReturn("A");
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Paint paint = getPaint(space, targetContext);
        assertEquals(ContextCompat.getColor(targetContext, R.color.space_bounds_interactive), paint.getColor());
    }

    @SmallTest
    @Test
    public void testColor_NonSelectable() throws NoSuchFieldException, IllegalAccessException {
        Space space = mock(Space.class);
        when(space.getId()).thenReturn("a");
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Paint paint = getPaint(space, targetContext);
        assertEquals(ContextCompat.getColor(targetContext, R.color.space_bounds), paint.getColor());
    }

    private Paint getPaint(Space space, Context targetContext) throws NoSuchFieldException, IllegalAccessException {
        SpaceView spaceView = new SpaceView(targetContext, space);
        Field field = CompositePathView.DrawablePath.class.getDeclaredField("paint");
        field.setAccessible(true);
        return (Paint) field.get(spaceView);
    }

    @SmallTest
    @Test
    public void testAddLogoToMap() throws Exception {
        String data = "    <space id=\"PB/WI\" reference_map_path=\"data/pb_wi.xml\" url=\"https://wi.pb.edu.pl\" logo_path=\"pb_acs\">\n" +
                "        <coordinates>\n" +
                "            <coordinate alt=\"150\" lat=\"1\" lng=\"2\" />\n" +
                "        </coordinates>\n" +
                "    </space>";
        Serializer serializer = new Persister();
        Space space = serializer.read(Space.class, data);
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SpaceView spaceView = new SpaceView(targetContext, space);

        MapView mapMock = mock(MapView.class);
        when(mapMock.getCoordinateTranslater()).thenReturn(new CoordinateTranslater());
        when(mapMock.getCompositePathView()).thenReturn(new CompositePathView(InstrumentationRegistry.getInstrumentation().getTargetContext()));

        spaceView.addToMap(mapMock);
        verify(mapMock).addMarker(notNull(), eq(2d), eq(1d), anyFloat(), anyFloat());
    }
}