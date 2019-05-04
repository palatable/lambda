package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.framework.Subjects;
import com.jnape.palatable.traitor.runners.Traits;
import org.junit.runner.RunWith;
import testsupport.EquatableM;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;
import testsupport.traits.MonadLaws;

import static com.jnape.palatable.lambda.adt.Either.trying;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Both.both;
import static com.jnape.palatable.traitor.framework.Subjects.subjects;
import static java.lang.Integer.parseInt;

@RunWith(Traits.class)
public class MarketTest {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class, MonadLaws.class})
    public Subjects<EquatableM<Market<Integer, String, String, ?>, String>> testSubject() {
        Market<Integer, String, String, String> market = new Market<>(id(), str -> trying(() -> parseInt(str),
                                                                                          constantly(str)));
        return subjects(new EquatableM<>(market, m -> both(m.bt(), m.sta(), "123")),
                        new EquatableM<>(market, m -> both(m.bt(), m.sta(), "foo")));
    }
}