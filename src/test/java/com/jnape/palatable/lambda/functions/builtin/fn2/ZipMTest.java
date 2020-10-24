package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ZipM.zipM;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.empty;
import static org.junit.Assert.*;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static testsupport.matchers.IterateTMatcher.isEmpty;
import static testsupport.matchers.IterateTMatcher.iterates;

public class ZipMTest {
    @Test
    public void zipEmpty() {
        IterateT<Identity<?>, Integer> numbers = empty(pureIdentity());
        IterateT<Identity<?>, String> strings = empty(pureIdentity());
        assertThat(zipM(numbers, strings), isEmpty());
    }

    @Test
    public void zipSameSize() {
        IterateT<Identity<?>, Integer> numbers = takeM(5, naturalNumbersM(pureIdentity()));
        IterateT<Identity<?>, String> strings = numbers.fmap(Integer::toBinaryString);
        assertThat(zipM(numbers, strings), iterates(tuple(1, "1"), tuple(2, "10"), tuple(3, "11"),
                                                    tuple(4, "100"), tuple(5, "101")));
    }

    @Test
    public void zipDifferentSize() {
        IterateT<Identity<?>, Integer> numbers = naturalNumbersM(pureIdentity());
        IterateT<Identity<?>, String> strings = takeM(5, numbers).fmap(Integer::toBinaryString);
        assertThat(zipM(numbers, strings), iterates(tuple(1, "1"), tuple(2, "10"), tuple(3, "11"),
                                                    tuple(4, "100"), tuple(5, "101")));
    }
}