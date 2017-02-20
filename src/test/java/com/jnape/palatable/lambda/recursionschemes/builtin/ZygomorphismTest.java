package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.recursionschemes.Algebra;
import org.junit.Test;
import testsupport.recursion.ListF;
import testsupport.recursion.NatF;

import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Zygomorphism.zygo;
import static org.junit.Assert.assertEquals;
import static testsupport.recursion.List.cons;
import static testsupport.recursion.List.nil;
import static testsupport.recursion.Nat.s;
import static testsupport.recursion.Nat.z;

public class ZygomorphismTest {

    @Test
    public void foldsThroughLeastFixedPointWithParallelAlgebra() {
        Algebra<NatF<Integer>, Integer> sumNat = natF -> natF.match(z -> 0, s -> s.carrier() + 1);
        Fn1<NatF<Tuple2<String, Integer>>, String> natFString =
                natF -> natF.match(z -> "_", s -> s.carrier().into((x, y) -> x + " : {" + y + "}"));

        assertEquals("_", zygo(natFString, sumNat, fix(NatF.z())));
        assertEquals("_", zygo(natFString, sumNat, z()));

        assertEquals("_ : {1} : {2} : {3}", zygo(natFString, sumNat, fix(NatF.s(fix(NatF.s(fix(NatF.s(fix(NatF.z())))))))));
        assertEquals("_ : {1} : {2} : {3}", zygo(natFString, sumNat, s(s(s(z())))));

        Algebra<ListF<Integer, Integer>, Integer> sumList = listF -> listF.match(nil -> 0, cons -> cons.head() + cons.tail());
        Fn1<ListF<Integer, Tuple2<String, Integer>>, String> listFString =
                listF -> listF.match(nil -> "_",
                                     cons -> "{" + cons.head() + " : " + cons.tail().into((x, y) -> "(" + x + ", " + y + ")") + "}");

        assertEquals("_", zygo(listFString, sumList, fix(ListF.nil())));
        assertEquals("_", zygo(listFString, sumList, nil()));

        assertEquals("{3 : ({2 : ({1 : (_, 1)}, 3)}, 6)}", zygo(listFString, sumList, fix(ListF.cons(3, fix(ListF.cons(2, fix(ListF.cons(1, fix(ListF.nil())))))))));
        assertEquals("{3 : ({2 : ({1 : (_, 1)}, 3)}, 6)}", zygo(listFString, sumList, cons(3, cons(2, cons(1, nil())))));
    }
}