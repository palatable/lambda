package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct7;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.BifunctorLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;
import testsupport.traits.TraversableLaws;

import static com.jnape.palatable.lambda.adt.choice.Choice8.a;
import static com.jnape.palatable.lambda.adt.choice.Choice8.b;
import static com.jnape.palatable.lambda.adt.choice.Choice8.c;
import static com.jnape.palatable.lambda.adt.choice.Choice8.d;
import static com.jnape.palatable.lambda.adt.choice.Choice8.e;
import static com.jnape.palatable.lambda.adt.choice.Choice8.f;
import static com.jnape.palatable.lambda.adt.choice.Choice8.g;
import static com.jnape.palatable.lambda.adt.choice.Choice8.h;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class Choice8Test {

    private Choice8<Integer, String, Boolean, Double, Character, Long, Float, Short> a;
    private Choice8<Integer, String, Boolean, Double, Character, Long, Float, Short> b;
    private Choice8<Integer, String, Boolean, Double, Character, Long, Float, Short> c;
    private Choice8<Integer, String, Boolean, Double, Character, Long, Float, Short> d;
    private Choice8<Integer, String, Boolean, Double, Character, Long, Float, Short> e;
    private Choice8<Integer, String, Boolean, Double, Character, Long, Float, Short> f;
    private Choice8<Integer, String, Boolean, Double, Character, Long, Float, Short> g;
    private Choice8<Integer, String, Boolean, Double, Character, Long, Float, Short> h;

    @Before
    public void setUp() {
        a = a(1);
        b = b("two");
        c = c(true);
        d = d(4d);
        e = e('z');
        f = f(5L);
        g = g(6F);
        h = h((short) 7);
    }

    @TestTraits({
            FunctorLaws.class,
            ApplicativeLaws.class,
            MonadLaws.class,
            BifunctorLaws.class,
            TraversableLaws.class,
            MonadRecLaws.class})
    public Subjects<Choice8<String, Integer, Boolean, Character, Double, Long, Float, Short>> testSubjects() {
        return subjects(a("foo"), b(1), c(true), d('a'), e(2d), f(5L), g(6F), h((short) 7));
    }

    @Test
    public void convergeStaysInChoice() {
        Fn1<Short, CoProduct7<Integer, String, Boolean, Double, Character, Long, Float, ?>> convergenceFn =
                h -> Choice7.b(h.toString());

        assertEquals(Choice7.a(1), a.converge(convergenceFn));
        assertEquals(Choice7.b("two"), b.converge(convergenceFn));
        assertEquals(Choice7.c(true), c.converge(convergenceFn));
        assertEquals(Choice7.d(4d), d.converge(convergenceFn));
        assertEquals(Choice7.e('z'), e.converge(convergenceFn));
        assertEquals(Choice7.f(5L), f.converge(convergenceFn));
        assertEquals(Choice7.g(6F), g.converge(convergenceFn));
        assertEquals(Choice7.b("7"), h.converge(convergenceFn));
    }

    @Test
    public void lazyZip() {
        assertEquals(h(2), h(1).lazyZip(lazy(h(x -> x + 1))).value());
        assertEquals(a(1), a(1).lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
        assertEquals(b(1), b(1).lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
        assertEquals(c(1), c(1).lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
        assertEquals(d(1), d(1).lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
        assertEquals(e(1), e(1).lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
        assertEquals(f(1), f(1).lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
        assertEquals(g(1), g(1).lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
    }

    @Test
    public void staticPure() {
        Choice8<Byte, Short, Integer, Long, Float, Double, Boolean, Character> choice =
                Choice8.<Byte, Short, Integer, Long, Float, Double, Boolean>pureChoice().apply('c');
        assertEquals(h('c'), choice);
    }
}