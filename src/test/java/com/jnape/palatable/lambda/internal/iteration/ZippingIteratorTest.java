package com.jnape.palatable.lambda.internal.iteration;

import com.jnape.palatable.lambda.functions.Fn2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ZippingIteratorTest {

    @Mock private Fn2<Object, Object, Object> zipper;
    @Mock private Iterator<Object>            as;
    @Mock private Iterator<Object>            bs;

    private ZippingIterator<Object, Object, Object> zippingIterator;

    @Before
    public void setUp() {
        zippingIterator = new ZippingIterator<>(zipper, as, bs);
    }

    @Test
    public void hasNextIfMoreAsAndMoreBs() {
        when(as.hasNext()).thenReturn(true);
        when(bs.hasNext()).thenReturn(true);
        assertThat(zippingIterator.hasNext(), is(true));
    }

    @Test
    public void doesNotHaveNextIfAsDoesNotHaveNext() {
        when(as.hasNext()).thenReturn(false);
        assertThat(zippingIterator.hasNext(), is(false));
    }

    @Test
    public void doesNotHaveNextIfBsDoesNotHaveNext() {
        when(as.hasNext()).thenReturn(true);
        when(bs.hasNext()).thenReturn(false);
        assertThat(zippingIterator.hasNext(), is(false));
    }

    @Test
    public void zipsNextElementFromAsAndBs() {
        when(as.next()).thenReturn(1);
        when(bs.next()).thenReturn(2);

        zippingIterator.next();

        verify(zipper).apply(1, 2);
    }
}
