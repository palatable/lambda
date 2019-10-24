package com.jnape.palatable.lambda.functions.ordering;

import org.junit.Test;

import static com.jnape.palatable.lambda.functions.ordering.ComparisonRelation.equal;
import static com.jnape.palatable.lambda.functions.ordering.ComparisonRelation.greaterThan;
import static com.jnape.palatable.lambda.functions.ordering.ComparisonRelation.lessThan;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.junit.Assert.assertEquals;

public class ComparisonRelationTest {
    @Test
    public void fromInt() {
        assertEquals(greaterThan(), ComparisonRelation.fromInt(1));
        assertEquals(greaterThan(), ComparisonRelation.fromInt(MAX_VALUE));

        assertEquals(equal(), ComparisonRelation.fromInt(0));

        assertEquals(lessThan(), ComparisonRelation.fromInt(-1));
        assertEquals(lessThan(), ComparisonRelation.fromInt(MIN_VALUE));
    }
}