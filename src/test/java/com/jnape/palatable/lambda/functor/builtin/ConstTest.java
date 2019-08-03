package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.BifunctorLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;
import testsupport.traits.TraversableLaws;

import static com.jnape.palatable.lambda.functor.builtin.Const.pureConst;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class ConstTest {

    @TestTraits({
            FunctorLaws.class,
            ApplicativeLaws.class,
            MonadLaws.class,
            MonadRecLaws.class,
            BifunctorLaws.class,
            TraversableLaws.class})
    public Const<?, ?> testSubject() {
        return new Const<>(1);
    }

    @Test
    public void staticPure() {
        Const<Integer, String> constInt = pureConst(1).apply("foo");
        assertEquals(new Const<>(1), constInt);
    }
}