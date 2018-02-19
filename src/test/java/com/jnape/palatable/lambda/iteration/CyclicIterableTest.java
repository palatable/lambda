package com.jnape.palatable.lambda.iteration;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.runner.RunWith;
import testsupport.traits.Deforesting;

@RunWith(Traits.class)
public class CyclicIterableTest {

    @TestTraits({Deforesting.class})
    public Fn1<Iterable<?>, Iterable<?>> testSubject() {
        return CyclicIterable::new;
    }
}