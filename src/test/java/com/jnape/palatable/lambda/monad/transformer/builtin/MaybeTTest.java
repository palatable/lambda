package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn2.GT.gt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT.*;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class MaybeTTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, MonadRecLaws.class})
    public Subjects<MaybeT<Either<String, ?>, Integer>> testSubject() {
        return subjects(maybeT(right(just(1))),
                        maybeT(right(nothing())),
                        maybeT(left("foo")));
    }

    @Test
    public void lazyZip() {
        assertEquals(maybeT(right(just(2))),
                     maybeT(right(just(1))).lazyZip(lazy(maybeT(right(just(x -> x + 1))))).value());
        assertEquals(maybeT(left("foo")),
                     maybeT(left("foo")).lazyZip(lazy(() -> {
                         throw new AssertionError();
                     })).value());
    }

    @Test
    public void staticPure() {
        MaybeT<Identity<?>, Integer> maybeT = pureMaybeT(pureIdentity()).apply(1);
        assertEquals(maybeT(new Identity<>(just(1))), maybeT);
    }

    @Test(timeout = 500)
    public void composedZip() {
        CountDownLatch latch = new CountDownLatch(2);
        IO<Unit> countdownAndAwait = io(() -> {
            latch.countDown();
            latch.await();
        });
        MaybeT<IO<?>, Unit> lifted = liftMaybeT().apply(countdownAndAwait);
        lifted.discardL(lifted)
                .<IO<Maybe<Unit>>>runMaybeT()
                .unsafePerformAsyncIO(Executors.newFixedThreadPool(2))
                .join();
    }

    @Test
    public void filter() {
        MaybeT<Identity<?>, Integer> maybeT = pureMaybeT(pureIdentity()).apply(1);
        assertEquals(maybeT(new Identity<>(just(1))), maybeT.filter(gt(0)));
        assertEquals(maybeT(new Identity<>(nothing())), maybeT.filter(lt(0)));
    }

    @Test
    public void orSelectsFirstPresentValueInsideEffect() {
        assertEquals(maybeT(new Identity<>(just(1))),
                     maybeT(new Identity<>(just(1))).or(maybeT(new Identity<>(nothing()))));

        assertEquals(maybeT(new Identity<>(just(1))),
                     maybeT(new Identity<>(nothing())).or(maybeT(new Identity<>(just(1)))));

        assertEquals(maybeT(new Identity<>(just(1))),
                     maybeT(new Identity<>(just(1))).or(maybeT(new Identity<>(just(2)))));
    }
}