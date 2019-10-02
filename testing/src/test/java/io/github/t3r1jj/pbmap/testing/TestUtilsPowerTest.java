package io.github.t3r1jj.pbmap.testing;

import androidx.test.espresso.intent.Intents;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.atomic.AtomicReference;

import static io.github.t3r1jj.pbmap.testing.TestUtils.withIntents;
import static junit.framework.TestCase.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {Intents.class})
public class TestUtilsPowerTest {

    @Test(expected = MockedOutcome.class)
    public void testWithIntents_Init() throws Exception {
        mockStatic(Intents.class);
        when(Intents.class, "init").thenThrow(new MockedOutcome());
        withIntents(() -> {
        });
    }

    @Test(expected = MockedOutcome.class)
    public void testWithIntents_Release() throws Exception {
        mockStatic(Intents.class);
        when(Intents.class, "release").thenThrow(new MockedOutcome());
        withIntents(() -> {
        });
    }

    @Test(expected = MockedOutcome.class)
    public void testWithIntents_InitFirst() throws Exception {
        mockStatic(Intents.class);
        when(Intents.class, "init").thenThrow(new MockedOutcome());
        when(Intents.class, "release").thenThrow(new RuntimeException("release"));
        withIntents(() -> {
            throw new RuntimeException("runnable");
        });
    }

    @Test
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

}