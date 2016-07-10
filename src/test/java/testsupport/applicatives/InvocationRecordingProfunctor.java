package testsupport.applicatives;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Profunctor;

import java.util.concurrent.atomic.AtomicReference;

public final class InvocationRecordingProfunctor<A, B> implements Profunctor<A, B> {
    private final AtomicReference<Fn1> leftFn;
    private final AtomicReference<Fn1> rightFn;

    public InvocationRecordingProfunctor(AtomicReference<Fn1> leftFn,
                                         AtomicReference<Fn1> rightFn) {
        this.leftFn = leftFn;
        this.rightFn = rightFn;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C, D> Profunctor<C, D> diMap(Fn1<C, A> lFn, Fn1<B, D> rFn) {
        leftFn.set(lFn);
        rightFn.set(rFn);
        return (Profunctor<C, D>) this;
    }
}
