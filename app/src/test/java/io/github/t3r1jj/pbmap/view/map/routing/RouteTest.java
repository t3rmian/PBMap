package io.github.t3r1jj.pbmap.view.map.routing;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Path;

import androidx.annotation.NonNull;

import com.qozix.tileview.geom.CoordinateTranslater;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.t3r1jj.pbmap.model.map.Coordinate;

import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RouteTest {

    @Test
    public void pathFromPositions_SkipDetached() {
        Path pathMock = mock(Path.class);
        Coordinate sameAltCoordinate1 = new Coordinate(0d, 0d, 150d);
        Coordinate sameAltCoordinate2 = new Coordinate(1d, 1d, 150d);
        Coordinate sameAltCoordinate3 = new Coordinate(2d, 2d, 150d);
        List<Coordinate> positions = new ArrayList<>(Arrays.asList(
                sameAltCoordinate1,
                sameAltCoordinate2,
                sameAltCoordinate3
        ));
        Context contextMock = mock(Context.class);
        when(contextMock.getResources()).thenReturn(mock(Resources.class));
        Route route = new Route(contextMock) {
            @NonNull
            @Override
            Path createPath() {
                return pathMock;
            }
        };
        sameAltCoordinate1.setDetachedFromNext(true);
        route.pathFromPositions(mock(CoordinateTranslater.class), positions);
        verify(pathMock, times(2)).moveTo(anyFloat(), anyFloat());
        verify(pathMock, times(1)).lineTo(anyFloat(), anyFloat());
    }
}