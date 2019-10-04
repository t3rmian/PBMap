package io.github.t3r1jj.pbmap.testing;

import android.app.Instrumentation;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.t3r1jj.pbmap.testing.TestUtils.allowPermissionsIfNeeded;
import static io.github.t3r1jj.pbmap.testing.TestUtils.pressDoubleBack;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIntents;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

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
        UiDevice deviceMock = mock(UiDevice.class);
        RuntimeException throwableMock = new RuntimeException("mocked exception");
        when(deviceMock.pressBack()).thenThrow(throwableMock);
        when(Log.w(eq("DoubleBackPress"), eq(throwableMock)))
                .thenThrow(MockedOutcome.class);
        pressDoubleBack(deviceMock);
    }

    @Test
    @PrepareForTest(value = {Build.VERSION.class, InstrumentationRegistry.class, ContextCompat.class})
    public void testAllowPermissionsIfNeeded_AlreadyGranted() throws UiObjectNotFoundException, NoSuchFieldException, IllegalAccessException {
        mockStatic(InstrumentationRegistry.class);
        Instrumentation instrumentation = mock(Instrumentation.class);
        when(InstrumentationRegistry.getInstrumentation()).thenReturn(instrumentation);
        Field field = Build.VERSION.class.getField("SDK_INT");
        field.setAccessible(true);
        field.set(null, 30);

        mockStatic(ContextCompat.class);
        when(ContextCompat.checkSelfPermission(any(), anyString())).thenReturn(PackageManager.PERMISSION_GRANTED);
        allowPermissionsIfNeeded("some permission");
    }

    @Test
    @PrepareForTest(value = {
            Build.VERSION.class, InstrumentationRegistry.class, ContextCompat.class,
            UiDevice.class, UiSelector.class})
    public void testAllowPermissionsIfNeeded_Grant() throws Exception {
        mockStatic(InstrumentationRegistry.class);
        Instrumentation instrumentation = mock(Instrumentation.class);
        when(InstrumentationRegistry.getInstrumentation()).thenReturn(instrumentation);
        Field field = Build.VERSION.class.getField("SDK_INT");
        field.setAccessible(true);
        field.set(null, 30);

        mockStatic(ContextCompat.class);
        when(ContextCompat.checkSelfPermission(any(), anyString())).thenReturn(PackageManager.PERMISSION_DENIED);

        mockStatic(UiDevice.class);
        UiDevice uiDeviceMock = mock(UiDevice.class);
        when(UiDevice.getInstance(any())).thenReturn(uiDeviceMock);

        UiObject objectMock = mock(UiObject.class);

        UiSelector uiSelector = spy(new UiSelector());
        whenNew(UiSelector.class).withNoArguments().thenReturn(uiSelector);
        whenNew(UiSelector.class).withAnyArguments().thenReturn(uiSelector);
        when(uiDeviceMock.findObject(any(UiSelector.class))).thenReturn(objectMock);

        AtomicInteger count = new AtomicInteger(0);

        doAnswer(a -> {
            count.incrementAndGet();
            return null;
        }).when(objectMock).click();

        allowPermissionsIfNeeded("some permission");

        assertEquals(0, count.get());

        when(objectMock.exists()).thenReturn(true);

        allowPermissionsIfNeeded("some permission");

        assertEquals(1, count.get());
    }
}