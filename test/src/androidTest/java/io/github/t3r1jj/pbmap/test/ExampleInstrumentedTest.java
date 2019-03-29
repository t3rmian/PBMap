package io.github.t3r1jj.pbmap.test;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.runner.Description.createSuiteDescription;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class ExampleInstrumentedTest {
    @Test(expected = ExceptionMock.class)
    public void testScreenshotOnTestFailedRule() {
        PowerMockito.mockStatic(Log.class);

        ScreenshotOnTestFailedRule rule = new ScreenshotOnTestFailedRule();
        when(Log.i(eq(ScreenshotOnTestFailedRule.class.getSimpleName()), contains("Taking a screenshot")))
                .thenThrow(new ExceptionMock());
        rule.failed(new RuntimeException("mock"), createSuiteDescription(getClass()));
    }

    public static class ExceptionMock extends RuntimeException {
    }
}
