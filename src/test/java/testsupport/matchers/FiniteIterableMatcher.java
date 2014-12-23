package testsupport.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class FiniteIterableMatcher extends BaseMatcher<Iterable> {

    @Override
    public boolean matches(Object item) {
        return item instanceof Iterable && supportsLessThanInfiniteIterations((Iterable) item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("finitely iterable");
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description.appendText("wasn't");
    }

    @SuppressWarnings("UnusedDeclaration")
    private boolean supportsLessThanInfiniteIterations(Iterable iterable) {
        long sufficientlyInfinite = 1000000;
        long elementsIterated = 0;
        for (Object ignored : iterable)
            if (elementsIterated++ > sufficientlyInfinite)
                return false;
        return true;
    }

    public static FiniteIterableMatcher finitelyIterable() {
        return new FiniteIterableMatcher();
    }
}
