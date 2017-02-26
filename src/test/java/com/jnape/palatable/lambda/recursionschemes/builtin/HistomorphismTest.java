package com.jnape.palatable.lambda.recursionschemes.builtin;

import com.jnape.palatable.lambda.recursionschemes.Cofree;
import com.jnape.palatable.lambda.recursionschemes.Fix;
import org.junit.Test;
import testsupport.recursion.NatF;

import java.util.function.Function;

import static com.jnape.palatable.lambda.recursionschemes.builtin.Histomorphism.histo;

public class HistomorphismTest {

    @Test
    public void foldingThroughLeastFixedPointRetainingFullTree() {
        Integer foo = histo((Function<NatF<Cofree<NatF, Integer, ?>>, Integer>) natF -> natF.match(z -> 0, s -> 1 + s.carrier().attr()),
                            Fix.fix(NatF.s(Fix.fix(NatF.z()))));
        System.out.println(foo);
    }
}