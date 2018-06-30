package testsupport;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Apply;
import com.jnape.palatable.lambda.functor.Bind;

import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static java.util.Objects.hash;

public final class EqualityAwareFn0<A> implements Fn0<A> {
    private final Fn0<A> fn;

    public EqualityAwareFn0(Fn0<A> fn) {
        this.fn = fn;
    }

    @Override
    public A apply(Unit unit) {
        return fn.apply(unit);
    }

    @Override
    public <B> EqualityAwareFn0<B> flatMap(Function<? super A, ? extends Bind<B, Fn1<Unit, ?>>> f) {
        return new EqualityAwareFn0<>(fn.flatMap(f));
    }

    @Override
    public <B> EqualityAwareFn0<B> fmap(Function<? super A, ? extends B> f) {
        return new EqualityAwareFn0<>(fn.fmap(f));
    }

    @Override
    public <B> EqualityAwareFn0<B> zip(Apply<Function<? super A, ? extends B>, Fn1<Unit, ?>> appFn) {
        return new EqualityAwareFn0<>(fn.zip(appFn));
    }

    @Override
    public <B> EqualityAwareFn0<B> pure(B b) {
        return new EqualityAwareFn0<>(fn.pure(b));
    }


    @Override
    public <B> EqualityAwareFn0<B> discardL(Applicative<B, Fn1<Unit, ?>> appB) {
        return new EqualityAwareFn0<>(fn.discardL(appB));
    }

    @Override
    public <B> EqualityAwareFn0<A> discardR(Applicative<B, Fn1<Unit, ?>> appB) {
        return new EqualityAwareFn0<>(fn.discardR(appB));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        return other instanceof Fn0 && ((Fn0<A>) other).apply(UNIT).equals(apply(UNIT));
    }

    @Override
    public int hashCode() {
        return hash(fn);
    }
}
