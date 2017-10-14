package com.jnape.palatable.lambda.traversable;

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

import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class LambdaIterableTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, TraversableLaws.class, MonadLaws.class})
    public Subjects<LambdaIterable<Object>> testSubject() {
        return subjects(LambdaIterable.empty(), LambdaIterable.wrap(singleton(1)), LambdaIterable.wrap(replicate(100, 1)));
    }

    @Test
    public void zipAppliesCartesianProductOfFunctionsAndValues() {
        LambdaIterable<Function<? super Integer, ? extends Integer>> fns = LambdaIterable.wrap(asList(x -> x + 1, x -> x - 1));
        LambdaIterable<Integer> xs = LambdaIterable.wrap(asList(1, 2, 3));
        assertThat(xs.zip(fns).unwrap(), iterates(2, 3, 4, 0, 1, 2));
    }
}