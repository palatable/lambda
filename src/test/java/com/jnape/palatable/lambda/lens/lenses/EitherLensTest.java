package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.lens.Lens;
import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static org.junit.Assert.assertEquals;

public class EitherLensTest {

    @Test
    public void rightFocusesOnRightValues() {
        Lens.Simple<Either<String, Integer>, Optional<Integer>> right = EitherLens.right();

        assertEquals(Optional.of(1), view(right, right(1)));
        assertEquals(Optional.empty(), view(right, left("fail")));
        assertEquals(right(2), set(right, Optional.of(2), right(1)));
        assertEquals(right(1), set(right, Optional.empty(), right(1)));
        assertEquals(right(2), set(right, Optional.of(2), left("fail")));
        assertEquals(left("fail"), set(right, Optional.empty(), left("fail")));
    }

    @Test
    public void leftFocusesOnLeftValues() {
        Lens.Simple<Either<String, Integer>, Optional<String>> left = EitherLens.left();

        assertEquals(Optional.of("fail"), view(left, left("fail")));
        assertEquals(Optional.empty(), view(left, right(1)));
        assertEquals(left("foo"), set(left, Optional.of("foo"), left("fail")));
        assertEquals(left("fail"), set(left, Optional.empty(), left("fail")));
        assertEquals(left("foo"), set(left, Optional.of("foo"), right(1)));
        assertEquals(right(1), set(left, Optional.empty(), right(1)));
    }
}