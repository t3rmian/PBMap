package io.github.t3r1jj.pbmap.testing;

import android.util.Log;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class ProfiledTestWatcher extends TestWatcher {
    private static final String TAG = ProfiledTestWatcher.class.getSimpleName();
    private long startTime;

    @Override
    protected void starting(Description description) {
        super.starting(description);
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void finished(Description description) {
        super.finished(description);
        String testName = description.getTestClass().getSimpleName() + "-" + description.getMethodName();
        Log.i(TAG, String.format("%s took %dms", testName, (System.currentTimeMillis() - startTime)));
    }
}