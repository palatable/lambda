package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;

@RunWith(Traits.class)
public class ComposeTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class})
    public Compose<Identity, Identity, Integer> testSubject() {
        return new Compose<>(new Identity<>(new Identity<>(1)));
    }
}