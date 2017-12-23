package com.jnape.palatable.lambda.lens.lenses;

import com.jnape.palatable.lambda.adt.hlist.Index;
import org.junit.Test;

import static com.jnape.palatable.lambda.adt.hlist.HList.singletonHList;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.lens.lenses.HListLens.elementAt;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static testsupport.assertion.LensAssert.assertLensLawfulness;

public class HListLensTest {

    @Test
    public void elementAtFocusesOnInvariantElementAtIndex() {
        assertLensLawfulness(elementAt(Index.<String>index().<Integer>after().after()),
                             asList(tuple(true, 0, "foo"), tuple(true, 0, "foo", '1')),
                             singletonList("bar"));
    }

    @Test
    public void headFocusesOnHead() {
        assertLensLawfulness(HListLens.head(),
                             asList(singletonHList(2), tuple(2, "3"), tuple(2, "3", '4')),
                             singletonList(0));
    }

    @Test
    public void tailFocusesOnTail() {
        assertLensLawfulness(HListLens.tail(),
                             singletonList(tuple(2, "3", '4')),
                             asList(tuple("3", '5'), tuple("4", '4'), tuple("4", '5')));
    }
}