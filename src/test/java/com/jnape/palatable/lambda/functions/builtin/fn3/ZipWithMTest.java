package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.DropM.dropM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
import static com.jnape.palatable.lambda.functions.builtin.fn3.ZipWithM.zipWithM;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static org.junit.Assert.*;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static testsupport.matchers.IterateTMatcher.iterates;

public class ZipWithMTest {

    @Test
    public void zipsSquares() {
        IterateT<Identity<?>, Integer> numbers = takeM(5, naturalNumbersM(pureIdentity()));
        assertThat(zipWithM((a, b) -> a * b, numbers, numbers.fmap(i -> i + 10)), iterates(11, 24, 39, 56, 75));
    }

    @Test
    public void zipsAsymetrical() {
        IterateT<Identity<?>, Integer> numbers = takeM(15, naturalNumbersM(pureIdentity()));
        assertThat(zipWithM((a, b) -> a * b, numbers, dropM(10, numbers)), iterates(11, 24, 39, 56, 75));
    }
}