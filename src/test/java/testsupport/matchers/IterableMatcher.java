package testsupport.matchers;

import com.jnape.loanshark.annotation.Todo;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.Iterator;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

public class IterableMatcher extends BaseMatcher<Iterable> {

    private final Iterable expected;

    public IterableMatcher(Iterable expected) {
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
        if (item instanceof Iterable)
            description.appendText("was <").appendText(stringify((Iterable) item)).appendText(">");
        else
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

    @Todo(created = "12/23/2014", author = "jnape", description = "Pull this out into a separate class")
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
    public static <Element> IterableMatcher iterates(Element... elements) {
        return new IterableMatcher(asList(elements));
    }

    public static IterableMatcher isEmpty() {
        return new IterableMatcher(new ArrayList());
    }
}
