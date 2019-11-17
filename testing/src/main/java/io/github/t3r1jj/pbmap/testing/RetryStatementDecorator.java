package io.github.t3r1jj.pbmap.testing;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Locale;

class RetryStatementDecorator extends Statement {

    private final int retryCount;
    private final Statement base;
    private final Description description;

    RetryStatementDecorator(Statement base, Description description, int retryCount) {
        this.base = base;
        this.description = description;
        this.retryCount = retryCount;
    }

    @Override
    public void evaluate() throws Throwable {
        Throwable caughtThrowable = null;

        for (int i = 0; i < retryCount; i++) {
            try {
                base.evaluate();
                return;
            } catch (Throwable t) {
                caughtThrowable = t;
                System.err.println(String.format(Locale.getDefault(), "%s: run %d failed", description.getDisplayName(), (i + 1)));
            }
        }
        System.err.println(String.format(Locale.getDefault(), "%s: giving up after %d failures", description.getDisplayName(), retryCount));
        //noinspection ConstantConditions
        throw caughtThrowable;
    }

}
