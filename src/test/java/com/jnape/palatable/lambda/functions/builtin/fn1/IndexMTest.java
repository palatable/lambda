package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.IndexM.indexM;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ReplicateM.replicateM;
import static org.junit.Assert.*;
import static testsupport.matchers.IterateTMatcher.iterates;

public class IndexMTest {

    @Test
    public void indexes() {
        IterateT<Identity<?>, Character> as = replicateM(5, new Identity<>('a'));
        assertThat(indexM(as), iterates(tuple(1, 'a'),
                                        tuple(2, 'a'),
                                        tuple(3, 'a'),
                                        tuple(4, 'a'),
                                        tuple(5, 'a')));
    }
}