package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.builtin.fn1.Constantly;
import com.jnape.palatable.lambda.semigroup.Semigroup;
import org.junit.Test;

import java.util.Arrays;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Repeat.repeat;
import static com.jnape.palatable.lambda.semigroup.builtin.Absent.absent;
import static org.junit.Assert.assertEquals;

public class AbsentTest {

    @Test
    public void semigroup() {
        Semigroup<Integer> addition = Integer::sum;

        assertEquals(just(3), absent(addition, just(1), just(2)));
        assertEquals(nothing(), absent(addition, nothing(), just(1)));
        assertEquals(nothing(), absent(addition, just(1), nothing()));
        assertEquals(nothing(), absent(addition, nothing(), nothing()));
    }

    @Test
    public void foldRight() {
        Absent<Integer>    absent   = absent();
        Semigroup<Integer> addition = Integer::sum;

        assertEquals(just(3), absent.apply(addition).foldRight(just(0), Arrays.asList(just(1), just(2))).value());
        assertEquals(nothing(), absent.apply(addition).foldRight(just(0), Arrays.asList(nothing(), just(1))).value());
        assertEquals(nothing(), absent.apply(addition).foldRight(just(0), Arrays.asList(just(1), nothing())).value());
        assertEquals(nothing(), absent.apply(addition).foldRight(just(0), Arrays.asList(nothing(), nothing())).value());
    }

    @Test
    public void foldLeft() {
        Absent<Integer>    absent   = absent();
        Semigroup<Integer> addition = Integer::sum;

        assertEquals(just(3), absent.apply(addition).foldLeft(just(0), Arrays.asList(just(1), just(2))));
        assertEquals(nothing(), absent.apply(addition).foldLeft(just(0), Arrays.asList(nothing(), just(1))));
        assertEquals(nothing(), absent.apply(addition).foldLeft(just(0), Arrays.asList(just(1), nothing())));
        assertEquals(nothing(), absent.apply(addition).foldLeft(just(0), Arrays.asList(nothing(), nothing())));
    }

    @Test(timeout = 200)
    public void foldRightShortCircuit() {
        Maybe<Unit> result = Absent.<Unit>absent(Constantly::constantly)
                .foldRight(just(UNIT), repeat(nothing())).value();
        assertEquals(nothing(), result);

        result = Absent.<Unit>absent(Constantly::constantly)
                .foldRight(nothing(), repeat(just(UNIT))).value();
        assertEquals(nothing(), result);
    }

    @Test(timeout = 200)
    public void foldLeftShortCircuit() {
        Maybe<Unit> result = Absent.<Unit>absent(Constantly::constantly)
                .foldLeft(just(UNIT), repeat(nothing()));
        assertEquals(nothing(), result);

        result = Absent.<Unit>absent(Constantly::constantly)
                .foldLeft(nothing(), repeat(just(UNIT)));
        assertEquals(nothing(), result);
    }

    @Test
    public void foldLeftWorksForJusts() {
        Maybe<Unit> result = Absent.<Unit>absent(Constantly::constantly)
                .foldLeft(just(UNIT), Arrays.asList(just(UNIT), just(UNIT)));
        assertEquals(just(UNIT), result);
    }

    @Test(timeout = 200)
    public void checkedApplyFoldRightShortCircuit() {
        Maybe<Unit> result = Absent.<Unit>absent().checkedApply(Constantly::constantly)
                .foldRight(just(UNIT), repeat(nothing())).value();
        assertEquals(nothing(), result);

        result = Absent.<Unit>absent().checkedApply(Constantly::constantly)
                .foldRight(nothing(), repeat(just(UNIT))).value();
        assertEquals(nothing(), result);
    }

    @Test(timeout = 200)
    public void checkedApplyFoldLeftShortCircuit() {
        Maybe<Unit> result = Absent.<Unit>absent().checkedApply(Constantly::constantly)
                .foldLeft(just(UNIT), repeat(nothing()));
        assertEquals(nothing(), result);

        result = Absent.<Unit>absent().checkedApply(Constantly::constantly)
                .foldLeft(nothing(), repeat(just(UNIT)));
        assertEquals(nothing(), result);
    }
}