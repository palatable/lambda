package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.hlist.HList;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.EquatableM;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.adt.product.Product2.product;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static org.junit.Assert.assertEquals;

@RunWith(Traits.class)
public class StateTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
    public EquatableM<State<Unit, ?>, Unit> testSubject() {
        return new EquatableM<>(State.get(), state -> state.run(UNIT).into(HList::tuple));
    }

    @Test
    public void eval() {
        State<Integer, Unit> state = State.put(0);
        assertEquals(state.run(1)._1(), state.eval(1));
    }

    @Test
    public void exec() {
        State<Integer, Unit> state = State.put(0);
        assertEquals(state.run(1)._2(), state.exec(1));
    }

    @Test
    public void get() {
        assertEquals(tuple(1, 1), State.<Integer>get().run(1));
    }

    @Test
    public void put() {
        assertEquals(tuple(UNIT, 1), State.put(1).run(1));
    }

    @Test
    public void gets() {
        assertEquals(tuple(0, "0"), State.<String, Integer>gets(Integer::parseInt).run("0"));
    }

    @Test
    public void modify() {
        assertEquals(tuple(UNIT, 1), State.<Integer>modify(x -> x + 1).run(0));
    }

    @Test
    public void state() {
        assertEquals(tuple(1, UNIT), State.<Unit, Integer>state(1).run(UNIT));
        assertEquals(tuple(1, -1), State.<Integer, Integer>state(x -> product(x + 1, x - 1)).run(0));
    }

    @Test
    public void stateAccumulation() {
        State<Integer, Integer> counter = State.<Integer>get().flatMap(i -> State.put(i + 1).discardL(State.state(i)));
        assertEquals(tuple(0, 1), counter.run(0));
    }

    @Test
    public void zipOrdering() {
        Tuple2<Integer, String> result = State.<String, Integer>state(s -> tuple(0, s + "1"))
                .zip(State.<String, Fn1<? super Integer, ? extends Integer>>state(s -> tuple(x -> x + 1, s + "2")))
                .run("_");
        assertEquals(tuple(1, "_12"), result);
    }

    @Test
    public void withState() {
        State<Integer, Integer> modified = State.<Integer>get().withState(x -> x + 1);
        assertEquals(tuple(1, 1), modified.run(0));
    }

    @Test
    public void mapState() {
        State<Integer, Integer> modified = State.<Integer>get().mapState(into((a, s) -> product(a + 1, s + 2)));
        assertEquals(tuple(1, 2), modified.run(0));
    }

    @Test
    public void staticPure() {
        State<String, Integer> state = State.<String>pureState().apply(1);
        assertEquals(tuple(1, "foo"), state.run("foo"));
    }
}