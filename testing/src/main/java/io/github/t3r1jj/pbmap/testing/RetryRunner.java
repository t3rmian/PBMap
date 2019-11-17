package io.github.t3r1jj.pbmap.testing;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class RetryRunner extends AndroidJUnit4ClassRunner {

    public RetryRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement classBlock(RunNotifier notifier) {
        return new RetryStatementDecorator(super.classBlock(notifier), getDescription(), BuildConfig.IT_TEST_TRY_LIMIT);
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        return new RetryStatementDecorator(super.methodBlock(method), describeChild(method), BuildConfig.IT_TEST_TRY_LIMIT);
    }

}