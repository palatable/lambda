package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.recursionschemes.Algebra;
import fix.Coalgebra;
import org.junit.Test;
import testsupport.recursion.ListF;
import testsupport.recursion.NatF;

import static com.jnape.palatable.lambda.recursionschemes.builtin.Hylomorphism.hylo;
import static org.junit.Assert.assertEquals;

public class HylomorphismTest {

    @Test
    public void unfoldsToThenFoldsThroughFixedPoint() {
        Algebra<NatF<Integer>, Integer> sum = nat ->
                nat.match(z -> 0,
                          s -> 1 + s.carrier());
        Coalgebra<Integer, NatF<Integer>> naturals = x -> x >= 10 ? NatF.z() : NatF.s(x + 1);
        assertEquals((Integer) 10, hylo(sum, naturals).apply(0));

        Algebra<ListF<Integer, Integer>, Integer> length = list -> list.match(
                nil -> 0,
                cons -> 1 + cons.tail());
        Coalgebra<Integer, ListF<Integer, Integer>> unroll = x -> x < 10 ? ListF.cons(x, x + 1) : ListF.nil();
        assertEquals((Integer) 10, hylo(length, unroll).apply(0));
    }
}