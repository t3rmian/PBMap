package io.github.t3r1jj.pbmap.view.map;

import android.content.Context;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.qozix.tileview.paths.CompositePathView;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import io.github.t3r1jj.pbmap.R;
import io.github.t3r1jj.pbmap.model.map.Space;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class SpaceViewTest {

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
}