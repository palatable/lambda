package testsupport.matchers;

import com.jnape.loanshark.annotation.Todo;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Iterator;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

public class IterableMatcher<Element> extends BaseMatcher<Iterable<Element>> {

    private final int length;
    private final Iterable<Element> expected;

    public IterableMatcher(int length, Iterable<Element> expected) {
        this.length = length;
        this.expected = expected;
    }

    @Override
    public boolean matches(Object actual) {
        return actual instanceof Iterable && iterablesIterateSameElementsInOrder((Iterable) expected, (Iterable) actual) && finitelyIterable((Iterable) actual);
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

    @Todo(created = "2/17/2014", author = "jnape", description = "This is a disaster. So many methods in one")
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

    private boolean finitelyIterable(Iterable iterable) {
        Iterator iterator = iterable.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            iterator.next();
            if (i++ > length)
                return false;
        }
        return true;
    }

    @Todo(created = "02/17/2014", author = "jnape", description = "Pull this out into a separate class")
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

    public static <Element> IterableMatcher<Element> iterates(Element... elements) {
        return new IterableMatcher<Element>(elements.length, asList(elements));
    }

    public static <A> Matcher<Iterable<A>> isEmpty() {
        return new IterableMatcher<A>(0, new ArrayList<A>());
    }
}
