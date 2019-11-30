package io.github.t3r1jj.pbmap.testing;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RetryRuleTest {

    @Test
    public void applySuccess() throws Throwable {
        Statement statement = new RetryRule(1).apply(new FailureLimitStatement(0), Description.EMPTY);
        statement.evaluate();
    }

    @Test(expected = Throwable.class)
    public void applyFailure() throws Throwable {
        Statement statement = new RetryRule(1).apply(new FailureLimitStatement(1), Description.EMPTY);
        statement.evaluate();
    }

    @Test
    public void applyRetrySuccess() throws Throwable {
        Statement statement = new RetryRule(3).apply(new FailureLimitStatement(2), Description.EMPTY);
        statement.evaluate();
        statement.evaluate();
        statement.evaluate();
    }

    @Test(expected = Throwable.class)
    public void applyRetryFailure() throws Throwable {
        Statement statement = new RetryRule(3).apply(new FailureLimitStatement(3), Description.EMPTY);
        statement.evaluate();
        statement.evaluate();
        statement.evaluate();
    }
}