package testsupport.applicatives;

import com.jnape.palatable.lambda.functor.Profunctor;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public final class InvocationRecordingProfunctor<A, B> implements Profunctor<A, B> {
    private final AtomicReference<Function> leftFn;
    private final AtomicReference<Function> rightFn;

    public InvocationRecordingProfunctor(AtomicReference<Function> leftFn,
                                         AtomicReference<Function> rightFn) {
        this.leftFn = leftFn;
        this.rightFn = rightFn;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C, D> Profunctor<C, D> diMap(Function<C, A> lFn, Function<B, D> rFn) {
        leftFn.set(lFn);
        rightFn.set(rFn);
        return (Profunctor<C, D>) this;
    }
}
