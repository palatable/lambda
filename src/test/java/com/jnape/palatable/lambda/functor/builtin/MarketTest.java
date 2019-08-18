package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.Equivalence;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.trying;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Both.both;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.lang.Integer.parseInt;
import static org.junit.Assert.assertEquals;
import static testsupport.traits.Equivalence.equivalence;

@RunWith(Traits.class)
public class MarketTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
    public Subjects<Equivalence<Market<Integer, String, String, String>>> testSubject() {
        Market<Integer, String, String, String> market = new Market<>(id(), str -> trying(() -> parseInt(str),
                                                                                          constantly(str)));
        return subjects(equivalence(market, m -> both(m.bt(), m.sta(), "123")),
                        equivalence(market, m -> both(m.bt(), m.sta(), "foo")));
    }

    @Test
    public void staticPure() {
        Market<Boolean, Character, String, Integer> market = Market.<Boolean, Character, String>pureMarket().apply(1);
        assertEquals((Integer) 1, market.bt().apply('a'));
        assertEquals(left(1), market.sta().apply("foo"));
    }
}