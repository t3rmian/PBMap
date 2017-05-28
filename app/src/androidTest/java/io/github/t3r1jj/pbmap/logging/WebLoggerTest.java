package io.github.t3r1jj.pbmap.logging;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.t3r1jj.pbmap.model.map.Coordinate;
import io.github.t3r1jj.pbmap.model.map.Spot;

@RunWith(AndroidJUnit4.class)
public class WebLoggerTest {

    private WebLogger logger;
    private SharedPreferences preferences;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        logger = new WebLogger(appContext);
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    @Test
    public void scheduleMessage() {
        preferences.edit().clear().apply();
        ArrayList<Message> messages = logger.getMessages();
        Assert.assertTrue(messages.isEmpty());
        Message message = new Message(null, null, null, null);
        logger.logMessage(message);
        messages = logger.getMessages();
        Assert.assertTrue(!messages.isEmpty());
    }

    @Test
    public void sendIncompleteMessages() throws InterruptedException {
        preferences.edit().clear().apply();
        ArrayList<Message> messages = logger.getMessages();
        Assert.assertTrue(messages.isEmpty());
        Message message = new Message(null, null, null, null);
        logger.logMessage(message);
        messages = logger.getMessages();
        Assert.assertTrue(!messages.isEmpty());
        logger.sendMessages();
        Thread.sleep(5000);
        messages = logger.getMessages();
        Assert.assertTrue(messages.isEmpty());
    }

    @Test
    public void sendCompleteMessages() throws InterruptedException {
        preferences.edit().clear().apply();
        ArrayList<Message> messages = logger.getMessages();
        Assert.assertTrue(messages.isEmpty());
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
        Assert.assertTrue(!messages.isEmpty());
        logger.sendMessages();
        Thread.sleep(5000);
        messages = logger.getMessages();
        Assert.assertTrue(messages.isEmpty());
    }

    @Test
    public void sendTwoCompleteMessages() throws InterruptedException {
        preferences.edit().clear().apply();
        ArrayList<Message> messages = logger.getMessages();
        Assert.assertTrue(messages.isEmpty());
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
        Assert.assertTrue(messages.size() == 2);
        logger.sendMessages();
        Thread.sleep(5000);
        messages = logger.getMessages();
        Assert.assertTrue(messages.isEmpty());
    }

}