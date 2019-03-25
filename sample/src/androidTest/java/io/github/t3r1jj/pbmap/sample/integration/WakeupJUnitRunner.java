package io.github.t3r1jj.pbmap.sample.integration;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

import androidx.test.runner.AndroidJUnitRunner;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;
import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;

public class WakeupJUnitRunner extends AndroidJUnitRunner {
    @Override
    public void onStart() {
        runOnMainSync(() -> {
            Context context = WakeupJUnitRunner.this.getTargetContext();
            unlockScreen(context);
            keepScreenOn(context);
        });
        ActivityLifecycleMonitorRegistry.getInstance().addLifecycleCallback((activity, stage) -> {
            if (stage == Stage.PRE_ON_CREATE) {
                activity.getWindow().addFlags(FLAG_DISMISS_KEYGUARD | FLAG_TURN_SCREEN_ON | FLAG_KEEP_SCREEN_ON);
            }
        });
        super.onStart();
    }

    private void keepScreenOn(Context context) {
        PowerManager power = (PowerManager) context.getSystemService(POWER_SERVICE);
        power.newWakeLock(FULL_WAKE_LOCK | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, getClass().getSimpleName())
                .acquire();
    }

    private void unlockScreen(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = km.newKeyguardLock(getClass().getSimpleName());
        keyguardLock.disableKeyguard();
    }
}