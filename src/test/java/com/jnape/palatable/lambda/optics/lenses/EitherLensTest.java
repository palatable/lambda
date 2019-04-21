package com.jnape.palatable.lambda.optics.lenses;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.optics.Lens;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.optics.functions.Set.set;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static org.junit.Assert.assertEquals;

public class EitherLensTest {

    @Test
    public void rightFocusesOnRightValues() {
        Lens.Simple<Either<String, Integer>, Maybe<Integer>> right = EitherLens.right();

        assertEquals(just(1), view(right, right(1)));
        assertEquals(nothing(), view(right, left("fail")));
        assertEquals(right(2), set(right, just(2), right(1)));
        assertEquals(right(1), set(right, nothing(), right(1)));
        assertEquals(right(2), set(right, just(2), left("fail")));
        assertEquals(left("fail"), set(right, nothing(), left("fail")));
    }

    @Test
    public void leftFocusesOnLeftValues() {
        Lens.Simple<Either<String, Integer>, Maybe<String>> left = EitherLens.left();

        assertEquals(just("fail"), view(left, left("fail")));
        assertEquals(nothing(), view(left, right(1)));
        assertEquals(left("foo"), set(left, just("foo"), left("fail")));
        assertEquals(left("fail"), set(left, nothing(), left("fail")));
        assertEquals(left("foo"), set(left, just("foo"), right(1)));
        assertEquals(right(1), set(left, nothing(), right(1)));
    }
}