package androidx.test.espresso.action;

import android.view.View;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;

import org.hamcrest.StringDescription;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActionsExt.swipeLeftExt;
import static androidx.test.espresso.action.ViewActionsExt.swipeRightExt;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

public class GeneralSwipeActionExtTest {

    @Test
    public void testGetConstraints() {
        assertNotEquals(swipeLeft().getConstraints(), swipeLeftExt().getConstraints());

        StringDescription swipeLeftDescription = new StringDescription();
        swipeLeft().getConstraints().describeTo(swipeLeftDescription);
        StringDescription swipeLeftExtDescription = new StringDescription();
        swipeLeftExt().getConstraints().describeTo(swipeLeftExtDescription);
        assertThat(swipeLeftDescription.toString(), containsString("90 percent"));
        assertThat(swipeLeftExtDescription.toString(), containsString("1 percent"));
    }

    @Test
    public void testPerform() {
        GeneralSwipeActionExt swipe = new GeneralSwipeActionExt(
                mock(Swipe.class),
                mock(GeneralLocation.class),
                mock(GeneralLocation.class),
                Press.FINGER);
        UiController controllerMock = mock(UiController.class);
        swipe.perform(controllerMock, mock(View.class));
    }

    @Test(expected = PerformException.class)
    public void testPerform_Invalid() {
        GeneralSwipeActionExt swipe = new GeneralSwipeActionExt(
                Swipe.FAST,
                mock(GeneralLocation.class),
                mock(GeneralLocation.class),
                Press.FINGER);
        UiController controllerMock = mock(UiController.class);
        swipe.perform(controllerMock, mock(View.class));
    }

    @Test(expected = PerformException.class)
    public void testPerform_Failure() {
        Swipe swipeMock = mock(Swipe.class);
        when(swipeMock.sendSwipe(any(), any(), any(), any())).thenReturn(Swiper.Status.FAILURE);
        GeneralLocation locationMock = mock(GeneralLocation.class);
        when(locationMock.calculateCoordinates(any())).thenReturn(new float[]{0f, 0f, 0f, 0f});
        GeneralSwipeActionExt swipe = new GeneralSwipeActionExt(
                swipeMock,
                locationMock,
                locationMock,
                Press.FINGER);
        UiController controllerMock = mock(UiController.class);
        swipe.perform(controllerMock, mock(View.class));
    }

    @Test
    public void getDescription() {
        assertEquals(swipeRight().getDescription(), swipeRightExt().getDescription());
    }
}