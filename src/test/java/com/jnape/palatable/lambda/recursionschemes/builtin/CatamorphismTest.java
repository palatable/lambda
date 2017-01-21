package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.recursionschemes.Algebra;
import org.junit.Test;
import testsupport.recursion.ListF;
import testsupport.recursion.NatF;

import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Catamorphism.cata;
import static org.junit.Assert.assertEquals;
import static testsupport.recursion.List.cons;
import static testsupport.recursion.List.nil;
import static testsupport.recursion.Nat.s;
import static testsupport.recursion.Nat.z;

public class CatamorphismTest {

    @Test
    public void foldingThroughLeastFixedPoint() {
        Algebra<NatF<Integer>, Integer> sum = nat -> nat.match(z -> 0, s -> s.carrier() + 1);
        assertEquals((Integer) 0, cata(sum).apply(fix(NatF.z())));
        assertEquals((Integer) 0, cata(sum).apply(z()));

        assertEquals((Integer) 3, cata(sum).apply(fix(NatF.s(fix(NatF.s(fix(NatF.s(fix(NatF.z())))))))));
        assertEquals((Integer) 3, cata(sum).apply(s(s(s(z())))));

        Algebra<ListF<String, Integer>, Integer> length = list -> list.match(nil -> 0, cons -> 1 + cons.tail());
        assertEquals((Integer) 0, cata(length).apply(fix(ListF.nil())));
        assertEquals((Integer) 0, cata(length).apply(nil()));

        assertEquals((Integer) 3, cata(length).apply(fix(ListF.cons("3", fix(ListF.cons("2", fix(ListF.cons("1", fix(ListF.nil())))))))));
        assertEquals((Integer) 3, cata(length).apply(cons("3", cons("2", cons("1", nil())))));
    }
}