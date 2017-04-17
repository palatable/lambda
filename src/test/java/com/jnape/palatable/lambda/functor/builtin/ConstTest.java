package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;

import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class ConstTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class})
    public Const testSubject() {
        return new Const<>(1);
    }

    @Test
    public void bifunctorProperties() {
        assertEquals("FOO", new Const<String, Integer>("foo").biMap(String::toUpperCase, x -> x + 1).runConst());
    }
}