package com.jnape.palatable.lambda.monad;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.Equivalence;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;

import java.util.concurrent.CountDownLatch;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Times.times;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.monad.SafeT.safeT;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.junit.Assert.assertEquals;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static testsupport.traits.Equivalence.equivalence;

@RunWith(Traits.class)
public class SafeTTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, MonadRecLaws.class})
    public Equivalence<SafeT<Identity<?>, Integer>> testSubject() {
        return equivalence(safeT(new Identity<>(1)), SafeT::<Identity<Integer>>runSafeT);
    }

    @Test
    public void stackSafelyComposesMonadRecs() {
        assertEquals(STACK_EXPLODING_NUMBER,
                     times(STACK_EXPLODING_NUMBER, f -> f.fmap(x -> x + 1), safeT(Id.<Integer>id()))
                             .<Fn1<Integer, Integer>>runSafeT()
                             .apply(0));
    }

    @Test
    public void flatMapStackSafety() {
        assertEquals(new Identity<>(STACK_EXPLODING_NUMBER),
                     times(STACK_EXPLODING_NUMBER,
                           safeT -> safeT.flatMap(x -> safeT(new Identity<>(x + 1))),
                           safeT(new Identity<>(0)))
                             .runSafeT());
    }

    @Test
    public void zipStackSafety() {
        assertEquals(new Identity<>(STACK_EXPLODING_NUMBER),
                     times(STACK_EXPLODING_NUMBER,
                           safeT -> safeT.zip(safeT(new Identity<>(x -> x + 1))),
                           safeT(new Identity<>(0))).runSafeT());

    }

    @Test(timeout = 500)
    public void compositionallyPreservesZip() {
        CountDownLatch latch = new CountDownLatch(2);
        IO<Unit> countDownAndAwait = io(() -> {
            latch.countDown();
            latch.await();
        });

        safeT(countDownAndAwait)
                .discardL(safeT(countDownAndAwait))
                .<IO<Unit>>runSafeT()
                .unsafePerformAsyncIO(newFixedThreadPool(2))
                .join();
    }
}