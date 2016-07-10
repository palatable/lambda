package testsupport.applicatives;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Bifunctor;

import java.util.concurrent.atomic.AtomicReference;

public final class InvocationRecordingBifunctor<A, B> implements Bifunctor<A, B> {
    private final AtomicReference<Fn1> leftFn;
    private final AtomicReference<Fn1> rightFn;

    public InvocationRecordingBifunctor(AtomicReference<Fn1> leftFn,
                                        AtomicReference<Fn1> rightFn) {
        this.leftFn = leftFn;
        this.rightFn = rightFn;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C, D> Bifunctor<C, D> biMap(Fn1<? super A, ? extends C> lFn,
                                        Fn1<? super B, ? extends D> rFn) {
        leftFn.set(lFn);
        rightFn.set(rFn);
        return (Bifunctor<C, D>) this;
    }
}
