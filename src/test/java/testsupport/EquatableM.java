package testsupport;

import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.Objects;
import java.util.function.Function;

public final class EquatableM<M extends Monad<?, ?>, A> implements Monad<A, EquatableM<M, ?>> {

    private final Monad<A, M>            ma;
    private final Function<? super M, ?> equatable;

    public EquatableM(Monad<A, M> ma, Function<? super M, ?> equatable) {
        this.ma = ma;
        this.equatable = equatable;
    }

    @Override
    public <B> EquatableM<M, B> flatMap(Function<? super A, ? extends Monad<B, EquatableM<M, ?>>> f) {
        return new EquatableM<>(ma.flatMap(f.andThen(x -> x.<EquatableM<M, B>>coerce().ma)), equatable);
    }

    @Override
    public <B> EquatableM<M, B> pure(B b) {
        return new EquatableM<>(ma.pure(b), equatable);
    }

    @Override
    public <B> EquatableM<M, B> fmap(Function<? super A, ? extends B> fn) {
        return new EquatableM<>(ma.fmap(fn), equatable);
    }

    @Override
    public <B> EquatableM<M, B> zip(Applicative<Function<? super A, ? extends B>, EquatableM<M, ?>> appFn) {
        return new EquatableM<>(ma.zip(appFn.<EquatableM<M, Function<? super A, ? extends B>>>coerce().ma), equatable);
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
}
