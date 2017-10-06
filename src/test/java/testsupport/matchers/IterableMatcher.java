package testsupport.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.Iterator;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

public class IterableMatcher<E> extends BaseMatcher<Iterable<E>> {

    private final Iterable<E> expected;

    private IterableMatcher(Iterable<E> expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object actual) {
        return actual instanceof Iterable && iterablesIterateSameElementsInOrder(expected, (Iterable) actual);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("<").appendText(stringify(expected)).appendText(">");
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        if (item instanceof Iterable) {
            if (description.toString().endsWith("but: "))
                description.appendText("was ");
            description.appendText("<").appendText(stringify((Iterable) item)).appendText(">");
        } else
            super.describeMismatch(item, description);
    }

    private boolean iterablesIterateSameElementsInOrder(Iterable expected, Iterable actual) {
        Iterator actualIterator = actual.iterator();
        Iterator expectedIterator = expected.iterator();

        while (expectedIterator.hasNext() && actualIterator.hasNext()) {
            Object nextExpected = expectedIterator.next();
            Object nextActual = actualIterator.next();

            if (nextExpected instanceof Iterable && nextActual instanceof Iterable) {
                if (!iterablesIterateSameElementsInOrder((Iterable) nextExpected, (Iterable) nextActual))
                    return false;
            } else if (!reflectionEquals(nextExpected, nextActual))
                return false;
        }

        return actualIterator.hasNext() == expectedIterator.hasNext();
    }

    private String stringify(Iterable iterable) {
        StringBuilder stringBuilder = new StringBuilder().append("[");
        Iterator iterator = iterable.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next instanceof Iterable)
                stringBuilder.append(stringify((Iterable) next));
            else
                stringBuilder.append(next);
            if (iterator.hasNext())
                stringBuilder.append(", ");
        }
        return stringBuilder.append("]").toString();
    }

    @SafeVarargs
    public static <E> IterableMatcher<E> iterates(E... es) {
        return new IterableMatcher<>(asList(es));
    }

    public static <E> IterableMatcher<E> isEmpty() {
        return new IterableMatcher<>(new ArrayList<>());
    }
}
