package com.jnape.palatable.lambda.internal.iteration;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyIterator;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SplicingIteratorTest {

    @Test
    public void hasNextBeforeTakingAnyElements() {
        List<Integer> original = asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> replacement = asList(100, 200, 300);
        SplicingIterator<Integer> splicingIterator = new SplicingIterator<>(2, 2, replacement.iterator(), original.iterator());
        assertThat(splicingIterator.hasNext(), is(true));
    }

    @Test
    public void doesNotHaveNextIfTakenEnoughElements() {
        List<Integer> original = asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> replacement = asList(100, 200, 300);
        SplicingIterator<Integer> splicingIterator = new SplicingIterator<>(2, 5, replacement.iterator(), original.iterator());
        splicingIterator.next();
        splicingIterator.next();
        splicingIterator.next();
        splicingIterator.next();
        splicingIterator.next();
        splicingIterator.next();
        assertThat(splicingIterator.hasNext(), is(false));
    }

    @Test
    public void emptyOriginal() {
        List<Integer> replacement = asList(1, 2, 3);
        SplicingIterator<Integer> splicingIterator = new SplicingIterator<>(1000, 2000, replacement.iterator(), emptyIterator());
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(1));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(2));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(3));
        assertThat(splicingIterator.hasNext(), is(false));
    }

    @Test
    public void ontoFrontWithoutReplacing() {
        List<Integer> original = asList(1, 2, 3);
        List<Integer> replacement = asList(100, 200);
        SplicingIterator<Integer> splicingIterator = new SplicingIterator<>(0, 0, replacement.iterator(), original.iterator());
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(100));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(200));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(1));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(2));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(3));
        assertThat(splicingIterator.hasNext(), is(false));
    }

    @Test
    public void ontoFrontWithReplacing() {
        List<Integer> original = asList(1, 2, 3);
        List<Integer> replacement = asList(100, 200);
        SplicingIterator<Integer> splicingIterator = new SplicingIterator<>(0, 2, replacement.iterator(), original.iterator());
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(100));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(200));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(3));
        assertThat(splicingIterator.hasNext(), is(false));
    }

    @Test
    public void intoMiddleWithoutReplacing() {
        List<Integer> original = asList(1, 2, 3);
        List<Integer> replacement = asList(100, 200);
        SplicingIterator<Integer> splicingIterator = new SplicingIterator<>(1, 0, replacement.iterator(), original.iterator());
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(1));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(100));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(200));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(2));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(3));
        assertThat(splicingIterator.hasNext(), is(false));
    }

    @Test
    public void intoMiddleWithReplacing() {
        List<Integer> original = asList(1, 2, 3);
        List<Integer> replacement = asList(100, 200);
        SplicingIterator<Integer> splicingIterator = new SplicingIterator<>(1, 1, replacement.iterator(), original.iterator());
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(1));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(100));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(200));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(3));
        assertThat(splicingIterator.hasNext(), is(false));
    }

    @Test
    public void intoMiddleReplacingWithEmpty() {
        List<Integer> original = asList(1, 2, 3);
        SplicingIterator<Integer> splicingIterator = new SplicingIterator<>(1, 1, emptyIterator(), original.iterator());
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(1));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(3));
        assertThat(splicingIterator.hasNext(), is(false));
    }

    @Test
    public void ontoEndWithoutReplacing() {
        List<Integer> original = asList(1, 2, 3);
        List<Integer> replacement = asList(100, 200);
        SplicingIterator<Integer> splicingIterator = new SplicingIterator<>(1000, 0, replacement.iterator(), original.iterator());
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(1));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(2));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(3));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(100));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(200));
        assertThat(splicingIterator.hasNext(), is(false));
    }

    @Test
    public void ontoEndWithReplacing() {
        List<Integer> original = asList(1, 2, 3);
        List<Integer> replacement = asList(100, 200);
        SplicingIterator<Integer> splicingIterator = new SplicingIterator<>(2, 1000, replacement.iterator(), original.iterator());
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(1));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(2));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(100));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(200));
        assertThat(splicingIterator.hasNext(), is(false));
    }

    @Test
    public void replacingEntireOriginal() {
        List<Integer> original = asList(1, 2, 3);
        List<Integer> replacement = asList(100, 200);
        SplicingIterator<Integer> splicingIterator = new SplicingIterator<>(0, 1000, replacement.iterator(), original.iterator());
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(100));
        assertThat(splicingIterator.hasNext(), is(true));
        assertThat(splicingIterator.next(), is(200));
        assertThat(splicingIterator.hasNext(), is(false));
    }

    @Test
    public void replacingEntireOriginalWithEmpty() {
        List<Integer> original = asList(1, 2, 3);
        SplicingIterator<Integer> splicingIterator = new SplicingIterator<>(0, 1000, emptyIterator(), original.iterator());
        assertThat(splicingIterator.hasNext(), is(false));
    }

}
