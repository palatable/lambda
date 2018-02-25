package com.jnape.palatable.lambda.iteration;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.runner.RunWith;
import testsupport.traits.Deforesting;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeWhile.takeWhile;

@RunWith(Traits.class)
public class PredicatedTakingIterableTest {

    @TestTraits({Deforesting.class})
    public Fn1<Iterable<Integer>, Iterable<Integer>> testSubject() {
        return takeWhile(constantly(true));
    }
}