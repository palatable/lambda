package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.runner.RunWith;
import testsupport.traits.Deforesting;

@RunWith(Traits.class)
public class SnocIterableTest {

    @TestTraits({Deforesting.class})
    public Fn1<Iterable<Object>, Iterable<Object>> testSubject() {
        return xs -> new SnocIterable<>(1, xs);
    }
}