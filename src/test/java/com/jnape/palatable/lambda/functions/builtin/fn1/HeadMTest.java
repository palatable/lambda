package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.HeadM.headM;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.empty;
import static org.junit.Assert.assertEquals;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;

public class HeadMTest {

    @Test
    public void headEmpty() {
        Identity<Maybe<Integer>> mma = headM(empty(pureIdentity()));
        assertEquals(mma.runIdentity(), nothing());
    }

    @Test
    public void headInfinite() {
        Identity<Maybe<Integer>> mma = headM(naturalNumbersM(pureIdentity()));
        assertEquals(mma.runIdentity(), just(1));
    }
}