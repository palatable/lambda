package testsupport.applicatives;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Profunctor;

import java.util.concurrent.atomic.AtomicReference;

public final class InvocationRecordingProfunctor<A, B> implements Profunctor<A, B, InvocationRecordingProfunctor<?, ?>> {
    private final AtomicReference<Fn1<?, ?>> leftFn;
    private final AtomicReference<Fn1<?, ?>> rightFn;

    public InvocationRecordingProfunctor(AtomicReference<Fn1<?, ?>> leftFn,
                                         AtomicReference<Fn1<?, ?>> rightFn) {
        this.leftFn = leftFn;
        this.rightFn = rightFn;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C, D> InvocationRecordingProfunctor<C, D> diMap(Fn1<? super C, ? extends A> lFn,
                                                            Fn1<? super B, ? extends D> rFn) {
        leftFn.set(lFn);
        rightFn.set(rFn);
        return (InvocationRecordingProfunctor<C, D>) this;
    }
}
