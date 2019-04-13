package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;

import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class ComposeTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class})
    public Compose<Identity<?>, Identity<?>, Integer> testSubject() {
        return new Compose<>(new Identity<>(new Identity<>(1)));
    }

    @Test
    public void inference() {
        Either<Object, Maybe<Integer>> a = new Compose<>(right(just(1))).fmap(x -> x + 1).getCompose();
        assertEquals(right(just(2)), a);
    }
}