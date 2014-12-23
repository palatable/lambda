package com.jnape.palatable.lambda.iterators;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static testsupport.Mocking.mockIteratorToHaveValues;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(MockitoJUnitRunner.class)
public class GroupingIteratorTest {

    @Mock private Iterator<Object> as;

    private GroupingIterator groupingIterator;

    @Before
    public void setUp() {
        groupingIterator = new GroupingIterator<>(2, as);
    }

    @Test
    public void hasNextIfIteratorHasKMoreElements() {
        mockIteratorToHaveValues(as, 1, 2);
        assertThat(groupingIterator.hasNext(), is(true));
    }

    @Test
    public void hasNextIfIteratorHasLessThanKElementsLeft() {
        mockIteratorToHaveValues(as, 1);
        assertThat(groupingIterator.hasNext(), is(true));
    }

    @Test
    public void doesNotHaveNextIfIteratorHasNoElementsLeft() {
        assertThat(groupingIterator.hasNext(), is(false));
    }

    @Test
    public void groupsElementsInKSlices() {
        mockIteratorToHaveValues(as, 1, 2, 3, 4);
        assertThat(groupingIterator.next(), iterates(1, 2));
        assertThat(groupingIterator.next(), iterates(3, 4));
    }
}
