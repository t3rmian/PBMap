package io.github.t3r1jj.pbmap.testing;

import android.util.Log;

import androidx.test.espresso.intent.Intents;
import androidx.test.uiautomator.UiDevice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.atomic.AtomicReference;

import static io.github.t3r1jj.pbmap.testing.TestUtils.pressDoubleBack;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIntents;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class TestUtilsPowerTest {

    @Test(expected = MockedOutcome.class)
    @PrepareForTest(value = {Intents.class})
    public void testWithIntents_Init() throws Exception {
        mockStatic(Intents.class);
        when(Intents.class, "init").thenThrow(new MockedOutcome());
        withIntents(() -> {
        });
    }

    @Test(expected = MockedOutcome.class)
    @PrepareForTest(value = {Intents.class})
    public void testWithIntents_Release() throws Exception {
        mockStatic(Intents.class);
        when(Intents.class, "release").thenThrow(new MockedOutcome());
        withIntents(() -> {
        });
    }

    @Test(expected = MockedOutcome.class)
    @PrepareForTest(value = {Intents.class})
    public void testWithIntents_InitFirst() throws Exception {
        mockStatic(Intents.class);
        when(Intents.class, "init").thenThrow(new MockedOutcome());
        when(Intents.class, "release").thenThrow(new RuntimeException("release"));
        withIntents(() -> {
            throw new RuntimeException("runnable");
        });
    }

    @Test
    @PrepareForTest(value = {Intents.class})
    public void testWithIntents_RunnableSecond_ReleaseLast() throws Exception {
        mockStatic(Intents.class);
        AtomicReference<Boolean> intended = new AtomicReference<>(false);
        when(Intents.class, "release").then(a -> {
            assertTrue(intended.get());
            return null;
        });
        withIntents(() -> {
            intended.set(true);
        });
        assertTrue(intended.get());
    }

    @Test(expected = MockedOutcome.class)
    @PrepareForTest(value = {Log.class})
    public void testDoubleBack_WithExceptionWarning() {
        mockStatic(Log.class);
        UiDevice deviceMock = PowerMockito.mock(UiDevice.class);
        RuntimeException throwableMock = new RuntimeException("mocked exception");
        when(deviceMock.pressBack()).thenThrow(throwableMock);
        when(Log.w(eq("DoubleBackPress"), eq(throwableMock)))
                .thenThrow(MockedOutcome.class);
        pressDoubleBack(deviceMock);
    }
}