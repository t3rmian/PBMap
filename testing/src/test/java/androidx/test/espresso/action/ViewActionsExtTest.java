package androidx.test.espresso.action;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.github.t3r1jj.pbmap.testing.VerifyAnswer;

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

    private final VerifyAnswer verifyAnswer = new VerifyAnswer();

    @Test
    public void testSwipeLeftExt() throws Exception {
        whenNew(GeneralSwipeActionExt.class)
                .withAnyArguments()
                .then(verifyAnswer);

        swipeLeftExt();
        verifyAnswer.assertCalled();
    }

    @Test
    public void testSwipeRightExt() throws Exception {
        whenNew(GeneralSwipeActionExt.class)
                .withAnyArguments()
                .then(verifyAnswer);

        swipeRightExt();
        verifyAnswer.assertCalled();
    }

    @Test
    public void testSwipeDownExt() throws Exception {
        whenNew(GeneralSwipeActionExt.class)
                .withAnyArguments()
                .then(verifyAnswer);

        swipeDownExt();
        verifyAnswer.assertCalled();
    }

    @Test
    public void testSwipeUpExt() throws Exception {
        whenNew(GeneralSwipeActionExt.class)
                .withAnyArguments()
                .then(verifyAnswer);

        swipeUpExt();
        verifyAnswer.assertCalled();
    }
}