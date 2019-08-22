package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct6;
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
import testsupport.traits.TraversableLaws;

import static com.jnape.palatable.lambda.adt.choice.Choice7.a;
import static com.jnape.palatable.lambda.adt.choice.Choice7.b;
import static com.jnape.palatable.lambda.adt.choice.Choice7.c;
import static com.jnape.palatable.lambda.adt.choice.Choice7.d;
import static com.jnape.palatable.lambda.adt.choice.Choice7.e;
import static com.jnape.palatable.lambda.adt.choice.Choice7.f;
import static com.jnape.palatable.lambda.adt.choice.Choice7.g;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class Choice7Test {

    private Choice7<Integer, String, Boolean, Double, Character, Long, Float> a;
    private Choice7<Integer, String, Boolean, Double, Character, Long, Float> b;
    private Choice7<Integer, String, Boolean, Double, Character, Long, Float> c;
    private Choice7<Integer, String, Boolean, Double, Character, Long, Float> d;
    private Choice7<Integer, String, Boolean, Double, Character, Long, Float> e;
    private Choice7<Integer, String, Boolean, Double, Character, Long, Float> f;
    private Choice7<Integer, String, Boolean, Double, Character, Long, Float> g;

    @Before
    public void setUp() {
        a = a(1);
        b = b("two");
        c = c(true);
        d = d(4d);
        e = e('z');
        f = f(5L);
        g = g(6F);
    }

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, BifunctorLaws.class, TraversableLaws.class})
    public Subjects<Choice7<String, Integer, Boolean, Character, Double, Long, Float>> testSubjects() {
        return subjects(a("foo"), b(1), c(true), d('a'), e(2d), f(5L), g(6F));
    }

    @Test
    public void convergeStaysInChoice() {
        Fn1<Float, CoProduct6<Integer, String, Boolean, Double, Character, Long, ?>> convergenceFn = g -> Choice6.b(g.toString());

        assertEquals(Choice6.a(1), a.converge(convergenceFn));
        assertEquals(Choice6.b("two"), b.converge(convergenceFn));
        assertEquals(Choice6.c(true), c.converge(convergenceFn));
        assertEquals(Choice6.d(4d), d.converge(convergenceFn));
        assertEquals(Choice6.e('z'), e.converge(convergenceFn));
        assertEquals(Choice6.f(5L), f.converge(convergenceFn));
        assertEquals(Choice6.b("6.0"), g.converge(convergenceFn));
    }

    @Test
    public void divergeStaysInChoice() {
        assertEquals(Choice8.a(1), a.diverge());
        assertEquals(Choice8.b("two"), b.diverge());
        assertEquals(Choice8.c(true), c.diverge());
        assertEquals(Choice8.d(4D), d.diverge());
        assertEquals(Choice8.e('z'), e.diverge());
        assertEquals(Choice8.f(5L), f.diverge());
        assertEquals(Choice8.g(6F), g.diverge());
    }

    @Test
    public void lazyZip() {
        assertEquals(g(2), g(1).lazyZip(lazy(g(x -> x + 1))).value());
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
    }

    @Test
    public void staticPure() {
        Choice7<Byte, Short, Integer, Long, Float, Double, Boolean> choice =
                Choice7.<Byte, Short, Integer, Long, Float, Double>pureChoice().apply(true);
        assertEquals(g(true), choice);
    }
}