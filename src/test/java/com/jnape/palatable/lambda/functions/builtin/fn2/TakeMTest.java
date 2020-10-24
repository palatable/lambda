package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;

import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.io.IO.pureIO;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.empty;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.of;
import static java.util.Arrays.asList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static testsupport.Constants.STACK_EXPLODING_NUMBER;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static testsupport.matchers.IOMatcher.yieldsValue;
import static testsupport.matchers.IterateTMatcher.isEmpty;
import static testsupport.matchers.IterateTMatcher.iteratesAll;

public class TakeMTest {

    @Test
    public void takeEmpty() {
        IterateT<Identity<?>, Integer> numbers = empty(pureIdentity());
        assertThat(takeM(5, numbers), isEmpty());
    }

    @Test
    public void takeAllShort() {
        IterateT<Identity<?>, Integer> numbers = of(new Identity<>(1), new Identity<>(2), new Identity<>(3));
        assertThat(takeM(5, numbers), iteratesAll(asList(1, 2, 3)));
    }

    @Test
    public void takeAll() {
        IterateT<Identity<?>, Integer> numbers = of(new Identity<>(1), new Identity<>(2), new Identity<>(3),
                                                    new Identity<>(4), new Identity<>(5));
        assertThat(takeM(5, numbers), iteratesAll(asList(1, 2, 3, 4, 5)));
    }

    @Test
    public void takeSome() {
        IterateT<Identity<?>, Integer> numbers = of(new Identity<>(1), new Identity<>(2), new Identity<>(3),
                                                    new Identity<>(4), new Identity<>(5), new Identity<>(6));
        assertThat(takeM(5, numbers), iteratesAll(asList(1, 2, 3, 4, 5)));
    }

    @Test
    public void takeSomeInfinite() {
        IterateT<Identity<?>, Integer> numbers = naturalNumbersM(pureIdentity());
        assertThat(takeM(5, numbers), iteratesAll(asList(1, 2, 3, 4, 5)));
    }

    @Test
    public void takeStackSafe() {
        IterateT<IO<?>, Integer> numbers = naturalNumbersM(pureIO());
        assertThat(takeM(STACK_EXPLODING_NUMBER, numbers).fold((a, i) -> io(a + i), io(0)),
                   yieldsValue(equalTo((STACK_EXPLODING_NUMBER + 1) * (STACK_EXPLODING_NUMBER / 2))));
    }
}