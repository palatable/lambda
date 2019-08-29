package com.jnape.palatable.lambda.io;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;
import static com.jnape.palatable.lambda.io.IO.memoize;
import static com.jnape.palatable.lambda.io.MVar.newMVar;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IOMatcher.yieldsValue;

public class MVarTest {

    @Test(timeout = 100)
    public void putTake() {
        assertThat(MVar.<String>newMVar()
                           .flatMap(mvar -> mvar.put("foo"))
                           .flatMap(MVar::take),
                   yieldsValue(equalTo("foo")));
    }

    @Test(timeout = 100)
    public void putPutTakeTake() {
        assertEquals(tuple(1, 2),
                     memoize(MVar.<Integer>newMVar())
                             .flatMap(mvar -> mvar.put(1)
                                                  .flatMap(mv1 -> mv1.put(2))
                                                  .discardL(mvar.take()
                                                                .fmap(tupler(mvar))
                                                                .flatMap(
                                                                        into((mv, i) -> mv.take()
                                                                                          .fmap(tupler(i))))))
                             .unsafePerformAsyncIO(newFixedThreadPool(1))
                             .join());
    }

    @Test
    public void initializeWithValue() {
        assertThat(newMVar(1).flatMap(MVar::take),
                   yieldsValue(equalTo(1)));
    }
}