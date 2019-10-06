package io.github.t3r1jj.pbmap.testing;

import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class VerifyAnswer implements Answer {
    private boolean called;
    private final Answer defaultAnswer;

    public VerifyAnswer() {
        this(Answers.CALLS_REAL_METHODS);
    }

    public VerifyAnswer(Answer answer) {
        this.defaultAnswer = answer;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        called = true;
        return defaultAnswer.answer(invocation);
    }

    public void assertCalled() {
        assertTrue(called);
    }

    public void assertNotCalled() {
        assertFalse(called);
    }
}
