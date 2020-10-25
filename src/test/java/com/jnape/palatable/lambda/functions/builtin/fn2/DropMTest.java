package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn1.HeadM.headM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.DropM.dropM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.empty;
import static org.junit.Assert.*;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static testsupport.matchers.IterateTMatcher.*;

public class DropMTest {

    @Test
    public void dropEmpty() {
        IterateT<Identity<?>, Integer> numbers = empty(pureIdentity());
        assertThat(dropM(5, numbers), isEmpty());
    }

    @Test
    public void dropAllShort() {
        IterateT<Identity<?>, Integer> numbers = takeM(3, naturalNumbersM(pureIdentity()));
        assertThat(dropM(5, numbers), isEmpty());
    }

    @Test
    public void dropAll() {
        IterateT<Identity<?>, Integer> numbers = takeM(5, naturalNumbersM(pureIdentity()));
        assertThat(dropM(5, numbers), isEmpty());
    }

    @Test
    public void dropSome() {
        IterateT<Identity<?>, Integer> numbers = takeM(6, naturalNumbersM(pureIdentity()));
        assertThat(dropM(5, numbers), iterates(6));
    }

    @Test
    public void dropStackSafe() {
        IterateT<Identity<?>, Integer> numbers = naturalNumbersM(pureIdentity());
        Identity<Maybe<Integer>> actual = headM(dropM(STACK_EXPLODING_NUMBER, numbers));
        assertEquals(just(STACK_EXPLODING_NUMBER + 1), actual.runIdentity());
    }
}