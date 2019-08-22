package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct5;
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

import static com.jnape.palatable.lambda.adt.choice.Choice6.a;
import static com.jnape.palatable.lambda.adt.choice.Choice6.b;
import static com.jnape.palatable.lambda.adt.choice.Choice6.c;
import static com.jnape.palatable.lambda.adt.choice.Choice6.d;
import static com.jnape.palatable.lambda.adt.choice.Choice6.e;
import static com.jnape.palatable.lambda.adt.choice.Choice6.f;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class Choice6Test {

    private Choice6<Integer, String, Boolean, Double, Character, Long> a;
    private Choice6<Integer, String, Boolean, Double, Character, Long> b;
    private Choice6<Integer, String, Boolean, Double, Character, Long> c;
    private Choice6<Integer, String, Boolean, Double, Character, Long> d;
    private Choice6<Integer, String, Boolean, Double, Character, Long> e;
    private Choice6<Integer, String, Boolean, Double, Character, Long> f;

    @Before
    public void setUp() {
        a = a(1);
        b = b("two");
        c = c(true);
        d = d(4d);
        e = e('z');
        f = f(5L);
    }

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, BifunctorLaws.class, TraversableLaws.class})
    public Subjects<Choice6<String, Integer, Boolean, Character, Double, Long>> testSubjects() {
        return subjects(a("foo"), b(1), c(true), d('a'), e(2d), f(5L));
    }

    @Test
    public void convergeStaysInChoice() {
        Fn1<Long, CoProduct5<Integer, String, Boolean, Double, Character, ?>> convergenceFn = f -> Choice5.b(f.toString());

        assertEquals(Choice5.a(1), a.converge(convergenceFn));
        assertEquals(Choice5.b("two"), b.converge(convergenceFn));
        assertEquals(Choice5.c(true), c.converge(convergenceFn));
        assertEquals(Choice5.d(4d), d.converge(convergenceFn));
        assertEquals(Choice5.e('z'), e.converge(convergenceFn));
        assertEquals(Choice5.b("5"), f.converge(convergenceFn));
    }

    @Test
    public void divergeStaysInChoice() {
        assertEquals(Choice7.a(1), a.diverge());
        assertEquals(Choice7.b("two"), b.diverge());
        assertEquals(Choice7.c(true), c.diverge());
        assertEquals(Choice7.d(4D), d.diverge());
        assertEquals(Choice7.e('z'), e.diverge());
        assertEquals(Choice7.f(5L), f.diverge());
    }

    @Test
    public void lazyZip() {
        assertEquals(f(2), f(1).lazyZip(lazy(f(x -> x + 1))).value());
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
    }

    @Test
    public void staticPure() {
        Choice6<Byte, Short, Integer, Long, Float, Double> choice =
                Choice6.<Byte, Short, Integer, Long, Float>pureChoice().apply(6d);
        assertEquals(f(6d), choice);
    }
}