package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.recursionschemes.Fix;
import org.junit.Test;
import testsupport.recursion.NatF;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.choice.Choice2.a;
import static com.jnape.palatable.lambda.adt.choice.Choice2.b;
import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Apomorphism.apo;
import static org.junit.Assert.assertEquals;

public class ApomorphismTest {

    @Test
    public void unfoldingToGreatestFixedPointWithShortCircuit() {
        Function<Integer, NatF<CoProduct2<Integer, Fix<NatF, NatF<Fix<NatF, ?>>>>>> unfold =
                x -> NatF.s(x < 3 ? a(x + 1) : b(fix(NatF.z())));

        assertEquals(Fix.<NatF, NatF<Fix<NatF, ?>>>fix(NatF.s(fix(NatF.s(fix(NatF.s(fix(NatF.z()))))))), apo(unfold, 1));
    }
}