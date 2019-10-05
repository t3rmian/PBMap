package io.github.t3r1jj.pbmap.about;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Looper;
import android.widget.Toast;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import io.github.t3r1jj.pbmap.BuildConfig;
import io.github.t3r1jj.pbmap.R;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class OnRateClickListenerIT {

    private boolean verifiedToastCreation = false;

    @Test
    @SmallTest
    public void onClick() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getPackageName()).thenReturn(BuildConfig.APPLICATION_ID);
        OnRateClickListener listener = new OnRateClickListener(context);

        listener.onClick(null);

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(context, times(1)).startActivity(intentCaptor.capture());
        assertThat(intentCaptor.getAllValues(), hasItem(
                new StoreRedirectIntentMatcher("market://details?id=")
        ));
    }

    @Test
    @SmallTest
    public void onClick_Fallback() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getPackageName()).thenReturn(BuildConfig.APPLICATION_ID);
        OnRateClickListener listener = new OnRateClickListener(context);

        doAnswer(answer -> {
            doNothing().when(context).startActivity(any(Intent.class));
            throw new ActivityNotFoundException("Mock activity not found");
        }).when(context).startActivity(any(Intent.class));

        listener.onClick(null);

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(context, times(2)).startActivity(intentCaptor.capture());
        assertThat(intentCaptor.getAllValues(), hasItem(
                new StoreRedirectIntentMatcher("https://play.google.com/store/apps/details?id=")
        ));
    }

    @Test
    @SmallTest
    public void onClick_FallbackDidNotWork() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getPackageName()).thenReturn(BuildConfig.APPLICATION_ID);
        OnRateClickListener listener = new OnRateClickListener(context);

        doAnswer(answer -> {
            doAnswer(answer2 -> {
                doNothing().when(context).startActivity(any(Intent.class));
                throw new ActivityNotFoundException("Mock activity not found");
            }).when(context).startActivity(any(Intent.class));
            throw new ActivityNotFoundException("Mock activity not found");
        }).when(context).startActivity(any(Intent.class));
        Resources resources = mock(Resources.class);
        when(context.getResources()).thenReturn(resources);
        doAnswer((ans) -> {
            assertEquals(R.string.could_not_open_android_market, Long.parseLong(ans.getArgument(0).toString()));
            verifiedToastCreation = true;
            return "mock";
        }).when(resources).getString(R.string.could_not_open_android_market);

        Looper.prepare();

        try {
            listener.onClick(null);
        } catch (NullPointerException toastException) {
            assertThat(Arrays.asList(toastException.getStackTrace()),
                    hasItem(new StackTraceElementTypeSafeMatcher(Toast.class)));
        }

        assertTrue(verifiedToastCreation);
    }

    private static class StoreRedirectIntentMatcher extends TypeSafeMatcher<Intent> {

        private final String uriBase;

        StoreRedirectIntentMatcher(String uriBase) {
            this.uriBase = uriBase;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("Has market action with URI");
        }

        @Override
        protected boolean matchesSafely(Intent item) {
            return Intent.ACTION_VIEW.equals(item.getAction()) &&
                    Uri.parse(uriBase + BuildConfig.APPLICATION_ID).equals(item.getData());
        }
    }

    private static class StackTraceElementTypeSafeMatcher extends TypeSafeMatcher<StackTraceElement> {

        private final Class aClass;

        StackTraceElementTypeSafeMatcher(@NotNull Class aClass) {
            this.aClass = aClass;
        }

        @Override
        protected boolean matchesSafely(StackTraceElement item) {
            return aClass.getName().equals(item.getClassName());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("of Toast class");
        }
    }
}