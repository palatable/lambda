package com.jnape.palatable.lambda.iterables;

import com.jnape.palatable.lambda.tuples.Tuple2;
import org.junit.Test;
import testsupport.matchers.IterableMatcher;

import static com.jnape.palatable.lambda.iterables.CartesianProductIterable.cartesianProduct;
import static com.jnape.palatable.lambda.tuples.Tuple2.tuple;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@SuppressWarnings("unchecked")
public class CartesianProductIterableTest {

    @Test
    public void computesCartesianProductOfTwoEquallySizedIterables() {
        Iterable<Integer> numbers = asList(1, 2, 3);
        Iterable<String> letters = asList("a", "b", "c");

        assertThat(
                cartesianProduct(numbers, letters),
                iterates(
                        tuple(1, "a"),
                        tuple(1, "b"),
                        tuple(1, "c"),
                        tuple(2, "a"),
                        tuple(2, "b"),
                        tuple(2, "c"),
                        tuple(3, "a"),
                        tuple(3, "b"),
                        tuple(3, "c")
                )
        );
    }

    @Test
    public void worksForTwoUnequallySizedIterables() {
        Iterable<Integer> oneThroughThree = asList(1, 2, 3);
        Iterable<String> aAndB = asList("a", "b");

        assertThat(
                cartesianProduct(oneThroughThree, aAndB),
                iterates(
                        tuple(1, "a"),
                        tuple(1, "b"),
                        tuple(2, "a"),
                        tuple(2, "b"),
                        tuple(3, "a"),
                        tuple(3, "b")
                )
        );
    }

    @Test
    public void isEmptyIfFirstIterableIsEmpty() {
        Iterable<Integer> empty = asList();
        Iterable<String> notEmpty = asList("a", "b");

        assertThat(cartesianProduct(empty, notEmpty), IterableMatcher.<Tuple2<Integer, String>>iterates());
    }
}
