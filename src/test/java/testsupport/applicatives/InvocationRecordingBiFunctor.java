package testsupport.applicatives;

import com.jnape.palatable.lambda.applicative.BiFunctor;
import com.jnape.palatable.lambda.functions.MonadicFunction;

import java.util.concurrent.atomic.AtomicReference;

public final class InvocationRecordingBiFunctor<A, B> implements BiFunctor<A, B> {
    private final AtomicReference<MonadicFunction> leftFn;
    private final AtomicReference<MonadicFunction> rightFn;

    public InvocationRecordingBiFunctor(AtomicReference<MonadicFunction> leftFn,
                                        AtomicReference<MonadicFunction> rightFn) {
        this.leftFn = leftFn;
        this.rightFn = rightFn;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C, D> BiFunctor<C, D> biMap(MonadicFunction<? super A, ? extends C> f1,
                                        MonadicFunction<? super B, ? extends D> f2) {
        leftFn.set(f1);
        rightFn.set(f2);
        return (BiFunctor<C, D>) this;
    }
}
