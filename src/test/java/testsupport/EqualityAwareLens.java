package testsupport;

import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.builtin.Const;
import com.jnape.palatable.lambda.lens.Lens;

import java.util.Objects;
import java.util.function.Function;

public final class EqualityAwareLens<S, T, A, B> implements Lens<S, T, A, B> {
    private final S                s;
    private final Lens<S, T, A, B> lens;

    public EqualityAwareLens(S s, Lens<S, T, A, B> lens) {
        this.s = s;
        this.lens = lens;
    }

    @Override
    public <F extends Functor, FT extends Functor<T, F>, FB extends Functor<B, F>> FT apply(
            Function<? super A, ? extends FB> fn, S s) {
        return lens.apply(fn, s);
    }

    @Override
    public <U> EqualityAwareLens<S, U, A, B> fmap(Function<? super T, ? extends U> fn) {
        return new EqualityAwareLens<>(s, lens.fmap(fn));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        return other instanceof Lens
                && Objects.equals(((Lens<S, T, A, B>) other).<Const<A, ?>, Const<A, T>, Const<A, B>>apply(Const::new, s).runConst(),
                                  this.<Const<A, ?>, Const<A, T>, Const<A, B>>apply(Const::new, s).runConst());
    }
}
