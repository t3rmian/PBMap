package io.github.t3r1jj.pbmap.view.map.routing;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.qozix.tileview.paths.CompositePathView;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.t3r1jj.pbmap.view.map.MapView;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class RouteIT {

    @Test
    @SmallTest
    public void addToMap() {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Route route = new Route(targetContext);
        MapView map = mock(MapView.class);
        when(map.getCompositePathView()).thenReturn(new CompositePathView(targetContext));
        route.addToMap(map);
    }


    @Test
    @SmallTest
    public void calculateDistance() {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals(0, new Route(targetContext).calculateDistance(), 0.01);
    }
}