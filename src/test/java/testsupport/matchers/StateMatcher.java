package testsupport.matchers;

import com.jnape.palatable.lambda.adt.These;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.builtin.State;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.jnape.palatable.lambda.adt.These.*;
import static com.jnape.palatable.lambda.io.IO.io;
import static org.hamcrest.Matchers.equalTo;

public class StateMatcher<S, A> extends TypeSafeMatcher<State<S, A>> {
    private final S initialState;
    private final These<Matcher<? super A>, Matcher<? super S>> matchers;

    private StateMatcher(S initialState, These<Matcher<? super A>, Matcher<? super S>> matchers) {
        this.initialState = initialState;
        this.matchers = matchers;
    }

    @Override
    protected boolean matchesSafely(State<S, A> item) {
        Tuple2<A, S> ran = item.run(initialState);
        return matchers.match(a -> a.matches(ran._1()),
                b -> b.matches(ran._2()),
                ab -> ab._1().matches(ran._1()) && ab._2().matches(ran._2()));
    }

    @Override
    public void describeTo(Description description) {
        matchers.match(a -> io(() -> a.describeTo(description.appendText("Value matching "))),
                b -> io(() -> b.describeTo(description.appendText("State matching "))),
                ab -> io(() -> {
                    description.appendText("Value matching: ");
                    ab._1().describeTo(description);
                    description.appendText(" and state matching: ");
                    ab._2().describeTo(description);
                }))
                .unsafePerformIO();
    }

    @Override
    protected void describeMismatchSafely(State<S, A> item, Description mismatchDescription) {
        Tuple2<A, S> ran = item.run(initialState);
        matchers.match(a -> io(() -> {
                    mismatchDescription.appendText("value matching ");
                    a.describeMismatch(ran._1(), mismatchDescription);
                }),
                b -> io(() -> {
                    mismatchDescription.appendText("state matching ");
                    b.describeMismatch(ran._2(), mismatchDescription);
                }),
                ab -> io(() -> {
                    mismatchDescription.appendText("value matching: ");
                    ab._1().describeMismatch(ran._1(), mismatchDescription);
                    mismatchDescription.appendText(" and state matching: ");
                    ab._2().describeMismatch(ran._2(), mismatchDescription);
                }))
                .unsafePerformIO();
    }

    public static <S, A> StateMatcher<S, A> whenRunWith(S initialState, Matcher<? super A> valueMatcher, Matcher<? super S> stateMatcher) {
        return new StateMatcher<>(initialState, both(valueMatcher, stateMatcher));
    }

    @SuppressWarnings("unused")
    public static <S, A> StateMatcher<S, A> whenRun(S initialState, A value, S state) {
        return whenRunWith(initialState, equalTo(value), equalTo(state));
    }

    public static <S, A> StateMatcher<S, A> whenExecutedWith(S initialState, Matcher<? super S> stateMatcher) {
        return new StateMatcher<>(initialState, b(stateMatcher));
    }

    @SuppressWarnings("unused")
    public static <S, A> StateMatcher<S, A> whenExecuted(S initialState, S state) {
        return whenExecutedWith(initialState, equalTo(state));
    }

    public static <S, A> StateMatcher<S, A> whenEvaluatedWith(S initialState, Matcher<? super A> valueMatcher) {
        return new StateMatcher<>(initialState, a(valueMatcher));
    }

    @SuppressWarnings("unused")
    public static <S, A> StateMatcher<S, A> whenEvaluated(S initialState, A value) {
        return whenEvaluatedWith(initialState, equalTo(value));
    }
}
