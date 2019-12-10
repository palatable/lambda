package com.jnape.palatable.lambda.monad;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.Equivalence;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadRecLaws;

import static com.jnape.palatable.lambda.functions.Fn1.fn1;
import static com.jnape.palatable.lambda.functions.builtin.fn3.Times.times;
import static com.jnape.palatable.lambda.monad.SafeT.safeT;
import static org.junit.Assert.assertEquals;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static testsupport.traits.Equivalence.equivalence;

@RunWith(Traits.class)
public class SafeTTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class, MonadRecLaws.class})
    public Equivalence<SafeT<Identity<?>, Integer>> testSubject() {
        return equivalence(safeT(new Identity<>(1)), SafeT::<Identity<Integer>>runSafeT);
    }

    @Test
    public void stackSafelyComposesMonadRecs() {
        assertEquals(STACK_EXPLODING_NUMBER,
                     times(STACK_EXPLODING_NUMBER, f -> f.fmap(x -> x + 1), safeT(Id.<Integer>id()))
                             .<Fn1<Integer, Integer>>runSafeT()
                             .apply(0));
    }

    @Test
    public void flatMapStackSafety() {
        assertEquals(new Identity<>(STACK_EXPLODING_NUMBER),
                     times(STACK_EXPLODING_NUMBER,
                           safeT -> safeT.flatMap(x -> safeT(new Identity<>(x + 1))),
                           safeT(new Identity<>(0)))
                             .runSafeT());
    }

    @Test
    public void zipStackSafety() {
        assertEquals((Integer) (STACK_EXPLODING_NUMBER + 1),
                     times(STACK_EXPLODING_NUMBER,
                           safeT -> safeT.zip(safeT(fn1(x -> y -> x + y))),
                           safeT(Fn1.<Integer, Integer>fn1(x -> x)))
                             .<Fn1<Integer, Integer>>runSafeT()
                             .apply(1));
    }
}