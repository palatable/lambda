package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;
import testsupport.traits.TraversableLaws;

import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class IdentityTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, TraversableLaws.class, MonadRecLaws.class})
    public Identity<?> testSubject() {
        return new Identity<>("");
    }

    @Test
    public void staticPure() {
        Identity<Integer> identity = pureIdentity().apply(1);
        assertEquals(new Identity<>(1), identity);
    }
}