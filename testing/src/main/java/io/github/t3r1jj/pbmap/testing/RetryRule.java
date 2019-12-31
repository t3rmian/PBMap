package io.github.t3r1jj.pbmap.testing;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RetryRule extends ProfiledTestWatcher {

    private final int retryCount;

    public RetryRule(int retryCount) {
        this.retryCount = retryCount;
    }

    public Statement apply(Statement base, Description description) {
        return new RetryStatementDecorator(base, description, retryCount);
    }

}
