package testsupport;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.Objects;

public final class EquatableM<M extends Monad<?, M>, A> implements Monad<A, EquatableM<M, ?>> {

    private final Monad<A, M>       ma;
    private final Fn1<? super M, ?> equatable;

    public EquatableM(Monad<A, M> ma, Fn1<? super M, ?> equatable) {
        this.ma = ma;
        this.equatable = equatable;
    }

    public <B> EquatableM<M, B> with(Fn1<? super Monad<A, M>, ? extends Monad<B, M>> fn) {
        return new EquatableM<>(fn.apply(ma), equatable);
    }

    public <B> EquatableM<M, B> swap(Monad<B, M> mb) {
        return new EquatableM<>(mb, equatable);
    }

    @Override
    public <B> EquatableM<M, B> flatMap(Fn1<? super A, ? extends Monad<B, EquatableM<M, ?>>> f) {
        return new EquatableM<>(ma.flatMap(f.fmap(x -> x.<EquatableM<M, B>>coerce().ma)), equatable);
    }

    @Override
    public <B> EquatableM<M, B> pure(B b) {
        return new EquatableM<>(ma.pure(b), equatable);
    }

    @Override
    public <B> EquatableM<M, B> fmap(Fn1<? super A, ? extends B> fn) {
        return new EquatableM<>(ma.fmap(fn), equatable);
    }

    @Override
    public <B> EquatableM<M, B> zip(Applicative<Fn1<? super A, ? extends B>, EquatableM<M, ?>> appFn) {
        return new EquatableM<>(ma.zip(appFn.<EquatableM<M, Fn1<? super A, ? extends B>>>coerce().ma), equatable);
    }

    @Override
    public <B> EquatableM<M, B> discardL(Applicative<B, EquatableM<M, ?>> appB) {
        return new EquatableM<>(ma.discardL(appB.<EquatableM<M, B>>coerce().ma), equatable);
    }

    @Override
    public <B> EquatableM<M, A> discardR(Applicative<B, EquatableM<M, ?>> appB) {
        return new EquatableM<>(ma.discardR(appB.<EquatableM<M, B>>coerce().ma), equatable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (other instanceof EquatableM<?, ?>) {
            EquatableM<M, ?> that = (EquatableM<M, ?>) other;
            return Objects.equals(equatable.apply((M) ma), that.equatable.apply((M) that.ma));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
