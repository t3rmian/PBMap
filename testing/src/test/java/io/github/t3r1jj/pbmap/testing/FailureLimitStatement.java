package io.github.t3r1jj.pbmap.testing;

import org.junit.runners.model.Statement;

class FailureLimitStatement extends Statement {
    private final int failureLimit;
    private int failureCount;

    FailureLimitStatement(int failureLimit) {
        this.failureLimit = failureLimit;
    }

    @Override
    public void evaluate() {
        if (failureCount++ < failureLimit) {
            throw new RuntimeException("Reached failure limit");
        }
    }
}
