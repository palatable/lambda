package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.TraversableLaws;

import static org.junit.Assert.assertEquals;
import testsupport.traits.MonadRecLaws;

@RunWith(Traits.class)
public class TaggedTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, TraversableLaws.class, MonadRecLaws.class})
    public Tagged<String, Integer> testSubject() {
        return new Tagged<>(1);
    }

    @Test
    public void staticPure() {
        Tagged<String, Integer> tagged = Tagged.<String>pureTagged().apply(1);
        assertEquals(new Tagged<>(1), tagged);
    }
}