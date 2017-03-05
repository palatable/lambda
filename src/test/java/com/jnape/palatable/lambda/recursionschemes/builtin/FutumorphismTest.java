package com.jnape.palatable.lambda.recursionschemes.builtin;

import org.junit.Test;

import static com.jnape.palatable.lambda.recursionschemes.Fix.fix;
import static com.jnape.palatable.lambda.recursionschemes.Free.pure;
import static com.jnape.palatable.lambda.recursionschemes.builtin.Futumorphism.futu;
import static org.junit.Assert.assertEquals;
import static testsupport.recursion.NatF.s;
import static testsupport.recursion.NatF.z;

public class FutumorphismTest {

    @Test
    public void unfoldingToGreatestFixedPointWithOptimizations() {
        assertEquals(fix(s(fix(s(fix(s(fix(s(fix(s(fix(z()))))))))))),
                     futu(x -> x == 1 ? z() : x % 2 == 0
                             ? s(pure(x / 2))
                             : s(pure(x * 3 + 1)), 5));
    }
}