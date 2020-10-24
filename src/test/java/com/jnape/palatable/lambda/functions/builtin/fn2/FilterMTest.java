package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.FilterM.filterM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.GT.gt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.LT.lt;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
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

public class FilterMTest {

    @Test
    public void filterEmpty() {
        IterateT<Identity<?>, Integer> numbers = empty(pureIdentity());
        assertThat(filterM(constantly(true), numbers), isEmpty());
    }

    @Test
    public void filterNothing() {
        IterateT<Identity<?>, Integer> numbers = takeM(5, naturalNumbersM(pureIdentity()));
        assertThat(filterM(constantly(true), numbers), iterates(1, 2, 3, 4, 5));
    }

    @Test
    public void filterAll() {
        IterateT<Identity<?>, Integer> numbers = takeM(5, naturalNumbersM(pureIdentity()));
        assertThat(filterM(constantly(false), numbers), isEmpty());
    }

    @Test
    public void filterSome() {
        IterateT<Identity<?>, Integer> numbers = takeM(5, naturalNumbersM(pureIdentity()));
        assertThat(filterM(gt(3), numbers), iterates(4, 5));
        assertThat(filterM(lt(3), numbers), iterates(1, 2));
    }

    @Test
    public void filterSomeIntermittent() {
        IterateT<Identity<?>, Integer> numbers = takeM(10, naturalNumbersM(pureIdentity()));
        assertThat(filterM(i -> i % 2 == 0, numbers), iterates(2, 4, 6, 8, 10));
        assertThat(filterM(i -> i % 2 == 1, numbers), iterates(1, 3, 5, 7, 9));
    }

    @Test
    public void filterStackSafe() {
        IterateT<IO<?>, Integer> numbers = takeM(STACK_EXPLODING_NUMBER, naturalNumbersM(pureIO()));
        assertThat(filterM(constantly(true), numbers).fold((a, i) -> io(max(a, i)), io(0)),
                   yieldsValue(equalTo(STACK_EXPLODING_NUMBER)));
    }
}