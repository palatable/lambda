package testsupport.recursion;

import com.jnape.palatable.lambda.recursionschemes.Fix;

public final class Nat implements Fix<NatF, NatF<Nat>> {

    private final NatF<Nat> carrier;

    private Nat(NatF<Nat> carrier) {
        this.carrier = carrier;
    }

    @Override
    public NatF<Nat> unfix() {
        return carrier;
    }

    public static Nat z() {
        return new Nat(NatF.z());
    }

    public static Nat s(Nat n) {
        return new Nat(NatF.s(n));
    }
}
