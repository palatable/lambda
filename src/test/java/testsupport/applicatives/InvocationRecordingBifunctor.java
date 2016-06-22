package testsupport.applicatives;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functor.Bifunctor;

import java.util.concurrent.atomic.AtomicReference;

public final class InvocationRecordingBifunctor<A, B> implements Bifunctor<A, B> {
    private final AtomicReference<MonadicFunction> leftFn;
    private final AtomicReference<MonadicFunction> rightFn;

    public InvocationRecordingBifunctor(AtomicReference<MonadicFunction> leftFn,
                                        AtomicReference<MonadicFunction> rightFn) {
        this.leftFn = leftFn;
        this.rightFn = rightFn;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C, D> Bifunctor<C, D> biMap(MonadicFunction<? super A, ? extends C> lFn,
                                        MonadicFunction<? super B, ? extends D> rFn) {
        leftFn.set(lFn);
        rightFn.set(rFn);
        return (Bifunctor<C, D>) this;
    }
}
