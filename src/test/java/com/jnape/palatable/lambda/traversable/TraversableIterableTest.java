package com.jnape.palatable.lambda.traversable;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.runner.RunWith;
import testsupport.traits.FunctorLaws;
import testsupport.traits.TraversableLaws;

import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.util.Arrays.asList;

@RunWith(Traits.class)
public class TraversableIterableTest {

    @TestTraits({FunctorLaws.class, TraversableLaws.class})
    public Subjects<TraversableIterable<Integer>> testSubject() {
        return subjects(TraversableIterable.empty(), TraversableIterable.wrap(asList(1, 2, 3)));
    }
}