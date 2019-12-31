package io.github.t3r1jj.pbmap.logging;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

class LogAsyncTask extends AsyncTask<String, Void, String> {

    private final WeakReference<SharedPreferences> preferencesReference;
    private final String url;

    LogAsyncTask(String url, SharedPreferences preferencesReference) {
        this.url = url;
        this.preferencesReference = new WeakReference<>(preferencesReference);
    }

    @Override
    protected String doInBackground(String... params) {
        String data = params[0];
        String result = "";
        try {
            Log.i("WebLogger", "Initiating log upload");
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));
            writer.write(data);
            writer.close();
            outputStream.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            result = sb.toString();
            urlConnection.disconnect();

        } catch (IOException e) {
            Log.e("WebLogger", "Error during log upload", e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if ("success".equals(result)) {
            SharedPreferences preferences = preferencesReference.get();
            if (preferences != null) {
                preferences.edit().putString(WebLogger.PREF_KEY_MESSAGES, WebLogger.objectToString(new ArrayList<>())).apply();
                Log.i("WebLogger", "Successfully uploaded log");
                return;
            }
        }
        Log.e("WebLogger", "Log not uploaded with result: " + result);
    }
}
