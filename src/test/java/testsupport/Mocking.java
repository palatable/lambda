package testsupport;

import org.mockito.stubbing.Answer;

import java.util.Iterator;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Mocking {

    @SuppressWarnings("unchecked")
    public static <A> Iterable<A> mockIterable() {
        Iterable<A> iterable = (Iterable<A>) mock(Iterable.class);
        when(iterable.iterator()).thenReturn((Iterator<A>) mock(Iterator.class));
        return iterable;
    }

    @SafeVarargs
    public static <T> void mockIteratorToHaveValues(Iterator iterator, T... values) {
        Iterator real = asList(values).iterator();

        when(iterator.hasNext()).then(delegateTo(real));
        when(iterator.next()).then(delegateTo(real));
    }

    public static Answer<?> delegateTo(final Object real) {
        return invocation -> invocation.getMethod().invoke(real);
    }
}
