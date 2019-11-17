package androidx.test.espresso.action;

import android.view.View;
import android.view.ViewConfiguration;

import androidx.test.espresso.UiController;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.atomic.AtomicReference;

import io.github.t3r1jj.pbmap.testing.VerifyAnswer;

import static androidx.test.espresso.action.ViewActionsExt.swipeDownExt;
import static androidx.test.espresso.action.ViewActionsExt.swipeLeftExt;
import static androidx.test.espresso.action.ViewActionsExt.swipeRightExt;
import static androidx.test.espresso.action.ViewActionsExt.swipeUpExt;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * The only change from the standard implementation is 1% visibility which is in form of GeneralSwipeActionExt
 */
@RunWith(PowerMockRunner.class)
public class ViewActionsExtTest {

    private final VerifyAnswer verifyAnswer = new VerifyAnswer();

    @Test
    @PrepareForTest(ViewActionsExt.class)
    public void testSwipeLeftExt() throws Exception {
        whenNew(GeneralSwipeActionExt.class)
                .withAnyArguments()
                .then(verifyAnswer);

        swipeLeftExt();
        verifyAnswer.assertCalled();
    }

    @Test
    @PrepareForTest(ViewActionsExt.class)
    public void testSwipeRightExt() throws Exception {
        whenNew(GeneralSwipeActionExt.class)
                .withAnyArguments()
                .then(verifyAnswer);

        swipeRightExt();
        verifyAnswer.assertCalled();
    }

    @Test
    @PrepareForTest(ViewActionsExt.class)
    public void testSwipeDownExt() throws Exception {
        whenNew(GeneralSwipeActionExt.class)
                .withAnyArguments()
                .then(verifyAnswer);

        swipeDownExt();
        verifyAnswer.assertCalled();
    }

    @Test
    @PrepareForTest(ViewActionsExt.class)
    public void testSwipeUpExt() throws Exception {
        whenNew(GeneralSwipeActionExt.class)
                .withAnyArguments()
                .then(verifyAnswer);

        swipeUpExt();
        verifyAnswer.assertCalled();
    }


    @Test
    @PrepareForTest(ViewConfiguration.class)
    public void testPerform_LoopMainThread() throws Exception {
        AtomicReference<Boolean> loopMainThreadCalled = new AtomicReference<>(false);
        mockStatic(ViewConfiguration.class);
        Swipe swiper = mock(Swipe.class);
        GeneralLocation locationMock = mock(GeneralLocation.class);
        when(locationMock.calculateCoordinates(any())).thenReturn(new float[]{0f, 0f, 0f, 0f});
        GeneralSwipeActionExt swipe = spy(new GeneralSwipeActionExt(
                swiper,
                locationMock,
                locationMock,
                Press.FINGER));
        UiController controllerMock = mock(UiController.class);
        doAnswer(a3 -> Swiper.Status.SUCCESS).when(swiper).sendSwipe(any(), any(), any(), any());
        PowerMockito.when(ViewConfiguration.class, "getPressedStateDuration").thenReturn(99);
        doAnswer(a -> {
            loopMainThreadCalled.set(true);
            return null;
        }).when(controllerMock).loopMainThreadForAtLeast(anyLong());

        swipe.perform(controllerMock, mock(View.class));
        assertTrue(loopMainThreadCalled.get());
    }

    @Test
    public void testPerformTap() {
        ViewActionsExt.tap();
    }
}