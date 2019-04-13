package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.functor.builtin.Identity;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.specialized.Kleisli.kleisli;
import static java.lang.Integer.parseInt;
import static org.junit.Assert.assertEquals;

public class KleisliTest {

    private static final Kleisli<Integer, String, Identity<?>, Identity<String>>  G = kleisli(i -> new Identity<>(i.toString()));
    private static final Kleisli<String, Integer, Identity<?>, Identity<Integer>> F = kleisli(s -> new Identity<>(parseInt(s)));

    @Test
    public void leftToRightComposition() {
        assertEquals(new Identity<>(1), G.andThen(F).apply(1));
        assertEquals(new Identity<>("1"), F.andThen(G).apply("1"));
    }

    @Test
    public void rightToLeftComposition() {
        assertEquals(new Identity<>("1"), G.compose(F).apply("1"));
        assertEquals(new Identity<>(1), F.compose(G).apply(1));
    }
}