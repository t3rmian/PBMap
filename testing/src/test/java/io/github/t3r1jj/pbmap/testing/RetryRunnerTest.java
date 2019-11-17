package io.github.t3r1jj.pbmap.testing;

import android.app.Instrumentation;

import androidx.test.internal.runner.RunnerArgs;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {InstrumentationRegistry.class, AndroidJUnit4ClassRunner.class, BlockJUnit4ClassRunner.class})
public class RetryRunnerTest {

    private static int tryCountBackup;

    private RunNotifier notifier;
    private RetryRunner runner;

    @BeforeClass
    public static void backupTryCount() {
        tryCountBackup = BuildConfig.IT_TEST_TRY_LIMIT;
    }

    @AfterClass
    public static void restoreTryCountBackup() throws Exception {
        setTryCount(tryCountBackup);
    }

    @Before
    public void setUp() throws Exception {
        mockStatic(InstrumentationRegistry.class);
        Instrumentation instrumentation = PowerMockito.mock(Instrumentation.class);
        when(InstrumentationRegistry.getInstrumentation()).thenReturn(instrumentation);
        RunnerArgs args = new RunnerArgs.Builder().build();
        RunnerArgs.Builder builder = PowerMockito.mock(RunnerArgs.Builder.class);
        whenNew(RunnerArgs.Builder.class).withNoArguments().thenReturn(builder);
        whenNew(RunnerArgs.Builder.class).withAnyArguments().thenReturn(builder);
        when(builder.fromBundle(any(), any())).thenReturn(builder);
        when(builder.build()).thenReturn(args);
        notifier = mock(RunNotifier.class);
        runner = spy(new RetryRunner(this.getClass()));
    }

    @Test
    public void testSuccessClassBlock() throws Throwable {
        setTryCount(1);
        mockSuperCassBlockWithFailureStatement(runner, 0);
        runner = new RetryRunner(RetryStatementDecoratorTest.class);
        Statement statement = runner.classBlock(notifier);
        statement.evaluate();
        verify(notifier, times(0)).fireTestFailure(any());
    }

    @Test
    public void testSuccessMethodBlock() throws Throwable {
        setTryCount(1);
        mockSuperMethodBlockWithFailureStatement(runner, 0);
        runner = new RetryRunner(RetryStatementDecoratorTest.class);
        Statement statement = runner.methodBlock(getAnyTestMethod());
        statement.evaluate();
        verify(notifier, times(0)).fireTestFailure(any());
    }

    @Test(expected = Throwable.class)
    public void testFailureClassBlock() throws Throwable {
        setTryCount(1);
        mockSuperCassBlockWithFailureStatement(runner, 1);
        runner = new RetryRunner(RetryStatementDecoratorTest.class);
        Statement statement = runner.classBlock(notifier);
        statement.evaluate();
        verify(notifier).fireTestFailure(any());
    }

    @Test(expected = Throwable.class)
    public void testFailureMethodBlock() throws Throwable {
        setTryCount(1);
        mockSuperMethodBlockWithFailureStatement(runner, 1);
        runner = new RetryRunner(RetryStatementDecoratorTest.class);
        Statement statement = runner.methodBlock(getAnyTestMethod());
        statement.evaluate();
        verify(notifier).fireTestFailure(any());
    }

    @Test
    public void testSuccessClassBlockRetry() throws Throwable {
        setTryCount(3);
        mockSuperCassBlockWithFailureStatement(runner, 2);
        runner = new RetryRunner(RetryStatementDecoratorTest.class);
        Statement statement = runner.classBlock(notifier);
        statement.evaluate();
        statement.evaluate();
        statement.evaluate();
        verify(notifier, times(0)).fireTestFailure(any());
    }

    @Test
    public void testSuccessMethodBlockRetry() throws Throwable {
        setTryCount(3);
        mockSuperMethodBlockWithFailureStatement(runner, 2);
        runner = new RetryRunner(RetryStatementDecoratorTest.class);
        Statement statement = runner.methodBlock(getAnyTestMethod());
        statement.evaluate();
        statement.evaluate();
        statement.evaluate();
        verify(notifier, times(0)).fireTestFailure(any());
    }

    @Test(expected = Throwable.class)
    public void testFailureClassBlockRetry() throws Throwable {
        mockSuperCassBlockWithFailureStatement(runner, 3);
        runner = new RetryRunner(RetryStatementDecoratorTest.class);
        Statement statement = runner.classBlock(notifier);
        statement.evaluate();
        statement.evaluate();
        statement.evaluate();
        verify(notifier).fireTestFailure(any());
    }

    @Test(expected = Throwable.class)
    public void testFailureMethodBlockRetry() throws Throwable {
        mockSuperMethodBlockWithFailureStatement(runner, 3);
        runner = new RetryRunner(RetryStatementDecoratorTest.class);
        Statement statement = runner.methodBlock(getAnyTestMethod());
        statement.evaluate();
        statement.evaluate();
        statement.evaluate();
        verify(notifier).fireTestFailure(any());
    }

    private FrameworkMethod getAnyTestMethod() {
        return runner.getTestClass().getAnnotatedMethods(Test.class).get(0);
    }

    private static void setTryCount(int tryCount) throws Exception {
        Field field = BuildConfig.class.getField("IT_TEST_TRY_LIMIT");
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, tryCount);
    }

    private void mockSuperCassBlockWithFailureStatement(AndroidJUnit4ClassRunner retryRunner,
                                                        int failureLimit) throws Exception {
        PowerMockito.doReturn(new FailureLimitStatement(failureLimit))
                .when(retryRunner, "classBlock", any());
    }

    private void mockSuperMethodBlockWithFailureStatement(BlockJUnit4ClassRunner retryRunner,
                                                          int failureLimit) throws Exception {
        PowerMockito.doReturn(new FailureLimitStatement(failureLimit))
                .when(retryRunner, "methodBlock", any());
    }
}