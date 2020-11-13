package com.jnape.palatable.lambda.functions.builtin.fn1;

import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Index.index;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Replicate.replicate;
import static org.junit.Assert.*;
import static testsupport.matchers.IterableMatcher.iterates;

public class IndexTest {

    @Test
    public void indexes() {
        Iterable<Character> as = replicate(5, 'a');
        assertThat(index(as), iterates(tuple(1, 'a'), tuple(2, 'a'), tuple(3, 'a'), tuple(4, 'a'), tuple(5, 'a')));
    }
}