package com.jnape.palatable.lambda.traversable;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.TraversableLaws;

import java.util.Optional;

@RunWith(Traits.class)
public class LambdaOptionalTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, TraversableLaws.class})
    public LambdaOptional<Object> testSubject() {
        return LambdaOptional.wrap(Optional.of(new Object()));
    }
}