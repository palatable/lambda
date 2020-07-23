package testsupport.matchers;

import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.lambda.monad.transformer.builtin.IterateT;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.LinkedList;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public final class IterateTMatcher<A> extends TypeSafeMatcher<IterateT<Identity<?>, A>> {
    private final Iterable<A> as;

    private IterateTMatcher(Iterable<A> as) {
        this.as = as;
    }

    @Override
    protected boolean matchesSafely(IterateT<Identity<?>, A> iterateT) {
        Identity<LinkedList<A>> fold = iterateT.fold((as, a) -> {
            as.add(a);
            return new Identity<>(as);
        }, new Identity<>(new LinkedList<>()));
        LinkedList<A> as = fold.runIdentity();
        return IterableMatcher.iteratesAll(this.as).matches(as);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an IterateT iterating " + as.toString() + " inside Identity");
    }

    public static <A> IterateTMatcher<A> iteratesAll(Iterable<A> as) {
        return new IterateTMatcher<>(as);
    }

    public static <A> IterateTMatcher<A> isEmpty() {
        return new IterateTMatcher<>(emptyList());
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <A> IterateTMatcher<A> iterates(A... as) {
        return iteratesAll(asList(as));
    }
}
