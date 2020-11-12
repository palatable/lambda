package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.InGroupsOfM.inGroupsOfM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;
import static testsupport.matchers.IterateTMatcher.iterates;

public class InGroupsOfMTest {

    @Test
    public void evenlyDividedGroupsOfTwo() {
        IterateT<Identity<?>, Integer> numbers = takeM(6, naturalNumbersM(pureIdentity()));
        List<IterateT<Identity<?>, Integer>> actual =
                inGroupsOfM(2, numbers)
                        .<List<IterateT<Identity<?>, Integer>>, Identity<List<IterateT<Identity<?>, Integer>>>>toCollection(ArrayList::new)
                        .runIdentity();
        assertThat(actual, contains(iterates(1, 2), iterates(3, 4), iterates(5, 6)));
    }

    @Test
    public void groupsOfTwoWithLastGroupShort() {
        IterateT<Identity<?>, Integer> numbers = takeM(5, naturalNumbersM(pureIdentity()));
        List<IterateT<Identity<?>, Integer>> actual =
                inGroupsOfM(2, numbers)
                        .<List<IterateT<Identity<?>, Integer>>, Identity<List<IterateT<Identity<?>, Integer>>>>toCollection(ArrayList::new)
                        .runIdentity();
        assertThat(actual, contains(iterates(1, 2), iterates(3, 4), iterates(5)));
    }
}