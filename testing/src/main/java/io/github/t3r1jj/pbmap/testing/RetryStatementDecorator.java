package io.github.t3r1jj.pbmap.testing;

import android.util.Log;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Locale;

class RetryStatementDecorator extends Statement {

    private static final String TAG = RetryStatementDecorator.class.getSimpleName();

    private final int tryLimit;
    private final Statement base;
    private final Description description;

    RetryStatementDecorator(Statement base, Description description, int tryLimit) {
        this.base = base;
        this.description = description;
        this.tryLimit = tryLimit;
    }

    @Override
    public void evaluate() throws Throwable {
        Throwable caughtThrowable = null;

        for (int i = 0; i < tryLimit; i++) {
            try {
                base.evaluate();
                return;
            } catch (Throwable t) {
                caughtThrowable = t;
                Log.w(TAG, String.format(Locale.getDefault(), "%s: run %d failed", description.getDisplayName(), (i + 1)));
            }
        }
        Log.w(TAG, String.format(Locale.getDefault(), "%s: giving up after %d failures", description.getDisplayName(), tryLimit));
        //noinspection ConstantConditions
        throw caughtThrowable;
    }

}
