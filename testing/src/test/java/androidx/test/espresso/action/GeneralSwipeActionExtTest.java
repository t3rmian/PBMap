package androidx.test.espresso.action;

import android.view.View;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;

import org.hamcrest.StringDescription;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActionsExt.swipeDownExt;
import static androidx.test.espresso.action.ViewActionsExt.swipeLeftExt;
import static androidx.test.espresso.action.ViewActionsExt.swipeRightExt;
import static androidx.test.espresso.action.ViewActionsExt.swipeUpExt;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

public class GeneralSwipeActionExtTest {

    @Test
    public void testGetLeftConstraints() {
        assertNotEquals(swipeLeft().getConstraints(), swipeLeftExt().getConstraints());
        StringDescription swipeLeftDescription = new StringDescription();
        swipeLeft().getConstraints().describeTo(swipeLeftDescription);
        StringDescription swipeLeftExtDescription = new StringDescription();
        swipeLeftExt().getConstraints().describeTo(swipeLeftExtDescription);
        assertThat(swipeLeftDescription.toString(), containsString("90 percent"));
        assertThat(swipeLeftExtDescription.toString(), containsString("1 percent"));
    }

    @Test
    public void testGetRightConstraints() {
        assertNotEquals(swipeRight().getConstraints(), swipeRightExt().getConstraints());
        StringDescription swipeRightDescription = new StringDescription();
        swipeRight().getConstraints().describeTo(swipeRightDescription);
        StringDescription swipeRightExtDescription = new StringDescription();
        swipeRightExt().getConstraints().describeTo(swipeRightExtDescription);
        assertThat(swipeRightDescription.toString(), containsString("90 percent"));
        assertThat(swipeRightExtDescription.toString(), containsString("1 percent"));
    }

    @Test
    public void testGetUpConstraints() {
        assertNotEquals(swipeUp().getConstraints(), swipeUpExt().getConstraints());
        StringDescription swipeUpDescription = new StringDescription();
        swipeUp().getConstraints().describeTo(swipeUpDescription);
        StringDescription swipeUpExtDescription = new StringDescription();
        swipeUpExt().getConstraints().describeTo(swipeUpExtDescription);
        assertThat(swipeUpDescription.toString(), containsString("90 percent"));
        assertThat(swipeUpExtDescription.toString(), containsString("1 percent"));
    }

    @Test
    public void testGetDownConstraints() {
        assertNotEquals(swipeDown().getConstraints(), swipeDownExt().getConstraints());
        StringDescription swipeDownDescription = new StringDescription();
        swipeDown().getConstraints().describeTo(swipeDownDescription);
        StringDescription swipeDownExtDescription = new StringDescription();
        swipeDownExt().getConstraints().describeTo(swipeDownExtDescription);
        assertThat(swipeDownDescription.toString(), containsString("90 percent"));
        assertThat(swipeDownExtDescription.toString(), containsString("1 percent"));
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
        assertEquals(swipeLeft().getDescription(), swipeLeftExt().getDescription());
        assertEquals(swipeDown().getDescription(), swipeDownExt().getDescription());
        assertEquals(swipeUp().getDescription(), swipeUpExt().getDescription());
    }
}