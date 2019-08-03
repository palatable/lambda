package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.builtin.Identity;
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

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functor.builtin.Identity.pureIdentity;
import static org.junit.Assert.assertEquals;
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

        assertEquals(new Identity<>("__"), stateT.execT("_"));
        assertEquals(new Identity<>(1), stateT.evalT("_"));
    }

    @Test
    public void mapStateT() {
        StateT<String, Identity<?>, Integer> stateT =
                StateT.stateT(str -> new Identity<>(tuple(str.length(), str + "_")));
        assertEquals(just(tuple(4, "ABC_")),
                     stateT.mapStateT(id -> id.<Identity<Tuple2<Integer, String>>>coerce()
                             .runIdentity()
                             .into((x, str) -> just(tuple(x + 1, str.toUpperCase()))))
                             .<Maybe<Tuple2<Integer, String>>>runStateT("abc"));
    }

    @Test
    public void zipping() {
        assertEquals(new Identity<>(tuple(4, "final state: FOO")),
                     StateT.<String, Identity<?>>modify(s -> new Identity<>(s.toUpperCase()))
                             .discardL(StateT.gets(s -> new Identity<>(s.length())))
                             .flatMap(x -> StateT.stateT(s -> new Identity<>(tuple(x + 1, "final state: " + s))))
                             .<Identity<Tuple2<Integer, String>>>runStateT("foo"));
    }

    @Test
    public void withStateT() {
        StateT<String, Identity<?>, Integer> stateT =
                StateT.stateT(str -> new Identity<>(tuple(str.length(), str + "_")));
        assertEquals(new Identity<>(tuple(3, "ABC_")),
                     stateT.withStateT(str -> new Identity<>(str.toUpperCase())).runStateT("abc"));
    }

    @Test
    public void get() {
        assertEquals(new Identity<>(tuple("state", "state")),
                     StateT.<String, Identity<?>>get(pureIdentity()).runStateT("state"));
    }

    @Test
    public void gets() {
        assertEquals(new Identity<>(tuple(5, "state")),
                     StateT.<String, Identity<?>, Integer>gets(s -> new Identity<>(s.length())).runStateT("state"));
    }

    @Test
    public void put() {
        assertEquals(new Identity<>(tuple(UNIT, 1)), StateT.put(new Identity<>(1)).runStateT(0));
    }

    @Test
    public void modify() {
        assertEquals(new Identity<>(tuple(UNIT, 1)),
                     StateT.<Integer, Identity<?>>modify(x -> new Identity<>(x + 1)).runStateT(0));
    }

    @Test
    public void stateT() {
        assertEquals(new Identity<>(tuple(0, "_")),
                     StateT.<String, Identity<?>, Integer>stateT(new Identity<>(0)).runStateT("_"));
        assertEquals(new Identity<>(tuple(1, "_1")),
                     StateT.<String, Identity<?>, Integer>stateT(s -> new Identity<>(tuple(s.length(), s + "1")))
                             .runStateT("_"));
    }

    @Test
    public void staticPure() {
        assertEquals(new Identity<>(tuple(1, "foo")),
                     StateT.<String, Identity<?>>pureStateT(pureIdentity())
                             .<Integer, StateT<String, Identity<?>, Integer>>apply(1)
                             .<Identity<Tuple2<Integer, String>>>runStateT("foo"));
    }

    @Test
    public void staticLift() {
        assertEquals(new Identity<>(tuple(1, "foo")),
                     StateT.<String>liftStateT().<Integer, Identity<?>, StateT<String, Identity<?>, Integer>>apply(new Identity<>(1))
                             .<Identity<Tuple2<Integer, String>>>runStateT("foo"));
    }
}