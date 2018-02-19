package com.jnape.palatable.lambda.iteration;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.runner.RunWith;
import testsupport.traits.NestingStackSafety;

@RunWith(Traits.class)
public class SnocIterableTest {

    @TestTraits({NestingStackSafety.class})
    public Fn1<Iterable<Object>, Iterable<Object>> testSubject() {
        return xs -> new SnocIterable<>(1, xs);
    }
}