package io.github.t3r1jj.pbmap.logging;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.Place;

import static io.github.t3r1jj.pbmap.logging.WebLogger.PREF_KEY_MESSAGES;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ConnectionChangeBroadcastReceiverIT {

    private Context context;

    @Before
    public void setUp() {
        clearMessages();
    }

    @After
    public void tearDown() {
        clearMessages();
    }

    private void clearMessages() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().remove(PREF_KEY_MESSAGES).apply();
    }

    @Test
    @SmallTest
    public void onReceive() {
        WebLogger webLogger = new WebLogger(context);
        assertTrue(webLogger.isEmpty());
        Place place = mock(Place.class);
        when(place.getCoordinates()).thenReturn(Collections.singletonList(new Coordinate()));

        webLogger.logMessage(new Message("test", new Coordinate(), new Coordinate(), place));
        assertFalse(webLogger.isEmpty());
        WebLogger differentWebLogger = new WebLogger(context);
        assertFalse(differentWebLogger.isEmpty());

        ConnectionChangeBroadcastReceiver receiver = new ConnectionChangeBroadcastReceiver();
        receiver.onReceive(context, null);
        clearMessages();

        assertTrue(webLogger.isEmpty());
        assertTrue(differentWebLogger.isEmpty());
        WebLogger yetAnotherWebLogger = new WebLogger(context);
        assertTrue(yetAnotherWebLogger.isEmpty());
    }

    @Test
    @SmallTest
    public void onReceive_Empty() {
        ConnectionChangeBroadcastReceiver receiver = new ConnectionChangeBroadcastReceiver();
        receiver.onReceive(context, null);
    }
}