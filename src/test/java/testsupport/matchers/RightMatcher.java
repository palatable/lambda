package testsupport.matchers;

import com.jnape.palatable.lambda.adt.Either;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.io.IO.io;

public final class RightMatcher<L, R> extends TypeSafeMatcher<Either<L, R>> {

    private final Matcher<R> rMatcher;

    private RightMatcher(Matcher<R> rMatcher) {
        this.rMatcher = rMatcher;
    }

    @Override
    protected boolean matchesSafely(Either<L, R> actual) {
        return actual.match(constantly(false), rMatcher::matches);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Right value of ");
        rMatcher.describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(Either<L, R> item, Description mismatchDescription) {
        mismatchDescription.appendText("was ");
        item.match(l -> io(() -> mismatchDescription.appendValue(item)),
                   r -> io(() -> {
                       mismatchDescription.appendText("Right value of ");
                       rMatcher.describeMismatch(r, mismatchDescription);
                   }))
                .unsafePerformIO();
    }

    public static <L, R> RightMatcher<L, R> isRightThat(Matcher<R> rMatcher) {
        return new RightMatcher<>(rMatcher);
    }
}
