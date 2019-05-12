package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT.maybeT;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class MaybeTTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
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
}