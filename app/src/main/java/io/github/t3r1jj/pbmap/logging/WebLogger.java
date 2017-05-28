package io.github.t3r1jj.pbmap.logging;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WebLogger extends ContextWrapper {

    static final String PREF_KEY_MESSAGES = "io.github.t3r1jj.pbmap.logging.WebLogger.PREF_KEY_MESSAGES";
    private static final String URL = "https://script.google.com/macros/s/AKfycbwXqgqKd4DhM6iV5PoaqNlwdw5W1o5s3YBau5Exgh0HCR8JRzzF/exec";
    private final SharedPreferences preferences;

    public WebLogger(Context base) {
        super(base);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void logMessage(Message message) {
        ArrayList<Message> messages = getMessages();
        message.setEpochMs(System.currentTimeMillis());
        message.setId(Build.SERIAL);
        messages.add(message);
        preferences.edit().putString(PREF_KEY_MESSAGES, objectToString(messages)).apply();
    }

    public boolean isEmpty() {
        return getMessages().isEmpty();
    }

    public void trySendingMessages() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isConnected()) {
            sendMessages();
        }
    }

    void sendMessages() {
        String messages = jsonStringify(getMessages());

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                String data = params[0];
                String result = "";
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) ((new URL(URL).openConnection()));
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.connect();

                    OutputStream outputStream = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    writer.write(data);
                    writer.close();
                    outputStream.close();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();
                    result = sb.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if ("success".equals(result)) {
                    preferences.edit().putString(PREF_KEY_MESSAGES, objectToString(new ArrayList<>())).apply();
                }
            }
        }.execute(messages);
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

    private String objectToString(Serializable object) {
        String encoded = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            encoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoded;
    }

    private Serializable stringToObject(String string) {
        byte[] bytes = Base64.decode(string, 0);
        Serializable object = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            object = (Serializable) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
        }
        return object;
    }
}
