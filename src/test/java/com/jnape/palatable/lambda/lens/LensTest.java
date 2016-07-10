package com.jnape.palatable.lambda.lens;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Const;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class LensTest {

    private static final Lens<List<String>, Set<Integer>, String, Integer> LENS = Lens.lens(xs -> xs.get(0), (xs, i) -> singleton(i));

    @Test
    public void setsUnderIdentity() {
        Set<Integer> ints = LENS.<Identity<Set<Integer>>, Identity<Integer>>apply(
                s -> new Identity<>(s.length()),
                asList("foo", "bar", "baz")
        ).runIdentity();

        assertEquals(singleton(3), ints);
    }

    @Test
    public void viewsUnderConst() {
        Integer i = LENS.<Const<Integer, Set<Integer>>, Const<Integer, Integer>>apply(
                s -> new Const<>(s.length()),
                asList("foo", "bar", "baz")
        ).runConst();

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
}