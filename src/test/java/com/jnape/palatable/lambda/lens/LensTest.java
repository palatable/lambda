package com.jnape.palatable.lambda.lens;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Const;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.jnape.palatable.lambda.lens.Lens.lens;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;

public class LensTest {

    private static final Lens<Map<String, List<String>>, Map<String, Set<Integer>>, List<String>, Set<Integer>> EARLIER_LENS = lens(m -> m.get("foo"), (m, s) -> singletonMap("foo", s));
    private static final Lens<List<String>, Set<Integer>, String, Integer>                                      LENS         = lens(xs -> xs.get(0), (xs, i) -> singleton(i));

    @Test
    public void setsUnderIdentity() {
        Set<Integer> ints = LENS.<Identity<Set<Integer>>, Identity<Integer>>apply(s -> new Identity<>(s.length()), asList("foo", "bar", "baz")).runIdentity();
        assertEquals(singleton(3), ints);
    }

    @Test
    public void viewsUnderConst() {
        Integer i = LENS.<Const<Integer, Set<Integer>>, Const<Integer, Integer>>apply(s -> new Const<>(s.length()), asList("foo", "bar", "baz")).runConst();
        assertEquals((Integer) 3, i);
    }

    @Test
    public void fix() {
        Fn1<String, Const<Integer, Integer>> fn = s -> new Const<>(s.length());
        List<String> s = singletonList("foo");

        Integer fixedLensResult = LENS.<Const<Integer, Set<Integer>>, Const<Integer, Integer>>fix().apply(fn, s).runConst();
        Integer unfixedLensResult = LENS.<Const<Integer, Set<Integer>>, Const<Integer, Integer>>apply(fn, s).runConst();

        assertEquals(unfixedLensResult, fixedLensResult);
    }

    @Test
    public void functorProperties() {
        assertEquals(false, set(LENS.fmap(Set::isEmpty), 1, singletonList("foo")));
    }

    @Test
    public void mapsIndividuallyOverParameters() {
        Lens<String, Boolean, Character, Integer> lens = lens(s -> s.charAt(0), (s, b) -> s.length() == b);
        Lens<Optional<String>, Optional<Boolean>, Optional<Character>, Optional<Integer>> theGambit = lens
                .mapS((Optional<String> optS) -> optS.orElse(""))
                .mapT(Optional::ofNullable)
                .mapA(Optional::ofNullable)
                .mapB((Optional<Integer> optI) -> optI.orElse(-1));

        Lens.Fixed<Optional<String>, Optional<Boolean>, Optional<Character>, Optional<Integer>, Identity<Optional<Boolean>>, Identity<Optional<Integer>>> fixed = theGambit.fix();
        assertEquals(Optional.of(true), fixed.apply(optC -> new Identity<>(optC.map(c -> parseInt(Character.toString(c)))), Optional.of("321")).runIdentity());
    }

    @Test
    public void composition() {
        Map<String, List<String>> map = singletonMap("foo", asList("one", "two", "three"));
        assertEquals("one", view(LENS.compose(EARLIER_LENS), map));
        assertEquals(singletonMap("foo", singleton(1)), set(LENS.compose(EARLIER_LENS), 1, map));
    }

    @Test
    public void andThenComposesInReverse() {
        Map<String, List<String>> map = singletonMap("foo", asList("one", "two", "three"));
        assertEquals("one", view(EARLIER_LENS.andThen(LENS), map));
        assertEquals(singletonMap("foo", singleton(1)), set(EARLIER_LENS.andThen(LENS), 1, map));
    }
}