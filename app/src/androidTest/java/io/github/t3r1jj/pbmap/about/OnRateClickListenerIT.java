package io.github.t3r1jj.pbmap.about;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import io.github.t3r1jj.pbmap.BuildConfig;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class OnRateClickListenerIT {

    @Test
    public void onClick() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getPackageName()).thenReturn(BuildConfig.APPLICATION_ID);
        OnRateClickListener listener = new OnRateClickListener(context);
        doAnswer(answer -> {
            doNothing().when(context).startActivity(any(Intent.class));
            assertThat(Arrays.asList(answer.getArguments()), hasItem(new StoreRedirectIntentMatcher("market://details?id=")));
            throw new ActivityNotFoundException("Mock activity not found");
        }).when(context).startActivity(any(Intent.class));
        listener.onClick(null);
        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(context, times(2)).startActivity(intentCaptor.capture());
        assertThat(intentCaptor.getAllValues(), hasItem(
                new StoreRedirectIntentMatcher("https://play.google.com/store/apps/details?id=")
        ));
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
}