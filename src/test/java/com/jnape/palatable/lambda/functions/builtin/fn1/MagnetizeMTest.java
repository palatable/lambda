package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn1.MagnetizeM.magnetizeM;
import static com.jnape.palatable.lambda.functions.builtin.fn1.RepeatM.repeatM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.TakeM.takeM;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.*;
import static com.jnape.palatable.lambda.functions.builtin.fn1.NaturalNumbersM.naturalNumbersM;
import static testsupport.matchers.IterateTMatcher.iterates;

public class MagnetizeMTest {

    @Test
    public void groupsLikeElements() {
        IterateT<Identity<?>, Integer> numbers = takeM(10, naturalNumbersM(pureIdentity()).flatMap(n -> takeM(n, repeatM(new Identity<>(n)))));
        List<IterateT<Identity<?>, Integer>> actual = magnetizeM(numbers)
                .<List<IterateT<Identity<?>, Integer>>, Identity<List<IterateT<Identity<?>, Integer>>>>toCollection(ArrayList::new)
                .runIdentity();

        assertThat(actual, contains(iterates(1),
                                    iterates(2, 2),
                                    iterates(3, 3, 3),
                                    iterates(4, 4, 4, 4)));
    }
}