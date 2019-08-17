package com.jnape.palatable.lambda.functions.builtin.fn2;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Until.until;
import static com.jnape.palatable.lambda.io.IO.io;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IOMatcher.yieldsValue;

public class UntilTest {

    @Test
    public void repeatedlyExecutesUntilPredicateMatches() {
        AtomicInteger counter = new AtomicInteger(0);
        assertThat(until(x -> x == 10, io(counter::getAndIncrement)),
                   yieldsValue(equalTo(10)));
    }

    @Test
    public void predicateThatImmediatelyMatchesDoesNotChangeIO() {
        assertThat(until(constantly(true), io(0)), yieldsValue(equalTo(0)));
    }
}