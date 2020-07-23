package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.BifunctorLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.monad.transformer.builtin.EitherT.eitherT;
import static com.jnape.palatable.lambda.monad.transformer.builtin.EitherT.liftEitherT;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;
import static testsupport.assertion.MonadErrorAssert.assertLaws;

@RunWith(Traits.class)
public class EitherTTest {

    @TestTraits({
            FunctorLaws.class,
            ApplicativeLaws.class,
            MonadLaws.class,
            BifunctorLaws.class,
            MonadRecLaws.class})
    public Subjects<EitherT<Identity<?>, String, Integer>> testSubject() {
        return subjects(eitherT(new Identity<>(left("foo"))), eitherT(new Identity<>(right(1))));
    }

    @Test
    public void lazyZip() {
        assertEquals(eitherT(just(right(2))),
                     eitherT(just(right(1))).lazyZip(lazy(eitherT(just(right(x -> x + 1))))).value());
        assertEquals(eitherT(nothing()),
                     eitherT(nothing()).lazyZip(lazy(() -> {
                         throw new AssertionError();
                     })).value());
    }

    @Test(timeout = 500)
    public void composedZip() {
        CountDownLatch latch = new CountDownLatch(2);
        IO<Unit> countdownAndAwait = io(() -> {
            latch.countDown();
            latch.await();
        });
        EitherT<IO<?>, Object, Unit> lifted = liftEitherT().apply(countdownAndAwait);
        lifted.discardL(lifted)
                .<IO<Either<Object, Unit>>>runEitherT()
                .unsafePerformAsyncIO(Executors.newFixedThreadPool(2))
                .join();
    }

    @Test
    public void staticPure() {
        EitherT<Identity<?>, String, Integer> eitherT = EitherT.<Identity<?>, String>pureEitherT(pureIdentity())
                .apply(1);
        assertEquals(eitherT(new Identity<>(right(1))), eitherT);
    }

    @Test
    public void monadError() {
        assertLaws(subjects(eitherT(new Identity<>(right(1))),
                            eitherT(new Identity<>(left("")))),
                   "bar",
                   str -> eitherT(new Identity<>(right(str.length()))));
    }
}