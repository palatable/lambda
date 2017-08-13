package testsupport.recursion;

import com.jnape.palatable.lambda.recursionschemes.Fix;

public final class Nat implements Fix<NatF, NatF<Nat>> {

    private final NatF<Nat> unfixed;

    private Nat(NatF<Nat> unfixed) {
        this.unfixed = unfixed;
    }

    @Override
    public NatF<Nat> unfix() {
        return unfixed;
    }

    public static Nat z() {
        return new Nat(NatF.z());
    }

    public static Nat s(Nat n) {
        return new Nat(NatF.s(n));
    }
}
