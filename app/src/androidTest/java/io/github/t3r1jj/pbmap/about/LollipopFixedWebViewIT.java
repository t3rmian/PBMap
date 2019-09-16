package io.github.t3r1jj.pbmap.about;

import android.content.Context;
import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

@SuppressWarnings("JavaReflectionMemberAccess")
@RunWith(AndroidJUnit4.class)
public class LollipopFixedWebViewIT {

    private int buildVersionSdk;
    private Field buildVersionSdkField;

    @Before
    public void saveBuildVersion() throws NoSuchFieldException {
        buildVersionSdkField = Build.VERSION.class.getField("SDK_INT");
        buildVersionSdk = Build.VERSION.SDK_INT;
    }

    @After
    public void revertBuildVersion() throws NoSuchFieldException, IllegalAccessException {
        setFinalStatic(buildVersionSdkField, buildVersionSdk);
    }

    @Test
    public void testConstructors_V21() throws Throwable {
        setFinalStatic(buildVersionSdkField, 21);
        runTest();
    }

    @Test
    public void testConstructors_V22() throws Throwable {
        setFinalStatic(buildVersionSdkField, 22);
        runTest();
    }

    @Test
    public void testConstructors_V23() throws Throwable {
        setFinalStatic(buildVersionSdkField, 23);
        runTest();
    }

    @Test
    public void testConstructors_V24() throws Throwable {
        setFinalStatic(buildVersionSdkField, 24);
        runTest();
    }

    private void runTest() throws Throwable {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        runOnUiThread(() -> {
            LollipopFixedWebView webView = new LollipopFixedWebView(context);
            assertFixedWebView(context, webView);
            webView = new LollipopFixedWebView(context, null);
            assertFixedWebView(context, webView);
            webView = new LollipopFixedWebView(context, null, 0);
            assertFixedWebView(context, webView);
            webView = new LollipopFixedWebView(context, null, 0, 0);
            assertFixedWebView(context, webView);
            webView = new LollipopFixedWebView(context, null, 0, false);
            assertFixedWebView(context, webView);
        });
    }

    private static void setFinalStatic(Field field, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("accessFlags");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

    private void assertFixedWebView(Context context, LollipopFixedWebView webView) {
        switch (Build.VERSION.SDK_INT) {
            case 22:
            case 21:
                assertThat(context, not(is(webView.getContext())));
                return;
            default:
                assertThat(context, is(webView.getContext()));
        }
    }

}