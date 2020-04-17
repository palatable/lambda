package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.Equivalence;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;
import testsupport.traits.MonadReaderLaws;
import testsupport.traits.MonadRecLaws;
import testsupport.traits.MonadWriterLaws;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.StateMatcher.whenEvaluated;
import static testsupport.matchers.StateMatcher.whenExecuted;
import static testsupport.matchers.StateMatcher.whenRun;
import static testsupport.traits.Equivalence.equivalence;

@RunWith(Traits.class)
public class StateTest {

    @TestTraits({FunctorLaws.class,
                 ApplicativeLaws.class,
                 MonadLaws.class,
                 MonadRecLaws.class,
                 MonadReaderLaws.class,
                 MonadWriterLaws.class})
    public Equivalence<State<String, Integer>> testSubject() {
        return equivalence(State.gets(String::length), s -> s.run("foo"));
    }

    @Test
    public void eval() {
        assertThat(State.gets(id()), whenEvaluated(1, 1));
    }

    @Test
    public void exec() {
        assertThat(State.modify(x -> x + 1), whenExecuted(1, 2));
    }

    @Test
    public void get() {
        assertThat(State.get(), whenRun(1, 1, 1));
    }

    @Test
    public void put() {
        assertThat(State.put(1), whenRun(1, UNIT, 1));
    }

    @Test
    public void gets() {
        assertThat(State.gets(Integer::parseInt), whenRun("0", 0, "0"));
    }

    @Test
    public void modify() {
        assertThat(State.modify(x -> x + 1), whenRun(0, UNIT, 1));
    }

    @Test
    public void state() {
        assertThat(State.state(1), whenRun(UNIT, 1, UNIT));
        assertThat(State.state(x -> tuple(x + 1, x - 1)), whenRun(0, 1, -1));
    }

    @Test
    public void stateAccumulation() {
        assertThat(State.<Integer>get().flatMap(i -> State.put(i + 1).discardL(State.state(i))),
                   whenRun(0, 0, 1));
    }

    @Test
    public void zipOrdering() {
        assertThat(State.<String, Integer>state(s -> tuple(0, s + "1"))
                           .zip(State.state(s -> tuple(x -> x + 1, s + "2"))),
                   whenRun("_", 1, "_12"));
    }

    @Test
    public void withState() {
        assertThat(State.<Integer>get().withState(x -> x + 1), whenRun(0, 1, 1));
    }

    @Test
    public void mapState() {
        assertThat(State.<Integer>get().mapState(into((a, s) -> tuple(a + 1, s + 2))), whenRun(0, 1, 2));
    }

    @Test
    public void staticPure() {
        assertThat(State.<String>pureState().apply(1), whenRun("foo", 1, "foo"));
    }
}