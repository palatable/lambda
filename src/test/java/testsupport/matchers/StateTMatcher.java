package testsupport.matchers;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.These;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.StateT;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.io.IO.io;
import static org.hamcrest.Matchers.equalTo;

public class StateTMatcher<S, M extends MonadRec<?, M>, A, MA extends MonadRec<A, M>, MS extends MonadRec<S, M>, MTS extends MonadRec<Tuple2<A, S>, M>> extends TypeSafeMatcher<StateT<S, M, A>> {
    private final S initialState;

    private final Either<Matcher<? super MTS>, These<Matcher<? super MA>, Matcher<? super MS>>> matcher;

    private StateTMatcher(S initialState, These<Matcher<? super MA>, Matcher<? super MS>> matchers) {
        this.initialState = initialState;
        this.matcher = right(matchers);
    }

    private StateTMatcher(S initialState, Matcher<? super MTS> matcher) {
        this.initialState = initialState;
        this.matcher = left(matcher);
    }

    @Override
    protected boolean matchesSafely(StateT<S, M, A> item) {
        MonadRec<Tuple2<A, S>, M> ran = item.runStateT(initialState);
        return matcher.match(bothMatcher -> bothMatcher.matches(ran),
                theseMatchers -> theseMatchers.match(a -> a.matches(ran.fmap(Tuple2::_1)),
                        b -> b.matches(ran.fmap(Tuple2::_2)),
                        ab -> ab._1().matches(ran.fmap(Tuple2::_1)) && ab._2().matches(ran.fmap(Tuple2::_2))));
    }

    @Override
    public void describeTo(Description description) {
        matcher.match(bothMatcher -> io(() -> bothMatcher.describeTo(description.appendText("Value and state matching "))),
                theseMatchers -> theseMatchers.match(a -> io(() -> a.describeTo(description.appendText("Value matching "))),
                        b -> io(() -> b.describeTo(description.appendText("State matching "))),
                        ab -> io(() -> {
                            description.appendText("Value matching: ");
                            ab._1().describeTo(description);
                            description.appendText(" and state matching: ");
                            ab._2().describeTo(description);
                        })))
                .unsafePerformIO();
    }

    @Override
    protected void describeMismatchSafely(StateT<S, M, A> item, Description mismatchDescription) {
        MonadRec<Tuple2<A, S>, M> ran = item.runStateT(initialState);

        matcher.match(bothMatcher -> io(() -> {
                    mismatchDescription.appendText("value and state matching ");
                    bothMatcher.describeMismatch(ran, mismatchDescription);
                }),
                theseMatchers -> theseMatchers.match(a -> io(() -> {
                            mismatchDescription.appendText("value matching ");
                            a.describeMismatch(ran.fmap(Tuple2::_1), mismatchDescription);
                        }),
                        b -> io(() -> {
                            mismatchDescription.appendText("state matching ");
                            b.describeMismatch(ran.fmap(Tuple2::_2), mismatchDescription);
                        }),
                        ab -> io(() -> {
                            mismatchDescription.appendText("value matching: ");
                            ab._1().describeMismatch(ran.fmap(Tuple2::_1), mismatchDescription);
                            mismatchDescription.appendText(" and state matching: ");
                            ab._2().describeMismatch(ran.fmap(Tuple2::_2), mismatchDescription);
                        })))
                .unsafePerformIO();
    }

    public static <S, M extends MonadRec<?, M>, A, MA extends MonadRec<A, M>, MS extends MonadRec<S, M>, MTS extends MonadRec<Tuple2<A, S>, M>> StateTMatcher<S, M, A, MA, MS, MTS> whenRunWith(S initialState, Matcher<? super MTS> bothMatcher) {
        return new StateTMatcher<>(initialState, bothMatcher);
    }

    public static <S, M extends MonadRec<?, M>, A, MA extends MonadRec<A, M>, MS extends MonadRec<S, M>, MTS extends MonadRec<Tuple2<A, S>, M>> StateTMatcher<S, M, A, MA, MS, MTS> whenRun(S initialState, MTS both) {
        return whenRunWith(initialState, equalTo(both));
    }

    // Note: This constructor will run both matchers, which can run effects twice
    public static <S, M extends MonadRec<?, M>, A, MA extends MonadRec<A, M>, MS extends MonadRec<S, M>, MTS extends MonadRec<Tuple2<A, S>, M>> StateTMatcher<S, M, A, MA, MS, MTS> whenRunWith(S initialState, Matcher<? super MA> valueMatcher, Matcher<? super MS> stateMatcher) {
        return new StateTMatcher<>(initialState, These.both(valueMatcher, stateMatcher));
    }

    // Note: This constructor will run both matchers, which can run effects twice
    @SuppressWarnings("unused")
    public static <S, M extends MonadRec<?, M>, A, MA extends MonadRec<A, M>, MS extends MonadRec<S, M>, MTS extends MonadRec<Tuple2<A, S>, M>> StateTMatcher<S, M, A, MA, MS, MTS> whenRun(S initialState, MA value, MS state) {
        return whenRunWith(initialState, equalTo(value), equalTo(state));
    }

    public static <S, M extends MonadRec<?, M>, A, MA extends MonadRec<A, M>, MS extends MonadRec<S, M>, MTS extends MonadRec<Tuple2<A, S>, M>> StateTMatcher<S, M, A, MA, MS, MTS> whenExecutedWith(S initialState, Matcher<? super MS> stateMatcher) {
        return new StateTMatcher<>(initialState, These.b(stateMatcher));
    }

    public static <S, M extends MonadRec<?, M>, A, MA extends MonadRec<A, M>, MS extends MonadRec<S, M>, MTS extends MonadRec<Tuple2<A, S>, M>> StateTMatcher<S, M, A, MA, MS, MTS> whenExecuted(S initialState, MS state) {
        return whenExecutedWith(initialState, equalTo(state));
    }

    public static <S, M extends MonadRec<?, M>, A, MA extends MonadRec<A, M>, MS extends MonadRec<S, M>, MTS extends MonadRec<Tuple2<A, S>, M>> StateTMatcher<S, M, A, MA, MS, MTS> whenEvaluatedWith(S initialState, Matcher<? super MA> valueMatcher) {
        return new StateTMatcher<>(initialState, These.a(valueMatcher));
    }

    public static <S, M extends MonadRec<?, M>, A, MA extends MonadRec<A, M>, MS extends MonadRec<S, M>, MTS extends MonadRec<Tuple2<A, S>, M>> StateTMatcher<S, M, A, MA, MS, MTS> whenEvaluated(S initialState, MA value) {
        return whenEvaluatedWith(initialState, equalTo(value));
    }
}
