package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import org.junit.Test;

import java.util.function.BiFunction;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class Fn2Test {

    private static final Fn2<String, Integer, Boolean> CHECK_LENGTH
            = (string, length) -> string.length() == length;

    @Test
    public void flipSwapsArguments() {
        assertThat(CHECK_LENGTH.flip().apply(3, "foo"), is(true));
    }

    @Test
    public void canBePartiallyApplied() {
        assertThat(CHECK_LENGTH.apply("quux").apply(4), is(true));
    }

    @Test
    public void uncurries() {
        assertThat(CHECK_LENGTH.uncurry().apply(tuple("abc", 3)), is(true));
    }

    @Test
    public void functorProperties() {
        assertThat(CHECK_LENGTH.fmap(f -> Id.id()).apply("foo").apply("bar"), is("bar"));
    }

    @Test
    public void profunctorProperties() {
        assertThat(CHECK_LENGTH.diMapL(Object::toString).apply(123).apply(3), is(true));
        assertThat(CHECK_LENGTH.diMapR(fn -> fn.andThen(Object::toString)).apply("123").apply(3), is("true"));
        assertThat(
                CHECK_LENGTH.<String, Fn1<Integer, String>>diMap(
                        Object::toString,
                        fn -> fn.andThen(Object::toString)
                ).apply("123").apply(3),
                is("true")
        );
    }

    @Test
    public void toBiFunction() {
        BiFunction<String, Integer, Boolean> biFunction = CHECK_LENGTH.toBiFunction();
        assertEquals(true, biFunction.apply("abc", 3));
    }
}
