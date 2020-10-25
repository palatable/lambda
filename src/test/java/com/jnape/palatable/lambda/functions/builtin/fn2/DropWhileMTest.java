package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.HeadM.headM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.DropWhileM.dropWhileM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LTE.lte;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static testsupport.matchers.IterateTMatcher.isEmpty;
import static testsupport.matchers.IterateTMatcher.iterates;

public class DropWhileMTest {

    private static final Fn1<Integer, Integer> MOD_7 = i -> i % 7;

    @Test
    public void dropEmpty() {
        IterateT<Identity<?>, Integer> numbers = empty(pureIdentity());
        assertThat(dropWhileM(constantly(true), numbers), isEmpty());
    }

    @Test
    public void dropAll() {
        IterateT<Identity<?>, Integer> numbers = takeM(10, naturalNumbersM(pureIdentity()));
        assertThat(dropWhileM(lt(100), numbers), isEmpty());
    }

    @Test
    public void dropSome() {
        IterateT<Identity<?>, Integer> numbers = takeM(10, naturalNumbersM(pureIdentity()));
        assertThat(dropWhileM(lte(5), numbers), iterates(6, 7, 8, 9, 10));
    }

    @Test
    public void dropSomeIntermittent() {
        IterateT<Identity<?>, Integer> numbers = takeM(10, naturalNumbersM(pureIdentity()));
        assertThat(dropWhileM(lte(5).diMapL(MOD_7), numbers), iterates(6, 7, 8, 9, 10));
    }
    @Test
    public void dropStackSafe() {
        IterateT<Identity<?>, Integer> numbers = naturalNumbersM(pureIdentity());
        Identity<Maybe<Integer>> actual = headM(dropWhileM(lte(STACK_EXPLODING_NUMBER), numbers));
        assertEquals(just(STACK_EXPLODING_NUMBER + 1), actual.runIdentity());
    }
}