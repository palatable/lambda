package com.jnape.palatable.lambda.optics.prisms;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static testsupport.assertion.PrismAssert.assertPrismLawfulness;

public class EitherPrismTest {

    @Test
    public void _right() {
        assertPrismLawfulness(EitherPrism._right(),
                              asList(left("foo"), right(1)),
                              singleton(1));
    }

    @Test
    public void _left() {
        assertPrismLawfulness(EitherPrism._left(),
                              asList(left("foo"), right(1)),
                              singleton("foo"));
    }
}