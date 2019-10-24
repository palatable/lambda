package com.jnape.palatable.lambda.functions.builtin.fn3;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Compare.compare;
import static com.jnape.palatable.lambda.functions.ordering.ComparisonRelation.equal;
import static com.jnape.palatable.lambda.functions.ordering.ComparisonRelation.greaterThan;
import static com.jnape.palatable.lambda.functions.ordering.ComparisonRelation.lessThan;
import static java.util.Comparator.naturalOrder;
import static org.junit.Assert.assertEquals;

public class CompareTest {
    @Test
    public void comparisons() {
        assertEquals(equal(), compare(naturalOrder(), 1, 1));
        assertEquals(lessThan(), compare(naturalOrder(), 2, 1));
        assertEquals(greaterThan(), compare(naturalOrder(), 1, 2));
    }
}