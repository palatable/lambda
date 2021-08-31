package testsupport.matchers;

import com.jnape.palatable.lambda.adt.Either;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.io.IO.io;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;

public final class EitherMatcher<L, R> extends TypeSafeMatcher<Either<L, R>> {
    private final Either<Matcher<? super L>, Matcher<? super R>> matcher;

    private EitherMatcher(Either<Matcher<? super L>, Matcher<? super R>> matcher) {
        this.matcher = matcher;
    }

    @Override
    protected void describeMismatchSafely(Either<L, R> item, Description mismatchDescription) {
        mismatchDescription.appendText("was ");
        item.match(l -> matcher.match(lMatcher -> io(() -> lMatcher.describeMismatch(l, mismatchDescription)),
                                      rMatcher -> io(() -> mismatchDescription.appendValue(item))),
                   r -> matcher.match(lMatcher -> io(() -> mismatchDescription.appendValue(item)),
                                      lMatcher -> io(() -> lMatcher.describeMismatch(r, mismatchDescription))))
            .unsafePerformIO();
    }

    @Override
    protected boolean matchesSafely(Either<L, R> actual) {
        return actual.match(l -> matcher.match(lMatcher -> lMatcher.matches(l),
                                               constantly(false)),
                            r -> matcher.match(constantly(false),
                                               rMatcher -> rMatcher.matches(r)));
    }

    @Override
    public void describeTo(Description description) {
        matcher.match(l -> io(() -> description.appendText("Left value of "))
                          .flatMap(constantly(io(() -> l.describeTo(description)))),
                      r -> io(() -> description.appendText("Right value of "))
                          .flatMap(constantly(io(() -> r.describeTo(description)))))
            .unsafePerformIO();
    }

    public static <L, R> EitherMatcher<L, R> isLeftThat(Matcher<? super L> lMatcher) {
        return new EitherMatcher<>(left(lMatcher));
    }

    public static <L, R> EitherMatcher<L, R> isLeft() {
        return isLeftThat(anything());
    }

    public static <L, R> EitherMatcher<L, R> isLeftOf(L l) {
        return isLeftThat(equalTo(l));
    }

    public static <L, R> EitherMatcher<L, R> isRightThat(Matcher<? super R> rMatcher) {
        return new EitherMatcher<>(right(rMatcher));
    }

    public static <L, R> EitherMatcher<L, R> isRight() {
        return isRightThat(anything());
    }

    public static <L, R> EitherMatcher<L, R> isRightOf(R r) {
        return isRightThat(equalTo(r));
    }
}
