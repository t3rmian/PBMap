package io.github.t3r1jj.pbmap.testing;

import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;

public class DisableAnimationsRule implements TestRule {

    final String[] previousAnimationSettings = new String[]{"0", "0", "0"};

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                changeAnimationSettings(previousAnimationSettings.clone());
                try {
                    base.evaluate();
                } finally {
                    changeAnimationSettings(previousAnimationSettings.clone());
                }
            }
        };
    }

    private void changeAnimationSettings(String[] animationSettings) throws IOException {
        previousAnimationSettings[0] = execute("settings get global transition_animation_scale").trim();
        previousAnimationSettings[1] = execute("settings get global transition_animation_scale").trim();
        previousAnimationSettings[2] = execute("settings get global transition_animation_scale").trim();
        execute("settings put global transition_animation_scale " + animationSettings[0]);
        execute("settings put global window_animation_scale " + animationSettings[0]);
        execute("settings put global animator_duration_scale " + animationSettings[0]);
    }

    private String execute(String command) throws IOException {
        Log.d(DisableAnimationsRule.class.getName(), command);
        return UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .executeShellCommand(command);
    }

}
