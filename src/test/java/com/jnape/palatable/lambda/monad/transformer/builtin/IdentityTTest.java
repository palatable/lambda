package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
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
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IdentityT.identityT;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class IdentityTTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
    public IdentityT<Maybe<?>, Integer> testSubject() {
        return identityT(just(new Identity<>(1)));
    }

    @Test
    public void lazyZip() {
        assertEquals(identityT(just(new Identity<>(2))),
                     identityT(just(new Identity<>(1)))
                             .lazyZip(lazy(identityT(just(new Identity<>(x -> x + 1))))).value());
        assertEquals(identityT(nothing()),
                     identityT(nothing()).lazyZip(lazy(() -> {
                         throw new AssertionError();
                     })).value());
    }
}