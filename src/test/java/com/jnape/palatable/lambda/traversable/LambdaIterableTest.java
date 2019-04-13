package com.jnape.palatable.lambda.traversable;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.TraversableLaws;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Size.size;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.traversable.LambdaIterable.empty;
import static com.jnape.palatable.lambda.traversable.LambdaIterable.wrap;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class LambdaIterableTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, TraversableLaws.class, MonadLaws.class})
    public Subjects<LambdaIterable<Object>> testSubject() {
        return subjects(LambdaIterable.empty(), wrap(singleton(1)), wrap(replicate(100, 1)));
    }

    @Test
    public void zipAppliesCartesianProductOfFunctionsAndValues() {
        LambdaIterable<Function<? super Integer, ? extends Integer>> fns = wrap(asList(x -> x + 1, x -> x - 1));
        LambdaIterable<Integer> xs = wrap(asList(1, 2, 3));
        assertThat(xs.zip(fns).unwrap(), iterates(2, 3, 4, 0, 1, 2));
    }

    @Test
    public void earlyTraverseTermination() {
        assertEquals(nothing(), wrap(repeat(1)).traverse(x -> nothing(), Maybe::just));
        assertEquals(nothing(), LambdaIterable.<Maybe<Integer>>wrap(cons(just(1), repeat(nothing())))
                .traverse(id(), Maybe::just));
    }

    @Test
    public void traverseStackSafety() {
        Maybe<LambdaIterable<Integer>> traversed = wrap(replicate(STACK_EXPLODING_NUMBER, just(1)))
                .traverse(id(), Maybe::just);
        assertEquals(just(STACK_EXPLODING_NUMBER.longValue()),
                     traversed.fmap(LambdaIterable::unwrap).fmap(size()));
    }

    @Test
    public void lazyZip() {
        assertEquals(wrap(singleton(2)), wrap(singleton(1)).lazyZip(lazy(wrap(singleton(x -> x + 1)))).value());
        assertEquals(empty(), empty().lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
    }
}