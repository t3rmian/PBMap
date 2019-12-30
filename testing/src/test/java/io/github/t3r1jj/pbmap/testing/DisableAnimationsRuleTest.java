package io.github.t3r1jj.pbmap.testing;

import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class DisableAnimationsRuleTest {
    private String[] originalSettings;

    @Test
    @PrepareForTest(value = {InstrumentationRegistry.class, UiDevice.class})
    public void apply() throws Throwable {
        mockStatic(InstrumentationRegistry.class);
        Instrumentation instrumentation = mock(Instrumentation.class);
        when(InstrumentationRegistry.getInstrumentation()).thenReturn(instrumentation);
        mockStatic(UiDevice.class);
        UiDevice device = mock(UiDevice.class);
        when(UiDevice.getInstance(any())).thenReturn(device);

        CallArgumentMatcher queueMock = new CallArgumentMatcher();
        when(device.executeShellCommand(argThat(queueMock))).then(invocation -> (queueMock.getResult()));

        DisableAnimationsRule rule = new DisableAnimationsRule();
        assertArrayEquals(new String[]{"0", "0", "0"}, rule.previousAnimationSettings);
        rule.apply(new Statement() {
            @Override
            public void evaluate() {
                assertEquals("0", queueMock.getResult());
                assertEquals("0", queueMock.getResult());
                assertEquals("0", queueMock.getResult());
                originalSettings = rule.previousAnimationSettings.clone();
                queueMock.queue.add("0");
                queueMock.queue.add("0");
                queueMock.queue.add("0");
            }
        }, Description.EMPTY).evaluate();
        assertArrayEquals(new String[]{"0", "0", "0"}, rule.previousAnimationSettings);
        assertArrayEquals(new String[]{"1", "2", "3"}, originalSettings);
    }

    private static class CallArgumentMatcher implements ArgumentMatcher<String> {
        Queue<String> queue = new LinkedList<>();

        private CallArgumentMatcher() {
            queue.addAll(Arrays.asList("1", "2", "3"));
        }

        @Override
        public boolean matches(String argument) {
            if (argument.contains("set")) {
                String[] args = argument.split(" ");
                queue.add(args[args.length - 1]);
            }
            return true;
        }

        String getResult() {
            return queue.remove();
        }
    }
}