package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.recursionschemes.Fix;
import org.junit.Test;
import testsupport.recursion.ListF;
import testsupport.recursion.NatF;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.choice.Choice2.a;
import static com.jnape.palatable.lambda.adt.choice.Choice2.b;
import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Apomorphism.apo;
import static com.jnape.palatable.lambda.recursionschemes.builtin.GApomorphism.gApo;
import static org.junit.Assert.assertEquals;
import static testsupport.recursion.ListF.cons;
import static testsupport.recursion.ListF.nil;

public class GApomorphismTest {

    @Test
    public void unfoldingToGreatestFixedPointWithShortCircuitAndCoalgebra() {
        Function<Integer, NatF<CoProduct2<Integer, Fix<NatF, NatF<Fix<NatF, ?>>>>>> nats =
                x -> NatF.s(x < 3 ? a(x + 1) : b(fix(NatF.z())));
        assertEquals(apo(nats, 3), gApo(nats, __ -> NatF.z(), 3));
        assertEquals(apo(nats, 1), gApo(nats, __ -> NatF.z(), 1));

        Function<Integer, ListF<String, CoProduct2<Integer, Fix<ListF<String, ?>, ListF<String, Fix<ListF<String, ?>, ?>>>>>> unfold =
                x -> cons("<" + x + ">", x < 3 ? a(x + 1) : b(fix(nil())));
        assertEquals(apo(unfold, 1), gApo(unfold, __ -> ListF.nil(), 1));
        assertEquals(apo(unfold, 3), gApo(unfold, __ -> ListF.nil(), 3));
    }
}