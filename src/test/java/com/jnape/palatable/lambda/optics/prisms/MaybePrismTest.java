package com.jnape.palatable.lambda.optics.prisms;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static testsupport.assertion.PrismAssert.assertPrismLawfulness;

public class MaybePrismTest {

    @Test
    public void _just() {
        assertPrismLawfulness(MaybePrism._just(),
                              asList(just(1), nothing()),
                              singleton(1));
    }

    @Test
    public void _nothing() {
        assertPrismLawfulness(MaybePrism._nothing(),
                              asList(just(1), nothing()),
                              singleton(UNIT));
    }
}