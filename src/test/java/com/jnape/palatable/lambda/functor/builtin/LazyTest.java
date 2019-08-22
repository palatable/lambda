package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.EquatableM;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Times.times;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.pureLazy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;

@RunWith(Traits.class)
public class LazyTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
    public EquatableM<Lazy<?>, Integer> testSubject() {
        return new EquatableM<>(lazy(0), Lazy::value);
    }

    @Test
    public void valueExtraction() {
        assertEquals("foo", lazy("foo").value());
        assertEquals("foo", lazy(() -> "foo").value());
    }

    @Test
    public void lazyEvaluation() {
        AtomicBoolean invoked = new AtomicBoolean(false);
        Lazy<Integer> lazy = lazy(0).flatMap(x -> {
            invoked.set(true);
            return lazy(x + 1);
        });
        assertFalse(invoked.get());
        assertEquals((Integer) 1, lazy.value());
        assertTrue(invoked.get());
    }

    @Test
    public void linearStackSafety() {
        assertEquals(STACK_EXPLODING_NUMBER,
                     times(STACK_EXPLODING_NUMBER, f -> f.fmap(x -> x + 1), lazy(0)).value());
    }

    @Test
    public void recursiveStackSafety() {
        assertEquals(STACK_EXPLODING_NUMBER,
                     new Fn1<Lazy<Integer>, Lazy<Integer>>() {
                         @Override
                         public Lazy<Integer> checkedApply(Lazy<Integer> lazyX) {
                             return lazyX.flatMap(x -> x < STACK_EXPLODING_NUMBER
                                                       ? apply(lazy(x + 1))
                                                       : lazy(x));
                         }
                     }.apply(lazy(0)).value());
    }

    @Test
    public void staticPure() {
        Lazy<Integer> lazy = pureLazy().apply(1);
        assertEquals(lazy(1), lazy);
    }
}