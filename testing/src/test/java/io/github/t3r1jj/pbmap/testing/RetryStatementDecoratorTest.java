package io.github.t3r1jj.pbmap.testing;

import org.junit.Test;
import org.junit.runner.Description;

public class RetryStatementDecoratorTest {

    @Test
    public void evaluateSuccess() throws Throwable {
        RetryStatementDecorator retryStatement = new RetryStatementDecorator(new FailureLimitStatement(0), Description.EMPTY, 1);
        retryStatement.evaluate();
    }

    @Test(expected = Throwable.class)
    public void evaluateFailure() throws Throwable {
        RetryStatementDecorator retryStatement = new RetryStatementDecorator(new FailureLimitStatement(1), Description.EMPTY, 1);
        retryStatement.evaluate();
    }

    @Test
    public void evaluateRetrySuccess() throws Throwable {
        RetryStatementDecorator retryStatement = new RetryStatementDecorator(new FailureLimitStatement(2), Description.EMPTY, 3);
        retryStatement.evaluate();
        retryStatement.evaluate();
        retryStatement.evaluate();
    }

    @Test(expected = Throwable.class)
    public void evaluateRetryFailure() throws Throwable {
        RetryStatementDecorator retryStatement = new RetryStatementDecorator(new FailureLimitStatement(3), Description.EMPTY, 3);
        retryStatement.evaluate();
        retryStatement.evaluate();
        retryStatement.evaluate();
    }

}