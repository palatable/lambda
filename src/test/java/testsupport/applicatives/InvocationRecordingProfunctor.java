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
    public <C, D> Profunctor<C, D> diMap(MonadicFunction<? super C, ? extends A> f1,
                                         MonadicFunction<? super B, ? extends D> f2) {
        leftFn.set(f1);
        rightFn.set(f2);
        return (Profunctor<C, D>) this;
    }
}
