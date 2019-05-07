package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.EquatableM;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.choice.Choice2.a;
import static com.jnape.palatable.lambda.adt.choice.Choice2.b;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn1.fn1;
import static com.jnape.palatable.lambda.functions.Fn1.fromFunction;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ReduceLeft.reduceLeft;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class Fn1Test {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
    public EquatableM<Fn1<String, ?>, ?> testSubject() {
        return new EquatableM<>(fn1(Integer::parseInt), f -> f.apply("1"));
    }

    @Test
    public void profunctorProperties() {
        Fn1<Integer, Integer> add2 = integer -> integer + 2;

        assertEquals((Integer) 3, add2.<String>diMapL(Integer::parseInt).apply("1"));
        assertEquals("3", add2.diMapR(Object::toString).apply(1));
        assertEquals("3", add2.<String, String>diMap(Integer::parseInt, Object::toString).apply("1"));
    }

    @Test
    public void staticFactoryMethod() {
        assertEquals((Integer) 1, Fn1.<String, Integer>fn1(Integer::parseInt).apply("1"));
        Function<String, Integer> function = Integer::parseInt;
        Fn1<String, Integer>      fn1      = fromFunction(function);
        assertEquals((Integer) 1, fn1.apply("1"));
    }

    @Test
    public void thunk() {
        Fn1<Integer, String> toString = Object::toString;
        assertEquals("1", toString.thunk(1).apply());
    }

    @Test
    public void widen() {
        Fn1<Integer, Integer> addOne = x -> x + 1;
        assertEquals(just(4), reduceLeft(addOne.widen(), asList(1, 2, 3)));
    }

    @Test
    public void cartesian() {
        Fn1<Integer, Integer> add1 = x -> x + 1;
        assertEquals(tuple("a", 2), add1.<String>cartesian().apply(tuple("a", 1)));
    }

    @Test
    public void carry() {
        Fn1<Integer, Integer> add1 = x -> x + 1;
        assertEquals(tuple(1, 2), add1.carry().apply(1));
    }

    @Test
    public void cocartesian() {
        Fn1<Integer, Integer> add1 = x -> x + 1;
        assertEquals(a("foo"), add1.<String>cocartesian().apply(a("foo")));
        assertEquals(b(2), add1.<String>cocartesian().apply(b(1)));
    }

    @Test
    public void choose() {
        Fn1<String, Integer> add1 = Integer::parseInt;
        assertEquals(b(123), add1.choose().apply("123"));
        assertEquals(a("foo"), add1.choose().apply("foo"));
    }

    @Test
    public void toFunction() {
        Fn1<Integer, Integer>      add1     = x -> x + 1;
        Function<Integer, Integer> function = add1.toFunction();
        assertEquals((Integer) 2, function.apply(1));
    }
}
