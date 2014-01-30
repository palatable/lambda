package com.jnape.palatable.lambda;

import com.jnape.palatable.lambda.exceptions.EmptyIterableException;
import org.junit.Test;

import java.util.Collections;

import static com.jnape.palatable.lambda.Folds.foldLeft;
import static com.jnape.palatable.lambda.Folds.reduceLeft;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FoldsTest {

    private static final DyadicFunction<String, String, String> ILLUSTRATE_EVALUATIONS = new DyadicFunction<String, String, String>() {
        @Override
        public String apply(String s, String s2) {
            return format("(%s + %s)", s, s2);
        }
    };

    @Test
    public void foldLeftAccumulatesLeftToRight() {
        assertThat(
                foldLeft(ILLUSTRATE_EVALUATIONS, "1", asList("2", "3", "4", "5")),
                is("((((1 + 2) + 3) + 4) + 5)")
        );
    }

    @Test
    public void reduceLeftAccumulatesLeftToRightUsingFirstElementAsStartingAccumulation() {
        assertThat(
                reduceLeft(ILLUSTRATE_EVALUATIONS, asList("1", "2", "3", "4", "5")),
                is("((((1 + 2) + 3) + 4) + 5)")
        );
    }

    @Test(expected = EmptyIterableException.class)
    public void reduceLeftFailsIfEmptyIterable() {
        reduceLeft(ILLUSTRATE_EVALUATIONS, Collections.<String>emptyList());
    }
}
