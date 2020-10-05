package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.monad.transformer.builtin.ReaderT.readerT;
import static org.junit.Assert.assertEquals;
import static testsupport.traits.Equivalence.equivalence;

@RunWith(Traits.class)
public class ReaderTTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, MonadReaderLaws.class, MonadRecLaws.class})
    public Equivalence<ReaderT<Integer, Identity<?>, Integer>> testSubject() {
        return equivalence(readerT(Identity::new), readerT -> readerT.runReaderT(1));
    }

    @Test
    public void profunctor() {
        assertEquals(new Identity<>(4),
                     ReaderT.<Integer, Identity<?>, Integer>readerT(Identity::new)
                             .diMap(String::length, x -> x + 1)
                             .runReaderT("123"));
    }

    @Test
    public void local() {
        assertEquals(new Identity<>(2),
                     ReaderT.<Integer, Identity<?>, Integer>readerT(Identity::new)
                             .local(x -> x + 1)
                             .runReaderT(1));
    }

    @Test
    public void mapReaderT() {
        assertEquals(just(3),
                     ReaderT.<String, Identity<?>, String>readerT(Identity::new)
                             .<Identity<String>, Maybe<?>, Integer>mapReaderT(id -> just(id.runIdentity().length()))
                             .runReaderT("foo"));
    }

    @Test
    public void andComposesLeftToRight() {
        ReaderT<Integer, Identity<?>, Float> intToFloat    = readerT(x -> new Identity<>(x.floatValue()));
        ReaderT<Float, Identity<?>, Double>  floatToDouble = readerT(f -> new Identity<>(f.doubleValue()));

        assertEquals(new Identity<>(1.),
                     intToFloat.and(floatToDouble).runReaderT(1));
    }

    @Test
    public void staticPure() {
        ReaderT<String, Identity<?>, Integer> readerT =
                ReaderT.<String, Identity<?>>pureReaderT(pureIdentity()).apply(1);
        assertEquals(new Identity<>(1), readerT.runReaderT("foo"));
    }

    @Test(timeout = 500)
    public void composedZip() {
        CountDownLatch latch = new CountDownLatch(2);
        IO<Unit> countdownAndAwait = io(() -> {
            latch.countDown();
            latch.await();
        });
        ReaderT<Unit, IO<?>, Unit> lifted = ReaderT.<Unit>liftReaderT().apply(countdownAndAwait);
        lifted.discardL(lifted)
                .<IO<Unit>>runReaderT(UNIT)
                .unsafePerformAsyncIO(Executors.newFixedThreadPool(2))
                .join();
    }

    @Test
    public void fmapInteractions() {
        AtomicInteger invocations = new AtomicInteger(0);
        ReaderT<Integer, Identity<?>, Integer> readerT = readerT(i -> {
            invocations.incrementAndGet();
            return new Identity<>(i);
        });

        Fn1<Integer, Integer> plusOne = x -> x + 1;
        readerT.fmap(plusOne).fmap(plusOne).fmap(plusOne).runReaderT(0);
        assertEquals(1, invocations.get());
    }
}