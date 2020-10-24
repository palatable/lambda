package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LTE.lte;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Snoc.snoc;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;
import static com.jnape.palatable.lambda.functions.builtin.fn4.IfThenElse.ifThenElse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static testsupport.matchers.IterableMatcher.iterates;

public class NaturalNumbersMTest {
    @Test
    public void producesTheNats() {
        IterateT<Identity<?>, Integer> numbers = naturalNumbersM(pureIdentity());
        Iterable<Integer> sample = numbers
                .<Iterable<Integer>, Identity<Iterable<Integer>>>foldCut(
                        (acc, n) -> new Identity<>(lte(10, n)
                                                   ? recurse(snoc(n, acc))
                                                   : terminate(acc)),
                        new Identity<>(emptyList()))
                .runIdentity();
        assertThat(sample, iterates(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    @Test
    public void producesNatsForever() {
        IterateT<Identity<?>, Integer> numbers = naturalNumbersM(pureIdentity());
        Iterable<Integer> sample = numbers
                .<Iterable<Integer>, Identity<Iterable<Integer>>>foldCut(
                        (acc, n) -> new Identity<>(lt(STACK_EXPLODING_NUMBER, n)
                                                   ? recurse(acc)
                                                   : lte(STACK_EXPLODING_NUMBER + 10, n)
                                                     ? recurse(snoc(n, acc))
                                                     : terminate(acc)),
                        new Identity<>(emptyList()))
                .runIdentity();
        assertThat(sample, iterates(STACK_EXPLODING_NUMBER,
                                    STACK_EXPLODING_NUMBER + 1,
                                    STACK_EXPLODING_NUMBER + 2,
                                    STACK_EXPLODING_NUMBER + 3,
                                    STACK_EXPLODING_NUMBER + 4,
                                    STACK_EXPLODING_NUMBER + 5,
                                    STACK_EXPLODING_NUMBER + 6,
                                    STACK_EXPLODING_NUMBER + 7,
                                    STACK_EXPLODING_NUMBER + 8,
                                    STACK_EXPLODING_NUMBER + 9,
                                    STACK_EXPLODING_NUMBER + 10));
    }
}