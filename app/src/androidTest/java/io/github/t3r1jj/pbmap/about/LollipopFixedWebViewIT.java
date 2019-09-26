package io.github.t3r1jj.pbmap.about;

import android.content.Context;
import android.os.Build;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class LollipopFixedWebViewIT {

    @Test
    public void testConstructors() throws Throwable {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        runOnUiThread(() -> {
            LollipopFixedWebView webView = new LollipopFixedWebView(context);
            assertFixedWebView(context, webView);
            webView = new LollipopFixedWebView(context, null);
            assertFixedWebView(context, webView);
            webView = new LollipopFixedWebView(context, null, 0);
            assertFixedWebView(context, webView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView = new LollipopFixedWebView(context, null, 0, 0);
                assertFixedWebView(context, webView);
            }
            webView = new LollipopFixedWebView(context, null, 0, false);
            assertFixedWebView(context, webView);
        });
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