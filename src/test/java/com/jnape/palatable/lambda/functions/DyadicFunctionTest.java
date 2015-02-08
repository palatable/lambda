package com.jnape.palatable.lambda.functions;

import org.junit.Test;

import static com.jnape.palatable.lambda.tuples.Tuple2.tuple;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DyadicFunctionTest {

    private static final DyadicFunction<String, Integer, Boolean> CHECK_LENGTH = (string,
                                                                                  length) -> string.length() == length;

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
    public void mapsContravariantlyOverSecondArgument() {
        assertThat(CHECK_LENGTH.<String>diMapL(Integer::parseInt).apply("123").apply("3"), is(true));
    }

    @Test
    public void mapsCovariantlyOverReturnValue() {
        assertThat(CHECK_LENGTH.<String>diMapR(Object::toString).apply("123").apply(3), is("true"));
    }

    @Test
    public void mapsOverSecondArgumentAndReturnValueInSingleTransformation() {
        assertThat(
                CHECK_LENGTH.<String, String>diMap(
                        Integer::parseInt,
                        Object::toString
                ).apply("123", "3"),
                is("true")
        );
    }
}
