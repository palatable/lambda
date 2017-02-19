package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.recursionschemes.Fix;
import org.junit.Test;
import testsupport.recursion.List;
import testsupport.recursion.ListF;
import testsupport.recursion.NatF;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Paramorphism.para;
import static org.junit.Assert.assertEquals;
import static testsupport.recursion.List.cons;
import static testsupport.recursion.List.nil;
import static testsupport.recursion.Nat.s;
import static testsupport.recursion.Nat.z;

public class ParamorphismTest {

    @Test
    public void foldingThroughLeastFixedPointRetainingSubtree() {
        Fn1<NatF<Tuple2<Fix<NatF, NatF<Fix<NatF, ?>>>, Integer>>, Integer> sum =
                nat -> nat.match(constantly(0), s -> s.carrier()._2() + 1);

        assertEquals((Integer) 0, para(sum, fix(NatF.z())));
        assertEquals((Integer) 0, para(sum, z()));

        assertEquals((Integer) 3, para(sum, fix(NatF.s(fix(NatF.s(fix(NatF.s(fix(NatF.z())))))))));
        assertEquals((Integer) 3, para(sum, s(s(s(z())))));

        Fn1<ListF<Integer, Tuple2<Fix<ListF<Integer, ?>, ListF<Integer, Fix<ListF<Integer, ?>, ?>>>, List<String>>>, List<String>> stringify =
                listF -> listF.match(nil -> nil(),
                                     cons -> cons(cons.head().toString(), cons.tail()._2()));

        assertEquals(nil(), para(stringify, fix(ListF.nil())));
        assertEquals(nil(), para(stringify, nil()));

        assertEquals(cons("3", cons("2", cons("1", nil()))), para(stringify, fix(ListF.cons(3, fix(ListF.cons(2, fix(ListF.cons(1, fix(ListF.nil())))))))));
        assertEquals(cons("3", cons("2", cons("1", nil()))), para(stringify, cons(3, cons(2, cons(1, nil())))));
    }
}