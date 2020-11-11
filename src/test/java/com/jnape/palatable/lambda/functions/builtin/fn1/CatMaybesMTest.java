package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.functions.builtin.fn1.CatMaybesM.catMaybesM;
import static com.jnape.palatable.lambda.functions.builtin.fn1.RepeatM.repeatM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.monad.transformer.builtin.IterateT.empty;
import static com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT.maybeT;
import static org.hamcrest.MatcherAssert.assertThat;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static testsupport.matchers.IterateTMatcher.isEmpty;
import static testsupport.matchers.IterateTMatcher.iterates;

public class CatMaybesMTest {

    @Test
    public void emptyStaysEmpty() {
        assertThat(catMaybesM(empty(pureIdentity())), isEmpty());
    }

    @Test
    public void nothingButNothing() {
        assertThat(catMaybesM(takeM(4, repeatM(new Identity<>(nothing())))), isEmpty());
    }

    @Test
    public void justAFew() {
        IterateT<Identity<?>, Maybe<Integer>> numbers =
                maybeT(naturalNumbersM(pureIdentity()).fmap(Maybe::just)).filter(x -> x % 2 == 0).runMaybeT();
        assertThat(takeM(3, catMaybesM(numbers)), iterates(2, 4, 6));
    }
}
