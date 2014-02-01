package com.jnape.palatable.lambda;

import com.jnape.palatable.lambda.exceptions.EmptyIterableException;
import org.junit.Test;

import java.util.Collections;

import static com.jnape.palatable.lambda.Folds.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FoldsTest {

    private static final DyadicFunction<String, String, String> simulateAdd = new DyadicFunction<String, String, String>() {
        @Override
        public String apply(String acc, String x) {
            return format("(%s + %s)", acc, x);
        }
    };

    @Test
    public void foldLeftAccumulatesLeftToRight() {
        assertThat(
                foldLeft(simulateAdd, "1", asList("2", "3", "4", "5")),
                is("((((1 + 2) + 3) + 4) + 5)")
        );
    }

    @Test
    public void reduceLeftAccumulatesLeftToRightUsingFirstElementAsStartingAccumulation() {
        assertThat(
                reduceLeft(simulateAdd, asList("1", "2", "3", "4", "5")),
                is("((((1 + 2) + 3) + 4) + 5)")
        );
    }

    @Test(expected = EmptyIterableException.class)
    public void reduceLeftFailsIfEmptyIterable() {
        reduceLeft(simulateAdd, Collections.<String>emptyList());
    }

    @Test
    public void foldRightAccumulatesRightToLeft() {
        assertThat(
                foldRight(simulateAdd, "5", asList("1", "2", "3", "4")),
                is("(1 + (2 + (3 + (4 + 5))))")
        );
    }

    @Test
    public void reduceRightAccumulatesRightToLeftUsingLastElementAsStartingAccumulation() {
        assertThat(
                reduceRight(simulateAdd, asList("1", "2", "3", "4", "5")),
                is("(1 + (2 + (3 + (4 + 5))))")
        );
    }
}
