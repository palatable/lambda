package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.recursionschemes.Algebra;
import com.jnape.palatable.lambda.recursionschemes.Fix;
import fix.Coalgebra;
import org.junit.Test;
import testsupport.recursion.ListF;
import testsupport.recursion.NatF;

import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Anamorphism.ana;
import static org.junit.Assert.assertEquals;

public class AnamorphismTest {

    @Test
    public void unfoldingToGreatestFixedPoint() {
        Coalgebra<Integer, NatF<Integer>> naturals = x -> x >= 3 ? NatF.z() : NatF.s(x + 1);
        assertEquals(Fix.fix(NatF.z()), ana(naturals, 3));
        assertEquals(Fix.<NatF, NatF<Fix<NatF, ?>>>fix(NatF.s(fix(NatF.s(fix(NatF.s(fix(NatF.z()))))))), ana(naturals, 0));

        Coalgebra<Integer, ListF<Integer, Integer>> elements = x -> x >= 3 ? ListF.nil() : ListF.cons(x, x + 1);
        assertEquals(Fix.fix(ListF.nil()), ana(elements, 4));
        assertEquals(Fix.<ListF<Integer, ?>, ListF<Integer, Fix<ListF<Integer, ?>, ?>>>fix(ListF.cons(0, fix(ListF.cons(1, fix(ListF.cons(2, fix(ListF.nil()))))))),
                     ana(elements, 0));
    }

    //    @Test
    //todo: wip
    public void unfoldingInfiniteCodata() {
        Coalgebra<Integer, NatF<Integer>> naturals = i -> NatF.s(i + 1);
        Algebra<NatF<String>, String> sum = nat -> nat.match(z -> "0", s -> s.carrier() + 1);
        String x = Hylomorphism.<Integer, String, NatF, NatF<Integer>, NatF<String>>hylo().apply(sum, naturals, 0);
        System.out.println(x);

    }
}