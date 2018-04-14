package testsupport;

import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.lens.Iso;
import com.jnape.palatable.lambda.lens.LensLike;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.Objects;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Both.both;
import static com.jnape.palatable.lambda.lens.functions.View.view;

public final class EqualityAwareIso<S, T, A, B> implements Iso<S, T, A, B> {
    private final S               s;
    private final B               b;
    private final Iso<S, T, A, B> iso;

    public EqualityAwareIso(S s, B b, Iso<S, T, A, B> iso) {
        this.s = s;
        this.b = b;
        this.iso = iso;
    }

    @Override
    public <P extends Profunctor, F extends Functor, FB extends Functor<B, F>, FT extends Functor<T, F>, PAFB extends Profunctor<A, FB, P>, PSFT extends Profunctor<S, FT, P>> PSFT apply(
            PAFB pafb) {
        return iso.<P, F, FB, FT, PAFB, PSFT>apply(pafb);
    }

    @Override
    public <F extends Functor, FT extends Functor<T, F>, FB extends Functor<B, F>> FT apply(
            Function<? super A, ? extends FB> fn, S s) {
        return iso.apply(fn, s);
    }

    @Override
    public <U> EqualityAwareIso<S, U, A, B> fmap(Function<? super T, ? extends U> fn) {
        return new EqualityAwareIso<>(s, b, iso.fmap(fn));
    }

    @Override
    public <U> EqualityAwareIso<S, U, A, B> pure(U u) {
        return new EqualityAwareIso<>(s, b, iso.pure(u));
    }

    @Override
    public <U> EqualityAwareIso<S, U, A, B> zip(
            Applicative<Function<? super T, ? extends U>, LensLike<S, ?, A, B, Iso>> appFn) {
        return new EqualityAwareIso<>(s, b, iso.zip(appFn));
    }

    @Override
    public <U> EqualityAwareIso<S, U, A, B> flatMap(
            Function<? super T, ? extends Monad<U, LensLike<S, ?, A, B, Iso>>> fn) {
        return new EqualityAwareIso<>(s, b, iso.flatMap(fn));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (other instanceof EqualityAwareIso) {
            Iso<S, T, A, B> that = (EqualityAwareIso<S, T, A, B>) other;
            Boolean sameForward = both(view(this), view(that)).apply(s).into(Objects::equals);
            Boolean sameReverse = both(view(this.mirror()), view(that.mirror())).apply(b).into(Objects::equals);
            return sameForward && sameReverse;
        }
        return false;
    }
}
