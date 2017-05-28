package io.github.t3r1jj.pbmap.logging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConnectionChangeBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WebLogger webLogger = new WebLogger(context);
        if (!webLogger.isEmpty()) {
            webLogger.trySendingMessages();
        }
    }
}
