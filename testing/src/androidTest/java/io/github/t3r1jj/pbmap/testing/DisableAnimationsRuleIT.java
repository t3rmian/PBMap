package io.github.t3r1jj.pbmap.testing;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DisableAnimationsRuleIT {
    private String[] originalSettings;

    @Test
    @SmallTest
    public void apply() throws Throwable {
        AtomicInteger evaluationCount = new AtomicInteger();
        DisableAnimationsRule rule = new DisableAnimationsRule();
        assertArrayEquals(new String[]{"0", "0", "0"}, rule.previousAnimationSettings);
        rule.apply(new Statement() {
            @Override
            public void evaluate() {
                originalSettings = rule.previousAnimationSettings.clone();
                evaluationCount.incrementAndGet();
            }
        }, Description.EMPTY).evaluate();
        assertArrayEquals(new String[]{"0", "0", "0"}, rule.previousAnimationSettings);
        rule.apply(new Statement() {
            @Override
            public void evaluate() {
                assertArrayEquals(originalSettings, rule.previousAnimationSettings);
                evaluationCount.incrementAndGet();
            }
        }, Description.EMPTY).evaluate();
        assertEquals(2, evaluationCount.get());
    }
}