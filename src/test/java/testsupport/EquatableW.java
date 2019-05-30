package testsupport;

import com.jnape.palatable.lambda.comonad.Comonad;
import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Objects;

import static com.jnape.palatable.lambda.functions.Fn1.fn1;

public final class EquatableW<W extends Comonad<?, W>, A> implements Comonad<A, EquatableW<W, ?>> {

    private final Comonad<A, W> wa;
    private final Fn1<? super W, ?> equatable;

    public EquatableW(Comonad<A, W> wa, Fn1<? super W, ?> equatable) {
        this.wa = wa;
        this.equatable = equatable;
    }

    @Override
    public A extract() {
        return wa.extract();
    }

    @Override
    public <B> Comonad<B, EquatableW<W, ?>> extendImpl(Fn1<? super Comonad<A, EquatableW<W, ?>>, ? extends B> f) {
        return new EquatableW<>(wa.extendImpl(f.contraMap(fn1(eq -> new EquatableW<>(eq, equatable)))), equatable);
    }

    @Override
    public <B> EquatableW<W, B> fmap(Fn1<? super A, ? extends B> fn) {
        return new EquatableW<>(wa.fmap(fn), equatable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (other instanceof EquatableW<?, ?>) {
            EquatableW<W, ?> that = (EquatableW<W, ?>) other;
            return Objects.equals(equatable.apply((W) wa), that.equatable.apply((W) that.wa));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
