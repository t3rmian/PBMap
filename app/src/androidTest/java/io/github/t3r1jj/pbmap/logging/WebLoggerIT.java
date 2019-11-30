package io.github.t3r1jj.pbmap.logging;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.filters.MediumTest;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.Spot;
import io.github.t3r1jj.pbmap.testing.RetryRunner;

import static io.github.t3r1jj.pbmap.logging.WebLogger.PREF_KEY_MESSAGES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RetryRunner.class)
public class WebLoggerIT {

    private WebLogger logger;
    private SharedPreferences preferences;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        logger = new WebLogger(appContext);
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        clearMessages();
    }


    @After
    public void tearDown() {
        clearMessages();
    }

    private void clearMessages() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(InstrumentationRegistry.getInstrumentation().getTargetContext());
        preferences.edit().remove(PREF_KEY_MESSAGES).apply();
    }

    @Test
    @SmallTest
    public void scheduleMessage() {
        preferences.edit().clear().apply();
        ArrayList<Message> messages = logger.getMessages();
        assertTrue("Messages empty initially", messages.isEmpty());
        Message message = new Message(null, null, null, null);
        logger.logMessage(message);
        messages = logger.getMessages();
        assertFalse("Scheduled at least one message", messages.isEmpty());
    }

    @Test
    @MediumTest
    public void sendIncompleteMessages() throws InterruptedException {
        preferences.edit().clear().apply();
        ArrayList<Message> messages = logger.getMessages();
        assertTrue("Messages empty initially", messages.isEmpty());
        Message message = new Message(null, null, null, null);
        logger.logMessage(message);
        messages = logger.getMessages();
        assertFalse("Scheduled at least one message", messages.isEmpty());
        logger.sendMessages();
        for (int i = 0; !logger.getMessages().isEmpty() && i < 15; i++) {
            Thread.sleep(1000);
        }
        messages = logger.getMessages();
        assertTrue("All scheduled messages have been sent", messages.isEmpty());
    }

    @Test
    @MediumTest
    public void sendCompleteMessages() throws InterruptedException {
        preferences.edit().clear().apply();
        ArrayList<Message> messages = logger.getMessages();
        assertTrue("Messages empty initially", messages.isEmpty());
        Spot closestPlace = new Spot() {
            {
                this.id = "closestPlaceId";
            }

            @Override
            public List<Coordinate> getCoordinates() {
                return Collections.singletonList(new Coordinate(7, 8, 9));
            }
        };
        Message message = new Message("map", new Coordinate(1, 2, 3), new Coordinate(4, 5, 6), closestPlace);
        message.setDescription("desc");
        logger.logMessage(message);
        messages = logger.getMessages();
        assertFalse("Scheduled at least one message", messages.isEmpty());
        logger.sendMessages();
        for (int i = 0; !logger.getMessages().isEmpty() && i < 15; i++) {
            Thread.sleep(1000);
        }
        messages = logger.getMessages();
        assertTrue("All scheduled messages have been sent", messages.isEmpty());
    }

    @Test
    @MediumTest
    public void sendTwoCompleteMessages() throws InterruptedException {
        preferences.edit().clear().apply();
        ArrayList<Message> messages = logger.getMessages();
        assertTrue("Messages empty initially", messages.isEmpty());
        Spot closestPlace = new Spot() {
            {
                this.id = "closestPlaceId";
            }

            @Override
            public List<Coordinate> getCoordinates() {
                return Collections.singletonList(new Coordinate(7, 8, 9));
            }
        };
        Message message = new Message("map1", new Coordinate(1, 2, 3), new Coordinate(4, 5, 6), closestPlace);
        Message message2 = new Message("map2", new Coordinate(1, 2, 3), new Coordinate(4, 5, 6), closestPlace);
        message.setDescription("desc");
        logger.logMessage(message);
        logger.logMessage(message2);
        messages = logger.getMessages();
        assertEquals("Scheduled two messages", 2, messages.size());
        logger.sendMessages();
        for (int i = 0; !logger.getMessages().isEmpty() && i < 20; i++) {
            Thread.sleep(1000);
        }
        if (!logger.getMessages().isEmpty()) {
            logger.sendMessages();
            for (int i = 0; !logger.getMessages().isEmpty() && i < 20; i++) {
                Thread.sleep(1000);
            }
        }
        messages = logger.getMessages();
        assertTrue("All scheduled messages have been sent", messages.isEmpty());
    }

}