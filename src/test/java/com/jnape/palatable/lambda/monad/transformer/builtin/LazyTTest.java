package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.monad.transformer.builtin.LazyT.lazyT;
import static com.jnape.palatable.lambda.monad.transformer.builtin.LazyT.pureLazyT;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class LazyTTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
    public LazyT<Maybe<?>, Integer> testSubject() {
        return lazyT(just(lazy(1)));
    }

    @Test
    public void lazyZip() {
        assertEquals(lazyT(just(lazy(2))),
                     lazyT(just(lazy(1)))
                             .lazyZip(lazy(lazyT(just(lazy(x -> x + 1))))).value());
        assertEquals(lazyT(nothing()),
                     lazyT(nothing()).lazyZip(lazy(() -> {
                         throw new AssertionError();
                     })).value());
    }

    @Test
    public void staticPure() {
        LazyT<Identity<?>, Integer> lazyT = pureLazyT(pureIdentity()).apply(1);
        assertEquals(lazyT(new Identity<>(lazy(1))), lazyT);
    }
}