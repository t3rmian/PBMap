package io.github.t3r1jj.pbmap.logging;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.Spot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class WebLoggerTest {

    private WebLogger logger;
    private SharedPreferences preferences;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getContext();
        logger = new WebLogger(appContext);
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    @Test
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
    public void sendCompleteMessages() throws InterruptedException {
        preferences.edit().clear().apply();
        ArrayList<Message> messages = logger.getMessages();
        assertTrue("Messages empty initially", messages.isEmpty());
        Spot closestPlace = new Spot() {
            @Override
            public List<Coordinate> getCoordinates() {
                return Collections.singletonList(new Coordinate(7, 8, 9));
            }
        };
        closestPlace.setId("closestPlaceId");
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
    public void sendTwoCompleteMessages() throws InterruptedException {
        preferences.edit().clear().apply();
        ArrayList<Message> messages = logger.getMessages();
        assertTrue("Messages empty initially", messages.isEmpty());
        Spot closestPlace = new Spot() {
            @Override
            public List<Coordinate> getCoordinates() {
                return Collections.singletonList(new Coordinate(7, 8, 9));
            }
        };
        closestPlace.setId("closestPlaceId");
        Message message = new Message("map1", new Coordinate(1, 2, 3), new Coordinate(4, 5, 6), closestPlace);
        Message message2 = new Message("map2", new Coordinate(1, 2, 3), new Coordinate(4, 5, 6), closestPlace);
        message.setDescription("desc");
        logger.logMessage(message);
        logger.logMessage(message2);
        messages = logger.getMessages();
        assertEquals("Scheduled two messages", 2, messages.size());
        logger.sendMessages();
        for (int i = 0; !logger.getMessages().isEmpty() && i < 15; i++) {
            Thread.sleep(1000);
        }
        messages = logger.getMessages();
        assertTrue("All scheduled messages have been sent", messages.isEmpty());
    }

}