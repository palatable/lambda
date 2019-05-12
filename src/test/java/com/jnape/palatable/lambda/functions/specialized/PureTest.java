package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.adt.Maybe;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static org.junit.Assert.assertEquals;

public class PureTest {

    @Test
    @SuppressWarnings("RedundantTypeArguments")
    public void inference() {
        assertEquals(just(1), Pure.<Maybe<?>>pure(Maybe::just).<Integer, Maybe<Integer>>apply(1));
    }
}