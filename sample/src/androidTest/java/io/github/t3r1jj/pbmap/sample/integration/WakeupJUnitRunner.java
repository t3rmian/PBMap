package io.github.t3r1jj.pbmap.sample.integration;

import android.os.Bundle;

import androidx.test.runner.AndroidJUnitRunner;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;

public class WakeupJUnitRunner extends AndroidJUnitRunner {
    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);

        ActivityLifecycleMonitorRegistry.getInstance().addLifecycleCallback((activity, stage) -> {
            if (stage == Stage.PRE_ON_CREATE) {
                activity.getWindow().addFlags(FLAG_DISMISS_KEYGUARD | FLAG_TURN_SCREEN_ON | FLAG_KEEP_SCREEN_ON);
            }
        });
    }
}