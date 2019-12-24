package io.github.t3r1jj.pbmap.testing;

import android.app.Instrumentation;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.util.EspressoOptional;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static io.github.t3r1jj.pbmap.testing.TestUtils.allowPermissionsIfNeeded;
import static io.github.t3r1jj.pbmap.testing.TestUtils.pressDoubleBack;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withIntents;
import static io.github.t3r1jj.pbmap.testing.TestUtils.withMenuIdOrContentDescription;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
public class TestUtilsPowerTest {

    private VerifyAnswer verifyAnswer;

    @Test
    @PrepareForTest(value = {Intents.class})
    public void testWithIntents_Init() throws Exception {
        verifyAnswer = new VerifyAnswer(Answers.RETURNS_DEFAULTS);
        mockStatic(Intents.class);
        when(Intents.class, "init").then(verifyAnswer);
        withIntents(() -> {
        });
        verifyAnswer.assertCalled();
    }

    @Test
    @PrepareForTest(value = {Intents.class})
    public void testWithIntents_Release() throws Exception {
        verifyAnswer = new VerifyAnswer(Answers.RETURNS_SMART_NULLS);
        mockStatic(Intents.class);
        when(Intents.class, "release").then(verifyAnswer);
        withIntents(() -> {
        });
        verifyAnswer.assertCalled();
    }

    @Test
    @PrepareForTest(value = {Intents.class})
    public void testWithIntents_InitFirst() throws Exception {
        AtomicReference<Boolean> runnableCalled = new AtomicReference<>(false);
        verifyAnswer = new VerifyAnswer(Answers.RETURNS_SMART_NULLS);
        VerifyAnswer verifyReleaseAnswer = new VerifyAnswer(Answers.RETURNS_SMART_NULLS) {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                assertTrue(runnableCalled.get());
                return super.answer(invocation);
            }
        };
        mockStatic(Intents.class);
        when(Intents.class, "init").then(verifyAnswer);
        when(Intents.class, "release").then(verifyReleaseAnswer);
        withIntents(() -> {
            verifyAnswer.assertCalled();
            runnableCalled.set(true);
        });
        verifyReleaseAnswer.assertCalled();
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
        withIntents(() -> intended.set(true));
        assertTrue(intended.get());
    }

    @Test
    @PrepareForTest(value = {Log.class})
    public void testDoubleBack_WithExceptionWarning() {
        verifyAnswer = new VerifyAnswer();
        mockStatic(Log.class);
        UiDevice deviceMock = mock(UiDevice.class);
        RuntimeException throwableMock = new RuntimeException("mocked exception");
        when(deviceMock.pressBack()).then(verifyAnswer);
        when(Log.w(eq("DoubleBackPress"), eq(throwableMock)))
                .then(verifyAnswer);
        pressDoubleBack(deviceMock);
        verifyAnswer.assertCalled();
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

    @Test
    @PrepareForTest(value = {Espresso.class})
    @SuppressStaticInitializationFor("androidx.test.espresso.Espresso")
    public void testWithMenuIdOrContentDescription() {
        mockStatic(Espresso.class);
        when(Espresso.onView(any())).thenReturn(mock(ViewInteraction.class));
        Matcher<View> withId = withMenuIdOrContentDescription(1, 1);
        StringDescription description = new StringDescription();
        withId.describeTo(description);
        StringDescription description2 = new StringDescription();
        withId(1).describeTo(description2);
        assertEquals(description.toString(), description2.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    @PrepareForTest(value = {Espresso.class, InstrumentationRegistry.class})
    @SuppressStaticInitializationFor("androidx.test.espresso.Espresso")
    public void testWithMenuIdOrContentDescription_NoId() {
        mockStatic(Espresso.class);
        mockStatic(InstrumentationRegistry.class);
        when(InstrumentationRegistry.getInstrumentation()).thenReturn(mock(Instrumentation.class));
        ViewInteraction viewInteraction = mock(ViewInteraction.class);
        when(Espresso.onView(any())).thenReturn(viewInteraction);
        NoMatchingViewException noMatchingViewException = new NoMatchingViewException.Builder()
                .withViewMatcher(mock(Matcher.class))
                .withRootView(mock(View.class))
                .withAdapterViews(Collections.emptyList())
                .withAdapterViewWarning(EspressoOptional.absent())
                .build();
        when(viewInteraction.check(any())).thenThrow(noMatchingViewException);

        Matcher<View> withContentDescription = withMenuIdOrContentDescription(2, 3);
        StringDescription description = new StringDescription();
        withContentDescription.describeTo(description);
        StringDescription description2 = new StringDescription();
        withContentDescription(3).describeTo(description2);
        assertEquals(description.toString(), description2.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    @PrepareForTest(value = {Espresso.class, InstrumentationRegistry.class})
    @SuppressStaticInitializationFor("androidx.test.espresso.Espresso")
    public void testWithMenuIdOrContentDescription_NoId_NoBar() throws Exception {
        mockStatic(Espresso.class);
        mockStatic(InstrumentationRegistry.class);
        when(InstrumentationRegistry.getInstrumentation()).thenReturn(mock(Instrumentation.class));
        ViewInteraction viewInteraction = mock(ViewInteraction.class);
        when(Espresso.onView(any())).thenReturn(viewInteraction);
        NoMatchingViewException noMatchingViewException = new NoMatchingViewException.Builder()
                .withViewMatcher(mock(Matcher.class))
                .withRootView(mock(View.class))
                .withAdapterViews(Collections.emptyList())
                .withAdapterViewWarning(EspressoOptional.absent())
                .build();
        when(viewInteraction.check(any())).thenThrow(noMatchingViewException);
        doThrow(noMatchingViewException).when(Espresso.class, "openActionBarOverflowOrOptionsMenu", any());

        Matcher<View> withContentDescription = withMenuIdOrContentDescription(2, 3);
        StringDescription description = new StringDescription();
        withContentDescription.describeTo(description);
        StringDescription description2 = new StringDescription();
        withContentDescription(3).describeTo(description2);
        assertEquals(description.toString(), description2.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    @PrepareForTest(value = {Espresso.class, InstrumentationRegistry.class})
    @SuppressStaticInitializationFor("androidx.test.espresso.Espresso")
    public void testWithMenuIdOrContentDescription_NoId_OpenBar_AndDrinkLegally() throws Exception {
        verifyAnswer = new VerifyAnswer(Answers.RETURNS_SMART_NULLS);
        mockStatic(Espresso.class);
        mockStatic(InstrumentationRegistry.class);
        when(InstrumentationRegistry.getInstrumentation()).thenReturn(mock(Instrumentation.class));
        ViewInteraction viewInteraction = mock(ViewInteraction.class);
        when(Espresso.onView(any())).thenReturn(viewInteraction);
        NoMatchingViewException noMatchingViewException = new NoMatchingViewException.Builder()
                .withViewMatcher(mock(Matcher.class))
                .withRootView(mock(View.class))
                .withAdapterViews(Collections.emptyList())
                .withAdapterViewWarning(EspressoOptional.absent())
                .build();
        when(viewInteraction.check(any())).thenThrow(noMatchingViewException);
        when(Espresso.class, "openActionBarOverflowOrOptionsMenu", any()).then(verifyAnswer);

        withMenuIdOrContentDescription(2, 3);
        verifyAnswer.assertCalled();
    }
}