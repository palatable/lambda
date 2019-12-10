package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monoid.Monoid;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.monoid.builtin.EndoK.endoK;
import static org.junit.Assert.assertEquals;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;

public class EndoKTest {

    @Test
    public void identity() {
        Monoid<Fn1<Integer, Identity<Integer>>> endoK = endoK(pureIdentity());
        assertEquals(new Identity<>(1), endoK.identity().apply(1));
    }

    @Test
    public void monoid() {
        Monoid<Fn1<Integer, Identity<Integer>>> endoK = endoK(pureIdentity());
        assertEquals(new Identity<>(3),
                     endoK.apply(x -> new Identity<>(x + 1), x -> new Identity<>(x + 2)).apply(0));
    }

    @Test
    public void stackSafe() {
        Monoid<Fn1<Integer, Identity<Integer>>> endoK = endoK(pureIdentity());
        assertEquals(new Identity<>(0), endoK.reduceLeft(replicate(STACK_EXPLODING_NUMBER, Identity::new)).apply(0));
    }
}