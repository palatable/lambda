package com.jnape.palatable.lambda.recursionschemes;

import org.junit.Test;
import testsupport.recursion.ListF;
import testsupport.recursion.NatF;

import static com.jnape.palatable.lambda.recursionschemes.Cofree.cofree;
import static testsupport.recursion.ListF.cons;
import static testsupport.recursion.ListF.nil;
import static testsupport.recursion.NatF.s;
import static testsupport.recursion.NatF.z;

public class CofreeTest {

    @Test
    @SuppressWarnings("unused")
    public void validTypeSafeSignatures() {
        Cofree<NatF, Integer, NatF<Cofree<NatF, Integer, NatF<Cofree<NatF, Integer, NatF<Cofree<NatF, Integer, ?>>>>>>> losslessNatF = cofree(s(cofree(s(cofree(z(), 1)), 2)), 3);
        Cofree<NatF, Integer, NatF<Cofree<NatF, Integer, ?>>> compactNatF = cofree(s(cofree(s(cofree(z(), 1)), 2)), 3);
        Cofree<NatF, ?, ?> minimalNatF = cofree(s(cofree(s(cofree(z(), 1)), 2)), 3);

        Cofree<ListF<Integer, ?>, Integer, ListF<Integer, Cofree<ListF<Integer, ?>, Integer, ListF<Integer, Cofree<ListF<Integer, ?>, Integer, ListF<Integer, Cofree<ListF<Integer, ?>, Integer, ?>>>>>>> losslessListF = cofree(cons(2, cofree(cons(1, cofree(nil(), 0)), -1)), -2);
        Cofree<ListF<Integer, ?>, Integer, ListF<Integer, Cofree<ListF<Integer, ?>, Integer, ?>>> compactListF = cofree(cons(2, cofree(cons(1, cofree(nil(), 0)), -1)), -2);
        Cofree<ListF<Integer, ?>, ?, ?> minimalListF = cofree(cons(2, cofree(cons(1, cofree(nil(), 0)), -1)), -2);
    }
}