package com.jnape.palatable.lambda.recursionschemes;

import org.junit.Test;
import testsupport.recursion.ListF;
import testsupport.recursion.NatF;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;
import static com.jnape.palatable.lambda.recursionschemes.Free.free;
import static com.jnape.palatable.lambda.recursionschemes.Free.pure;
import static org.junit.Assert.assertEquals;
import static testsupport.recursion.ListF.cons;
import static testsupport.recursion.ListF.nil;
import static testsupport.recursion.NatF.s;
import static testsupport.recursion.NatF.z;

public class FreeTest {

    @Test
    @SuppressWarnings("unused")
    public void validTypeSafeSignatures() {
        Free<Integer, NatF, NatF<Free<Integer, NatF, NatF<Free<Integer, NatF, NatF<Free<Integer, NatF, ?>>>>>>> losslessNatFWithPure = free(s(free(s(pure(0)))));
        Free<Integer, NatF, NatF<Free<Integer, NatF, NatF<Free<Integer, NatF, NatF<Free<Integer, NatF, ?>>>>>>> losslessNatFWithoutPure = free(s(free(s(free(z())))));
        Free<Integer, NatF, NatF<Free<Integer, NatF, ?>>> compactNatFWithPure = free(s(free(s(pure(0)))));
        Free<Integer, NatF, NatF<Free<Integer, NatF, ?>>> compactNatFWithoutPure = free(s(free(s(free(z())))));
        Free<Integer, NatF, ?> minimalNatFWithPure = free(s(free(s(pure(0)))));
        Free<Integer, NatF, ?> minimalNatFWithoutPure = free(s(free(s(free(z())))));

        Free<Integer, ListF<Integer, ?>, ListF<Integer, Free<Integer, ListF<Integer, ?>, ListF<Integer, Free<Integer, ListF<Integer, ?>, ListF<Integer, Free<Integer, ListF<Integer, ?>, ListF<Integer, Free<Integer, ListF<Integer, ?>, ?>>>>>>>>> losslessListFWithPure = free(cons(3, free(cons(2, free(cons(1, pure(0)))))));
        Free<Integer, ListF<Integer, ?>, ListF<Integer, Free<Integer, ListF<Integer, ?>, ListF<Integer, Free<Integer, ListF<Integer, ?>, ListF<Integer, Free<Integer, ListF<Integer, ?>, ListF<Integer, Free<Integer, ListF<Integer, ?>, ?>>>>>>>>> losslessListFWithoutPure = free(cons(3, free(cons(2, free(cons(1, free(nil())))))));
        Free<Integer, ListF<Integer, ?>, ListF<Integer, Free<Integer, ListF<Integer, ?>, ?>>> compactListFWithPure = free(cons(3, free(cons(2, free(cons(1, pure(0)))))));
        Free<Integer, ListF<Integer, ?>, ListF<Integer, Free<Integer, ListF<Integer, ?>, ?>>> compactListFWithoutPure = free(cons(3, free(cons(2, free(cons(1, free(nil())))))));
        Free<Integer, ListF<Integer, ?>, ?> minimalListFWithPure = free(cons(3, free(cons(2, free(cons(1, pure(0)))))));
        Free<Integer, ListF<Integer, ?>, ?> minimalListFWithoutPure = free(cons(3, free(cons(2, free(cons(1, free(nil())))))));
    }

    @Test
    public void coproduct() {
        assertEquals(z(), free(z()).match(id(), id()));
        assertEquals(0, pure(0).match(id(), id()));
    }

    @Test
    public void foldsIntoFix() {
        Coalgebra<Integer, NatF<Integer>> nats = x -> x == 0 ? z() : s(x - 1);
        assertEquals(free(s(free(s(free(s(pure(0))))))).fix(nats), fix(s(fix(s(fix(s(fix(z()))))))));
        assertEquals(free(s(free(s(free(s(pure(1))))))).fix(nats), fix(s(fix(s(fix(s(fix(s(fix(z()))))))))));
    }
}