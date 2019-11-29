package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.builtin.Identity;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.*;

import java.util.ArrayList;
import java.util.List;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static com.jnape.palatable.lambda.optics.functions.Set.set;
import static com.jnape.palatable.lambda.optics.lenses.ListLens.elementAt;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static testsupport.matchers.StateTMatcher.*;
import static testsupport.traits.Equivalence.equivalence;

@RunWith(Traits.class)
public class StateTTest {

    @TestTraits({FunctorLaws.class,
            ApplicativeLaws.class,
            MonadLaws.class,
            MonadRecLaws.class,
            MonadReaderLaws.class,
            MonadWriterLaws.class})
    public Equivalence<StateT<String, Identity<?>, Integer>> testReader() {
        return equivalence(StateT.gets(s -> new Identity<>(s.length())), s -> s.runStateT("foo"));
    }

    @Test
    public void evalAndExec() {
        StateT<String, Identity<?>, Integer> stateT =
                StateT.stateT(str -> new Identity<>(tuple(str.length(), str + "_")));

        assertThat(stateT, hasExecT("_", new Identity<>("__")));
        assertThat(stateT, hasEvalT("_", new Identity<>(1)));
    }

    @Test
    public void mapStateT() {
        StateT<String, Identity<?>, Integer> stateT =
                StateT.stateT(str -> new Identity<>(tuple(str.length(), str + "_")));

        assertThat(stateT.mapStateT(id -> id.<Identity<Tuple2<Integer, String>>>coerce()
                        .runIdentity()
                        .into((x, str) -> just(tuple(x + 1, str.toUpperCase())))),
                isStateT("abc", just(tuple(4, "ABC_"))));
    }

    @Test
    public void zipping() {
        StateT<List<String>, Identity<?>, Unit> result = StateT.<List<String>, Identity<?>>modify(
                s -> new Identity<>(set(elementAt(s.size()), just("one"), s)))
                .discardL(StateT.modify(s -> new Identity<>(set(elementAt(s.size()), just("two"), s))));

        assertThat(result,
            isStateT(new ArrayList<>(), new Identity<>(tuple(UNIT, asList("one", "two")))));
    }

    @Test
    public void withStateT() {
        StateT<String, Identity<?>, Integer> stateT =
                StateT.stateT(str -> new Identity<>(tuple(str.length(), str + "_")));
        assertThat(stateT.withStateT(str -> new Identity<>(str.toUpperCase())),
                isStateT("abc", new Identity<>(tuple(3, "ABC_"))));
    }

    @Test
    public void get() {
        assertThat(StateT.get(pureIdentity()),
                isStateT("state", new Identity<>(tuple("state", "state"))));
    }

    @Test
    public void gets() {
        assertThat(StateT.gets(s -> new Identity<>(s.length())),
                isStateT("state", new Identity<>(tuple(5, "state"))));
    }

    @Test
    public void put() {
        assertThat(StateT.put(new Identity<>(1)),
                isStateT(0, new Identity<>(tuple(UNIT, 1))));
    }

    @Test
    public void modify() {
        assertThat(StateT.modify(x -> new Identity<>(x + 1)),
                isStateT(0, new Identity<>(tuple(UNIT, 1))));
    }

    @Test
    public void stateT() {
        assertThat(StateT.stateT(new Identity<>(0)),
                isStateT("_", new Identity<>(tuple(0, "_"))));
        assertThat(StateT.stateT(s -> new Identity<>(tuple(s.length(), s + "1"))),
                isStateT("_", new Identity<>(tuple(1, "_1"))));
    }

    @Test
    public void staticPure() {
        assertThat(StateT.<String, Identity<?>>pureStateT(pureIdentity()).apply(1),
                isStateT("foo", new Identity<>(tuple(1, "foo"))));
    }

    @Test
    public void staticLift() {
        assertThat(StateT.<String>liftStateT().apply(new Identity<>(1)),
                isStateT("foo", new Identity<>(tuple(1, "foo"))));
    }
}