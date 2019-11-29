package testsupport.matchers;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.builtin.State;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.Effect.noop;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.io.IO.io;
import static org.hamcrest.Matchers.equalTo;

public class StateMatcher<S, A> extends TypeSafeMatcher<State<S, A>> {
    private final S initialState;
    private final Maybe<Matcher<? super S>> stateMatcher;
    private final Maybe<Matcher<? super A>> valueMatcher;

    private StateMatcher(S initialState, Maybe<Matcher<? super A>> valueMatcher, Maybe<Matcher<? super S>> stateMatcher) {
        this.initialState = initialState;
        this.stateMatcher = stateMatcher;
        this.valueMatcher = valueMatcher;
    }

    @Override
    protected boolean matchesSafely(State<S, A> item) {
        Tuple2<A, S> run = item.run(initialState);
        return stateMatcher.match(constantly(true), matcher -> matcher.matches(run._2())) &&
                valueMatcher.match(constantly(true), matcher -> matcher.matches(run._1()));
    }

    @Override
    public void describeTo(Description description) {
        stateMatcher.match(noop(),
                matcher -> io(() -> matcher.describeTo(description.appendText("State matching "))))
                .discardL(valueMatcher.match(noop(),
                        matcher -> io(() -> matcher.describeTo(description.appendText("Value matching ")))))
                .unsafePerformIO();
    }

    @Override
    protected void describeMismatchSafely(State<S, A> item, Description mismatchDescription) {
        io(() -> item.run(initialState))
                .flatMap(as ->
                        stateMatcher.fmap(matcher -> io(() -> {
                            mismatchDescription.appendText("state matching ");
                            mismatchDescription.appendValue(as._2());
                        })).orElse(io(UNIT))
                                .flatMap(constantly(valueMatcher.fmap(matcher -> io(() -> {
                                    mismatchDescription.appendText("value matching ");
                                    mismatchDescription.appendValue(as._1());
                                })).orElse(io(UNIT)))))
                .unsafePerformIO();
    }

    public static <S, A> StateMatcher<S, A> isStateThat(S initialState, Matcher<? super A> valueMatcher, Matcher<? super S> stateMatcher) {
        return new StateMatcher<>(initialState, just(valueMatcher), just(stateMatcher));
    }

    public static <S, A> StateMatcher<S, A> isState(S initialState, A value, S state) {
        return isStateThat(initialState, equalTo(value), equalTo(state));
    }

    public static <S, A> StateMatcher<S, A> hasExecThat(S initialState, Matcher<? super S> stateMatcher) {
        return new StateMatcher<>(initialState, nothing(), just(stateMatcher));
    }

    public static <S, A> StateMatcher<S, A> hasExec(S initialState, S state) {
        return hasExecThat(initialState, equalTo(state));
    }

    public static <S, A> StateMatcher<S, A> hasEvalThat(S initialState, Matcher<? super A> valueMatcher) {
        return new StateMatcher<>(initialState, just(valueMatcher), nothing());
    }

    public static <S, A> StateMatcher<S, A> hasEval(S initialState, A value) {
        return hasEvalThat(initialState, equalTo(value));
    }
}
