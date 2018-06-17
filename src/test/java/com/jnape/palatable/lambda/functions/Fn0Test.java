package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.runner.RunWith;
import testsupport.EqualityAwareFn0;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

@RunWith(Traits.class)
public class Fn0Test {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
    public Fn0<Integer> testSubject() {
        return new EqualityAwareFn0<>(constantly(1)::apply);
    }
}