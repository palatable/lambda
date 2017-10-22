package testsupport.applicatives;

import com.jnape.palatable.lambda.functor.Profunctor;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public final class InvocationRecordingProfunctor<A, B> implements Profunctor<A, B, InvocationRecordingProfunctor> {
    private final AtomicReference<Function> leftFn;
    private final AtomicReference<Function> rightFn;

    public InvocationRecordingProfunctor(AtomicReference<Function> leftFn,
                                         AtomicReference<Function> rightFn) {
        this.leftFn = leftFn;
        this.rightFn = rightFn;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C, D> InvocationRecordingProfunctor<C, D> diMap(Function<? super C, ? extends A> lFn,
                                                            Function<? super B, ? extends D> rFn) {
        leftFn.set(lFn);
        rightFn.set(rFn);
        return (InvocationRecordingProfunctor<C, D>) this;
    }
}
