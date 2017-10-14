package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.EqualityAwareFn1;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class Fn1Test {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
    public Fn1<String, Integer> testSubject() {
        return new EqualityAwareFn1<>("1", Integer::parseInt);
    }

    @Test
    public void profunctorProperties() {
        Fn1<Integer, Integer> add2 = integer -> integer + 2;

        assertEquals((Integer) 3, add2.<String>diMapL(Integer::parseInt).apply("1"));
        assertEquals("3", add2.diMapR(Object::toString).apply(1));
        assertEquals("3", add2.<String, String>diMap(Integer::parseInt, Object::toString).apply("1"));
    }

    @Test
    public void fn1() {
        Function<String, Integer> parseInt = Integer::parseInt;
        assertEquals((Integer) 1, Fn1.fn1(parseInt).apply("1"));
    }
}
