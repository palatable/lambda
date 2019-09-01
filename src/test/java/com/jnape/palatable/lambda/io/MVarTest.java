package com.jnape.palatable.lambda.io;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;
import static com.jnape.palatable.lambda.io.IO.*;
import static com.jnape.palatable.lambda.io.MVar.newMVar;
import static java.lang.Thread.State.WAITING;
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

    @Test(timeout = 200)
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

    @Test(timeout = 100)
    public void initializeWithValue() {
        assertThat(newMVar(1).flatMap(MVar::take),
                yieldsValue(equalTo(1)));
    }

    @Test(timeout = 200)
    public void threadWaitingIsWaiting() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        IO<MVar<Integer>> mVarIO = memoize(newMVar());

        Thread thread = new Thread(() -> mVarIO.flatMap(MVar::take)
                .flatMap(constantly(io(countDownLatch::countDown)))
                .unsafePerformIO());
        thread.start();

        Thread.sleep(100);
        assertEquals(WAITING, thread.getState());
        mVarIO.flatMap(m -> m.put(1)).unsafePerformIO();

        countDownLatch.await();
    }

    @Test(timeout = 200)
    public void emptyDoesntModifystate() {
        assertThat(newMVar().flatMap(MVar::empty), yieldsValue(equalTo(true)));
        assertThat(newMVar(1).flatMap(MVar::empty), yieldsValue(equalTo(false)));
        assertThat(newMVar(1).flatMap(mvar -> mvar.empty()
                        .fmap(constantly(mvar)))
                        .flatMap(MVar::empty),
                yieldsValue(equalTo(false)));
    }

    @Test(timeout = 200)
    public void swapsOverPreviousValue() {
        assertThat(newMVar(1)
                        .flatMap(mv -> mv.swap(2).fmap(tupler(mv)))
                        .flatMap(into((mv, old) ->
                                mv.take().fmap(tupler(old)))),
                yieldsValue(equalTo(tuple(1, 2))));
    }

    @Test(timeout = 200)
    public void withReplacesTheValueInTheMVarIfIoThrows() {
        assertThat(newMVar(1)
                        .flatMap(mv -> mv
                                .<String>with(i -> throwing(new RuntimeException("With method blew up")))
                                .catchError(t -> io(t.getMessage()))
                                .fmap(constantly(mv)))
                        .flatMap(MVar::take),
                yieldsValue(equalTo(1)));
    }

    @Test(timeout = 200)
    public void addDoesNotBlock() {
        assertThat(newMVar(1)
                        .flatMap(mv -> mv
                                .add(2)
                                .fmap(tupler(mv)))
                        .flatMap(into((mvar, added) -> mvar
                                .take()
                                .fmap(tupler(added)))),
                yieldsValue(equalTo(tuple(false, 1))));
        assertThat(newMVar()
                        .flatMap(mv -> mv
                                .add(1)
                                .fmap(tupler(mv)))
                        .flatMap(into((mvar, added) -> mvar
                                .take()
                                .fmap(tupler(added)))),
                yieldsValue(equalTo(tuple(true, 1))));
    }

    @Test(timeout = 200)
    public void pollDoesNotBlock() {
        assertThat(newMVar().flatMap(MVar::poll), yieldsValue(equalTo(nothing())));
        assertThat(newMVar(1).flatMap(MVar::poll), yieldsValue(equalTo(just(1))));
    }
}