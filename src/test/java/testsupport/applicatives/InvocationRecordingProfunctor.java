package testsupport.applicatives;

import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functor.Profunctor;

import java.util.concurrent.atomic.AtomicReference;

public final class InvocationRecordingProfunctor<A, B> implements Profunctor<A, B> {
    private final AtomicReference<MonadicFunction> leftFn;
    private final AtomicReference<MonadicFunction> rightFn;

    public InvocationRecordingProfunctor(AtomicReference<MonadicFunction> leftFn,
                                         AtomicReference<MonadicFunction> rightFn) {
        this.leftFn = leftFn;
        this.rightFn = rightFn;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C, D> Profunctor<C, D> diMap(MonadicFunction<C, A> lFn, MonadicFunction<B, D> rFn) {
        leftFn.set(lFn);
        rightFn.set(rFn);
        return (Profunctor<C, D>) this;
    }
}
