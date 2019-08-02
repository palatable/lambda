package com.jnape.palatable.lambda.adt.choice;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.*;

import static com.jnape.palatable.lambda.adt.choice.Choice2.a;
import static com.jnape.palatable.lambda.adt.choice.Choice2.b;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class Choice2Test {

    private Choice2<Integer, Boolean> a;
    private Choice2<Integer, Boolean> b;

    @Before
    public void setUp() {
        a = a(1);
        b = b(true);
    }

    @TestTraits({FunctorLaws.class,
            ApplicativeLaws.class,
            MonadLaws.class,
            BifunctorLaws.class,
            TraversableLaws.class,
            MonadRecLaws.class})
    public Subjects<Choice2<String, Integer>> testSubjects() {
        return subjects(a("foo"), b(1));
    }

    @Test
    public void divergeStaysInChoice() {
        assertEquals(Choice3.<Integer, Boolean, Object>a(1), a.diverge());
        assertEquals(Choice3.<Integer, Boolean, Object>b(true), b.diverge());
    }

    @Test
    public void lazyZip() {
        assertEquals(b(2), b(1).lazyZip(lazy(b(x -> x + 1))).value());
        assertEquals(a("foo"), a("foo").lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
    }
}