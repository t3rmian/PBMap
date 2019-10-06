package io.github.t3r1jj.pbmap.logging;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Starts the {@link WebLogger} and if not empty, tries to send messages
 */
public class ConnectionChangeBroadcastReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        WebLogger webLogger = new WebLogger(context);
        if (!webLogger.isEmpty()) {
            webLogger.trySendingMessages();
        }
    }
}
