package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.*;

import static com.jnape.palatable.lambda.adt.choice.Choice5.*;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class Choice5Test {

    private Choice5<Integer, String, Boolean, Double, Character> a;
    private Choice5<Integer, String, Boolean, Double, Character> b;
    private Choice5<Integer, String, Boolean, Double, Character> c;
    private Choice5<Integer, String, Boolean, Double, Character> d;
    private Choice5<Integer, String, Boolean, Double, Character> e;

    @Before
    public void setUp() {
        a = a(1);
        b = b("two");
        c = c(true);
        d = d(4d);
        e = e('z');
    }

    @TestTraits({FunctorLaws.class,
                 ApplicativeLaws.class,
                 MonadLaws.class,
                 BifunctorLaws.class,
                 TraversableLaws.class,
                 MonadRecLaws.class})
    public Subjects<Choice5<String, Integer, Boolean, Character, Double>> testSubjects() {
        return subjects(Choice5.a("foo"), Choice5.b(1), Choice5.c(true), Choice5.d('a'), Choice5.e(2d));
    }

    @Test
    public void convergeStaysInChoice() {
        assertEquals(Choice4.a(1), a.converge(e -> Choice4.b(e.toString())));
        assertEquals(Choice4.b("two"), b.converge(e -> Choice4.b(e.toString())));
        assertEquals(Choice4.c(true), c.converge(e -> Choice4.b(e.toString())));
        assertEquals(Choice4.d(4d), d.converge(e -> Choice4.b(e.toString())));
        assertEquals(Choice4.b("z"), e.converge(e -> Choice4.b(e.toString())));
    }

    @Test
    public void divergeStaysInChoice() {
        assertEquals(Choice6.a(1), a.diverge());
        assertEquals(Choice6.b("two"), b.diverge());
        assertEquals(Choice6.c(true), c.diverge());
        assertEquals(Choice6.d(4D), d.diverge());
        assertEquals(Choice6.e('z'), e.diverge());
    }

    @Test
    public void lazyZip() {
        assertEquals(e(2), e(1).lazyZip(lazy(e(x -> x + 1))).value());
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
    }

    @Test
    public void staticPure() {
        Choice5<Byte, Short, Integer, Long, Float> choice = Choice5.<Byte, Short, Integer, Long>pureChoice().apply(5f);
        assertEquals(e(5f), choice);
    }
}