package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.CartesianProduct.cartesianProduct;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
@SuppressWarnings("unchecked")
public class CartesianProductTest {

    @TestTraits({Laziness.class, ImmutableIteration.class, EmptyIterableSupport.class, FiniteIteration.class})
    public Fn1<Iterable<Object>, Iterable<Tuple2<Integer, Object>>> createTestSubject() {
        return cartesianProduct(asList(1, 2, 3));
    }

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
}
