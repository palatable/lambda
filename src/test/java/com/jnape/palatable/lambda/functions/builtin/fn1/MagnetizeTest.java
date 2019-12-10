package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.EmptyIterableSupport;
import testsupport.traits.FiniteIteration;
import testsupport.traits.ImmutableIteration;
import testsupport.traits.InfiniteIterableSupport;
import testsupport.traits.Laziness;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Magnetize.magnetize;
import static java.util.Arrays.asList;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class MagnetizeTest {

    @TestTraits({EmptyIterableSupport.class, InfiniteIterableSupport.class, FiniteIteration.class, ImmutableIteration.class, Laziness.class})
    public Fn1<Iterable<Object>, Iterable<Iterable<Object>>> testSubject() {
        return magnetize();
    }

    @Test
    public void magnetizesElementsByPredicateOutcome() {
        assertThat(magnetize(asList(1, 1, 2, 3, 3, 3, 2, 2, 1)),
                   contains(iterates(1, 1),
                            iterates(2),
                            iterates(3, 3, 3),
                            iterates(2, 2),
                            iterates(1)));
    }
}