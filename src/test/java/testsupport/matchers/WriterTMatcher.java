package testsupport.matchers;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.WriterT;
import com.jnape.palatable.lambda.monoid.Monoid;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public final class WriterTMatcher<W, M extends MonadRec<?, M>, A> extends
        TypeSafeMatcher<MonadRec<A, WriterT<W, M, ?>>> {

    private final Matcher<? extends MonadRec<Tuple2<A, W>, M>> expected;
    private final Monoid<W>                                    wMonoid;

    private WriterTMatcher(Matcher<? extends MonadRec<Tuple2<A, W>, M>> expected, Monoid<W> wMonoid) {
        this.wMonoid = wMonoid;
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(MonadRec<A, WriterT<W, M, ?>> item) {
        return expected.matches(item.<WriterT<W, M, A>>coerce().runWriterT(wMonoid));
    }

    @Override
    public void describeTo(Description description) {
        expected.describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(MonadRec<A, WriterT<W, M, ?>> item, Description mismatchDescription) {
        expected.describeMismatch(item.<WriterT<W, M, A>>coerce().runWriterT(wMonoid), mismatchDescription);
    }

    public static <W, M extends MonadRec<?, M>, A, MAW extends MonadRec<Tuple2<A, W>, M>>
    WriterTMatcher<W, M, A> whenRunWith(Monoid<W> wMonoid, Matcher<MAW> matcher) {
        return new WriterTMatcher<>(matcher, wMonoid);
    }

    public static <W, M extends MonadRec<?, M>, A, MW extends MonadRec<? extends W, M>>
    WriterTMatcher<W, M, A> whenExecutedWith(Monoid<W> wMonoid, Matcher<MW> matcher) {
        return whenRunWith(wMonoid, new TypeSafeMatcher<MonadRec<Tuple2<A, W>, M>>() {
            @Override
            protected boolean matchesSafely(MonadRec<Tuple2<A, W>, M> item) {
                return matcher.matches(item.fmap(Tuple2::_2));
            }

            @Override
            public void describeTo(Description description) {
                matcher.describeTo(description);
            }

            @Override
            protected void describeMismatchSafely(MonadRec<Tuple2<A, W>, M> item, Description mismatchDescription) {
                matcher.describeMismatch(item.fmap(Tuple2::_2), mismatchDescription);
            }
        });
    }

    public static <W, M extends MonadRec<?, M>, A, MA extends MonadRec<A, M>>
    WriterTMatcher<W, M, A> whenEvaluatedWith(Monoid<W> wMonoid, Matcher<MA> matcher) {
        return whenRunWith(wMonoid, new TypeSafeMatcher<MonadRec<Tuple2<A, W>, M>>() {
            @Override
            protected boolean matchesSafely(MonadRec<Tuple2<A, W>, M> item) {
                return matcher.matches(item.fmap(Tuple2::_1));
            }

            @Override
            public void describeTo(Description description) {
                matcher.describeTo(description);
            }

            @Override
            protected void describeMismatchSafely(MonadRec<Tuple2<A, W>, M> item, Description mismatchDescription) {
                matcher.describeMismatch(item.fmap(Tuple2::_1), mismatchDescription);
            }
        });
    }
}