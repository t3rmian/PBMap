package androidx.test.espresso.action;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.github.t3r1jj.pbmap.testing.MockedOutcome;

import static androidx.test.espresso.action.ViewActionsExt.swipeDownExt;
import static androidx.test.espresso.action.ViewActionsExt.swipeLeftExt;
import static androidx.test.espresso.action.ViewActionsExt.swipeRightExt;
import static androidx.test.espresso.action.ViewActionsExt.swipeUpExt;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * The only change from the standard implementation is 1% visibility which is in form of GeneralSwipeActionExt
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ViewActionsExt.class)
public class ViewActionsExtTest {

    @Test(expected = MockedOutcome.class)
    public void testSwipeLeftExt() throws Exception {
        whenNew(GeneralSwipeActionExt.class)
                .withAnyArguments()
                .thenThrow(new MockedOutcome());

        swipeLeftExt();
    }

    @Test(expected = MockedOutcome.class)
    public void testSwipeRightExt() throws Exception {
        whenNew(GeneralSwipeActionExt.class)
                .withAnyArguments()
                .thenThrow(new MockedOutcome());

        swipeRightExt();
    }

    @Test(expected = MockedOutcome.class)
    public void testSwipeDownExt() throws Exception {
        whenNew(GeneralSwipeActionExt.class)
                .withAnyArguments()
                .thenThrow(new MockedOutcome());

        swipeDownExt();
    }

    @Test(expected = MockedOutcome.class)
    public void testSwipeUpExt() throws Exception {
        whenNew(GeneralSwipeActionExt.class)
                .withAnyArguments()
                .thenThrow(new MockedOutcome());

        swipeUpExt();
    }
}