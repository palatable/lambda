package com.jnape.palatable.lambda.lens.lenses;

import org.junit.Test;

import java.util.HashSet;
import java.util.TreeSet;

import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static testsupport.assertion.LensAssert.assertLensLawfulness;

public class SetLensTest {

    @Test
    public void containsWithCopyFn() {
        assertLensLawfulness(SetLens.contains(HashSet::new, 1),
                             asList(emptySet(),
                                    singleton(1),
                                    singleton(2),
                                    new HashSet<>(asList(1, 2)),
                                    new HashSet<>(asList(2, 3))),
                             asList(true, false));
        assertThat(set(SetLens.contains(TreeSet::new, 1), true, emptySet()), instanceOf(TreeSet.class));
    }

    @Test
    public void containsWithoutCopyFn() {
        assertLensLawfulness(SetLens.contains(1),
                             asList(emptySet(),
                                    singleton(1),
                                    singleton(2),
                                    new HashSet<>(asList(1, 2)),
                                    new HashSet<>(asList(2, 3))),
                             asList(true, false));
        assertThat(set(SetLens.contains(1), true, emptySet()), instanceOf(HashSet.class));
    }
}