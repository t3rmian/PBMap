package io.github.t3r1jj.pbmap.logging;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import io.github.t3r1jj.pbmap.main.DeviceServices;

import static android.provider.Settings.Secure.ANDROID_ID;

public class WebLogger extends ContextWrapper {

    static final String PREF_KEY_MESSAGES = "io.github.t3r1jj.pbmap.logging.WebLogger.PREF_KEY_MESSAGES";
    private static final String URL = "https://script.google.com/macros/s/AKfycbwXqgqKd4DhM6iV5PoaqNlwdw5W1o5s3YBau5Exgh0HCR8JRzzF/exec";
    private final SharedPreferences preferences;
    private final DeviceServices deviceServices;

    public WebLogger(Context base) {
        super(base);
        deviceServices = new DeviceServices(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void logMessage(Message message) {
        ArrayList<Message> messages = getMessages();
        message.setEpochMs(System.currentTimeMillis());
        message.setId(ANDROID_ID);
        messages.add(message);
        preferences.edit().putString(PREF_KEY_MESSAGES, objectToString(messages)).apply();
    }

    public boolean isEmpty() {
        return getMessages().isEmpty();
    }

    public void trySendingMessages() {
        if (deviceServices.isWifiConnected()) {
            sendMessages();
        } else {
            Log.w("WebLogger", "Tried uploading logs but there is no connection");
        }
    }

    void sendMessages() {
        String messages = jsonStringify(getMessages());
        new LogAsyncTask(URL, preferences).execute(messages);
    }

    private String jsonStringify(ArrayList<Message> messages) {
        StringBuilder json = new StringBuilder("[");
        String prefix = "";
        for (Message message : messages) {
            json.append(prefix).append(message.toJson());
            prefix = ",";
        }
        json.append("]");
        return json.toString();
    }

    @SuppressWarnings("unchecked")
    ArrayList<Message> getMessages() {
        return (ArrayList<Message>) stringToObject(preferences.getString(PREF_KEY_MESSAGES, objectToString(new ArrayList<Message>())));
    }

    static String objectToString(Serializable object) {
        String encoded = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            encoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (IOException e) {
            Log.e("WebLogger", "Error during message encoding", e);
        }
        return encoded;
    }

    private static Serializable stringToObject(String string) {
        byte[] bytes = Base64.decode(string, 0);
        Serializable object = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            object = (Serializable) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            Log.e("WebLogger", "Error during message decoding", e);
        }
        return object;
    }

}
