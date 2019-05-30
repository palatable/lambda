package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.traversable.LambdaIterable;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.EquatableW;
import testsupport.traits.ComonadLaws;
import testsupport.traits.FunctorLaws;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functor.builtin.Store.store;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static testsupport.matchers.IterableMatcher.iterates;

@RunWith(Traits.class)
public class StoreTest {

    @TestTraits({FunctorLaws.class, ComonadLaws.class})
    public EquatableW<Store<Unit, ?>, ?> testSubject() {
        Store<Unit, Unit> baseStore = store(id(), UNIT);
        return new EquatableW<>(baseStore, store -> tuple(store.extract(), store.peeks(constantly(UNIT))));
    }

    @Test
    public void pos() {
        Store<Integer, Integer> store = store(x -> x + 2, 1);
        Integer i = 3;
        assertEquals(i, store.pos());
    }

    @Test
    public void peek() {
        Store<Integer, Integer> store = store(x -> x + 2, 1);
        Integer i = 5;
        assertEquals(i, store.peek(3));
    }

    @Test
    public void peeks() {
        Fn1<Integer, Integer> integerIntegerFn1 = x -> x + 2;
        Store<Integer, Integer> store = store(integerIntegerFn1, 1);
        Integer i = -7;
        assertEquals(i, store.peeks(x -> x - 10));
    }

    @Test
    public void experimentIterable() {
        Fn1<Integer, Integer> integerIntegerFn1 = x -> x + 2;
        Store<Integer, Integer> store = store(integerIntegerFn1, 1);
        Fn1<Integer, LambdaIterable<Integer>> f = x -> LambdaIterable.wrap(asList(x + 1, x - 1));
        LambdaIterable<Integer> exp = store.experiment(f).coerce();
        assertThat(exp.unwrap(), iterates(4,2));
    }
}
