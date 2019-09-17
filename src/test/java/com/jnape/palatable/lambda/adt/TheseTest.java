package com.jnape.palatable.lambda.adt;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.BifunctorLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;
import testsupport.traits.TraversableLaws;

import static com.jnape.palatable.lambda.adt.These.a;
import static com.jnape.palatable.lambda.adt.These.b;
import static com.jnape.palatable.lambda.adt.These.both;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class TheseTest {

    @TestTraits({FunctorLaws.class,
                 ApplicativeLaws.class,
                 MonadLaws.class,
                 MonadRecLaws.class,
                 TraversableLaws.class,
                 BifunctorLaws.class})
    public Subjects<These<String, Integer>> testSubject() {
        return subjects(a("foo"), b(1), both("foo", 1));
    }

    @Test
    public void lazyZip() {
        assertEquals(b(2), b(1).lazyZip(lazy(b(x -> x + 1))).value());
        assertEquals(both("foo", 2), b(1).lazyZip(lazy(both("foo", x -> x + 1))).value());
        assertEquals(both("foo", 2), both("foo", 1).lazyZip(lazy(both("bar", x -> x + 1))).value());
        assertEquals(both("foo", 2), both("foo", 1).lazyZip(lazy(b(x -> x + 1))).value());
        assertEquals(a(1), a(1).lazyZip(lazy(() -> {
            throw new AssertionError();
        })).value());
    }

    @Test
    public void staticPure() {
        These<String, Integer> these = These.<String>pureThese().apply(1);
        assertEquals(b(1), these);
    }
}