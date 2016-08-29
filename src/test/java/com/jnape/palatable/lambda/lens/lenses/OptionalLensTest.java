package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.lens.Lens;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.jnape.palatable.lambda.lens.Lens.lens;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static com.jnape.palatable.lambda.lens.lenses.OptionalLens.liftA;
import static com.jnape.palatable.lambda.lens.lenses.OptionalLens.liftB;
import static com.jnape.palatable.lambda.lens.lenses.OptionalLens.liftS;
import static com.jnape.palatable.lambda.lens.lenses.OptionalLens.liftT;
import static org.junit.Assert.assertEquals;

public class OptionalLensTest {

    private Lens<String, Boolean, Character, Integer> lens;

    @Before
    public void setUp() throws Exception {
        lens = lens(s -> s.charAt(0), (s, b) -> s.length() == b);
    }

    @Test
    public void asOptionalWrapsValuesInOptional() {
        Lens.Simple<String, Optional<String>> asOptional = OptionalLens.asOptional();

        assertEquals(Optional.of("foo"), view(asOptional, "foo"));
        assertEquals(Optional.empty(), view(asOptional, null));
        assertEquals("bar", set(asOptional, Optional.of("bar"), "foo"));
        assertEquals("foo", set(asOptional, Optional.empty(), "foo"));
    }

    @Test
    public void liftSLiftsSToOptional() {
        assertEquals((Character) '3', view(liftS(lens, "3"), Optional.empty()));
    }

    @Test
    public void liftTLiftsTToOptional() {
        assertEquals(Optional.of(true), set(liftT(lens), 3, "123"));
    }

    @Test
    public void liftALiftsAToOptional() {
        assertEquals(Optional.of('1'), view(liftA(lens), "123"));
    }

    @Test
    public void liftBLiftsBToOptional() {
        assertEquals(true, set(OptionalLens.liftB(lens, 1), Optional.empty(), "1"));
    }

    @Test
    public void unLiftSPullsSOutOfOptional() {
        Lens<Optional<String>, Optional<Boolean>, Optional<Character>, Optional<Integer>> liftedToOptional = liftS(liftT(liftA(liftB(lens, 3))), "123");
        assertEquals(Optional.of('f'), view(OptionalLens.unLiftS(liftedToOptional), "f"));
    }

    @Test
    public void unLiftTPullsTOutOfOptional() {
        Lens<Optional<String>, Optional<Boolean>, Optional<Character>, Optional<Integer>> liftedToOptional = liftS(liftT(liftA(liftB(lens, 3))), "123");
        assertEquals(true, set(OptionalLens.unLiftT(liftedToOptional, false), Optional.of(3), Optional.of("321")));
    }

    @Test
    public void unLiftAPullsAOutOfOptional() {
        Lens<Optional<String>, Optional<Boolean>, Optional<Character>, Optional<Integer>> liftedToOptional = liftS(liftT(liftA(liftB(lens, 3))), "123");
        assertEquals((Character) '1', view(OptionalLens.unLiftA(liftedToOptional, '4'), Optional.empty()));
    }

    @Test
    public void unLiftBPullsBOutOfOptional() {
        Lens<Optional<String>, Optional<Boolean>, Optional<Character>, Optional<Integer>> liftedToOptional = liftS(liftT(liftA(liftB(lens, 3))), "123");
        assertEquals(Optional.of(true), set(OptionalLens.unLiftB(liftedToOptional), 3, Optional.of("321")));
    }
}