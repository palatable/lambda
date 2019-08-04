package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.*;

import static com.jnape.palatable.lambda.adt.choice.Choice3.a;
import static com.jnape.palatable.lambda.adt.choice.Choice3.b;
import static com.jnape.palatable.lambda.adt.choice.Choice3.c;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class Choice3Test {

    private Choice3<Integer, String, Boolean> a;
    private Choice3<Integer, String, Boolean> b;
    private Choice3<Integer, String, Boolean> c;

    @Before
    public void setUp() {
        a = Choice3.a(1);
        b = Choice3.b("two");
        c = Choice3.c(true);
    }

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, BifunctorLaws.class, TraversableLaws.class, MonadRecLaws.class})
    public Subjects<Choice3<String, Integer, Boolean>> testSubjects() {
        return subjects(a("foo"), b(1), c(true));
    }

    @Test
    public void convergeStaysInChoice() {
        assertEquals(Choice2.a(1), a.converge(c -> Choice2.b(c.toString())));
        assertEquals(Choice2.b("two"), b.converge(c -> Choice2.b(c.toString())));
        assertEquals(Choice2.b("true"), c.converge(c -> Choice2.b(c.toString())));
    }

    @Test
    public void divergeStaysInChoice() {
        assertEquals(Choice4.a(1), a.diverge());
        assertEquals(Choice4.b("two"), b.diverge());
        assertEquals(Choice4.c(true), c.diverge());
    }

    @Test
    public void lazyZip() {
        assertEquals(Choice3.c(2), c(1).lazyZip(lazy(c(x -> x + 1))).value());
        assertEquals(Choice3.b(1), b(1).lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
        assertEquals(Choice3.a(1), a(1).lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
    }
}