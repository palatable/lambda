package com.jnape.palatable.lambda.recursionschemes;

import org.junit.Test;
import testsupport.recursion.ListF;
import testsupport.recursion.NatF;

import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;
import static org.junit.Assert.assertEquals;
import static testsupport.recursion.ListF.cons;
import static testsupport.recursion.ListF.nil;
import static testsupport.recursion.NatF.s;
import static testsupport.recursion.NatF.z;

public class FixTest {

    @Test
    @SuppressWarnings("unused")
    public void validTypeSafeSignatures() {
        Fix<NatF, NatF<Fix<NatF, NatF<Fix<NatF, NatF<Fix<NatF, ?>>>>>>> losslessNatF = fix(s(fix(s(fix(z())))));
        Fix<NatF, NatF<Fix<NatF, ?>>> compactNatF = fix(s(fix(s(fix(z())))));
        Fix<NatF, ?> minimalNatF = fix(s(fix(s(fix(z())))));

        Fix<ListF<Integer, ?>, ListF<Integer, Fix<ListF<Integer, ?>, ListF<Integer, Fix<ListF<Integer, ?>, ListF<Integer, Fix<ListF<Integer, ?>, ListF<Integer, Fix<ListF<Integer, ?>, ?>>>>>>>>> losslessListF = fix(cons(3, fix(cons(2, fix(cons(1, fix(nil())))))));
        Fix<ListF<Integer, ?>, ListF<Integer, Fix<ListF<Integer, ?>, ?>>> compactListF = fix(cons(3, fix(cons(2, fix(cons(1, fix(nil())))))));
        Fix<ListF<Integer, ?>, ?> minimalListF = fix(cons(3, fix(cons(2, fix(cons(1, fix(nil())))))));
    }

    @Test
    public void fixAndUnfix() {
        NatF<Fix<NatF, ?>> z = z();
        assertEquals(z, fix(z).unfix());
    }
}