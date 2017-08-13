package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.functor.NaturalTransformation;
import com.jnape.palatable.lambda.recursionschemes.Algebra;
import org.junit.Test;
import testsupport.recursion.Nat;
import testsupport.recursion.NatF;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Iterate.iterate;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Take.take;
import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Anamorphism.ana;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Catamorphism.cata;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Prepromorphism.prepro;
import static org.junit.Assert.assertEquals;
import static testsupport.recursion.NatF.s;
import static testsupport.recursion.NatF.z;

public class PrepromorphismTest {

    @Test
    public void foldingThroughFixedPointWithNaturalTransformation() {
        Algebra<NatF<Integer>, Integer> sum = natF -> natF.match(z -> 0, s -> 1 + s.carrier());
        NaturalTransformation<Integer, NatF<Integer>, NatF<Integer>> times2 = natF -> natF.match(constantly(z()), s -> s(s.carrier() * 2));

        assertEquals((Integer) 0, prepro(sum, times2, fix(z())));
        assertEquals((Integer) 0, prepro(sum, times2).apply(Nat.z()));
        assertEquals((Integer) 1, prepro(sum, times2).apply(fix(NatF.s(fix(z())))));
        assertEquals((Integer) 1, prepro(sum, times2).apply(Nat.s(Nat.z())));
        assertEquals((Integer) 3, prepro(sum, times2).apply(fix(NatF.s(fix(NatF.s(fix(z())))))));
        assertEquals((Integer) 3, prepro(sum, times2).apply(Nat.s(Nat.s(Nat.z()))));
    }

    @Test
    public void prepromorphismWithIdentityNaturalTransformationIsIsomorphicToCatamorphism() {
        Algebra<NatF<Integer>, Integer> sum = natF -> natF.match(z -> 0, s -> 1 + s.carrier());
        take(100, map(ana(x -> x > 0 ? s(x - 1) : z()), iterate(x -> x + 1, 0)))
                .forEach(fix -> assertEquals(cata(sum, fix), prepro(sum, NaturalTransformation.identity(), fix)));
    }
}