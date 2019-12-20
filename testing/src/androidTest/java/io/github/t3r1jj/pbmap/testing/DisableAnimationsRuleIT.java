package io.github.t3r1jj.pbmap.testing;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

import static org.junit.Assert.assertArrayEquals;

@RunWith(AndroidJUnit4.class)
public class DisableAnimationsRuleIT {
    private String[] originalSettings;

    @Test
    @SmallTest
    public void apply() {
        DisableAnimationsRule rule = new DisableAnimationsRule();
        assertArrayEquals(new String[]{"0", "0", "0"}, rule.previousAnimationSettings);
        rule.apply(new Statement() {
            @Override
            public void evaluate() {
                originalSettings = rule.previousAnimationSettings.clone();
                Log.d(DisableAnimationsRuleIT.class.getName(), "evaulate");
            }
        }, Description.EMPTY);
        assertArrayEquals(new String[]{"0", "0", "0"}, rule.previousAnimationSettings);
        rule.apply(new Statement() {
            @Override
            public void evaluate() {
                assertArrayEquals(originalSettings, rule.previousAnimationSettings);
                Log.d(DisableAnimationsRuleIT.class.getName(), "evaulate");
            }
        }, Description.EMPTY);

    }
}