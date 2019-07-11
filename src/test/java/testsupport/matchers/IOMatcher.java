package testsupport.matchers;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.io.IO;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.atomic.AtomicReference;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static org.hamcrest.Matchers.anything;

public final class IOMatcher<A> extends TypeSafeMatcher<IO<A>> {

    private final Either<Matcher<? super Throwable>, Matcher<? super A>> matcher;
    private final AtomicReference<Either<Throwable, A>>                  resultRef;

    private IOMatcher(Either<Matcher<? super Throwable>, Matcher<? super A>> matcher) {
        this.matcher = matcher;
        resultRef = new AtomicReference<>();
    }

    @Override
    protected boolean matchesSafely(IO<A> io) {
        Either<Throwable, A> res = io.safe().unsafePerformIO();
        resultRef.set(res);
        return res.match(t -> matcher.match(tMatcher -> tMatcher.matches(t), aMatcher -> false),
                         a -> matcher.match(tMatcher -> false, aMatcher -> aMatcher.matches(a)));
    }

    @Override
    public void describeTo(Description description) {
        throw new UnsupportedOperationException();
    }

    public static <A> IOMatcher<A> isIOThat(Matcher<? super A> matcher) {
        return new IOMatcher<>(right(matcher));
    }

    public static <A> IOMatcher<A> isIOThatCompletesNormally() {
        return isIOThat(anything());
    }

    public static <A> IOMatcher<A> isIOThatThrows(Matcher<? super Throwable> throwableMatcher) {
        return new IOMatcher<>(left(throwableMatcher));
    }
}
