package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.GT.gt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LTE.lte;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeWhileM.takeWhileM;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.io.IO.pureIO;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.empty;
import static com.jnape.palatable.lambda.semigroup.builtin.Max.max;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static testsupport.matchers.IOMatcher.yieldsValue;
import static testsupport.matchers.IterateTMatcher.isEmpty;
import static testsupport.matchers.IterateTMatcher.iterates;

public class TakeWhileMTest {

    public static final Fn1<Integer, Integer> MOD_10 = i -> i % 10;

    @Test
    public void takeEmpty() {
        IterateT<Identity<?>, Integer> numbers = empty(pureIdentity());
        assertThat(takeWhileM(lt(5), numbers), isEmpty());
    }

    @Test
    public void takeNothing() {
        IterateT<Identity<?>, Integer> numbers = naturalNumbersM(pureIdentity());
        assertThat(takeWhileM(gt(5), numbers), isEmpty());
    }

    @Test
    public void takeSomeInfinite() {
        IterateT<Identity<?>, Integer> numbers = naturalNumbersM(pureIdentity());
        assertThat(takeWhileM(lte(5), numbers), iterates(1, 2, 3, 4, 5));
    }

    @Test
    public void takeNothingIntermittent() {
        IterateT<Identity<?>, Integer> numbers = naturalNumbersM(pureIdentity());
        assertThat(takeWhileM(gt(5).diMapL(MOD_10), numbers), isEmpty());
    }

    @Test
    public void takeSomeIntermittent() {
        IterateT<Identity<?>, Integer> numbers = naturalNumbersM(pureIdentity());
        assertThat(takeWhileM(lte(5).diMapL(MOD_10), numbers), iterates(1, 2, 3, 4, 5));
    }

    @Test
    public void takeStackSafe() {
        IterateT<IO<?>, Integer> numbers = naturalNumbersM(pureIO());
        assertThat(takeWhileM(lte(STACK_EXPLODING_NUMBER), numbers).fold((a, i) -> io(max(a, i)), io(0)),
                   yieldsValue(equalTo(STACK_EXPLODING_NUMBER)));
    }
}