package testsupport.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Iterator;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

public class IterableMatcher<Element> extends BaseMatcher<Iterable<Element>> {

    private final Iterable<Element> expected;

    public IterableMatcher(Iterable<Element> expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object actual) {
        return actual instanceof Iterable && iteratesExpectedElementsInOrder((Iterable) actual);
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        if (item instanceof Iterable)
            description.appendText("was ").appendText(stringify((Iterable) item));
        else
            super.describeMismatch(item, description);
    }

    private boolean iteratesExpectedElementsInOrder(Iterable actual) {
        Iterator actualIterator = actual.iterator();
        Iterator expectedIterator = expected.iterator();

        while (actualIterator.hasNext() && expectedIterator.hasNext())
            if (!reflectionEquals(actualIterator.next(), expectedIterator.next()))
                return false;

        return actualIterator.hasNext() == expectedIterator.hasNext();
    }

    private String stringify(Iterable iterable) {
        StringBuilder stringBuilder = new StringBuilder().append("<[");
        Iterator iterator = iterable.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next());
            if (iterator.hasNext())
                stringBuilder.append(", ");
        }
        return stringBuilder.append("]>").toString();
    }

    public static <Element> IterableMatcher<Element> iterates(Element... elements) {
        return new IterableMatcher<Element>(asList(elements));
    }
}
