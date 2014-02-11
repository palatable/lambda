package testsupport;

import java.util.Iterator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Mocking {

    @SuppressWarnings("unchecked")
    public static <A> Iterable<A> mockIterable() {
        Iterable<A> iterable = (Iterable<A>) mock(Iterable.class);
        when(iterable.iterator()).thenReturn((Iterator<A>) mock(Iterator.class));
        return iterable;
    }
}
