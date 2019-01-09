package testsupport;

import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.function.Function;

import static java.util.Objects.hash;

public final class EqualityAwareIO<A> implements IO<A> {
    private final IO<A> io;

    public EqualityAwareIO(IO<A> io) {
        this.io = io;
    }

    @Override
    public A unsafePerformIO() {
        return io.unsafePerformIO();
    }

    @Override
    public <B> EqualityAwareIO<B> flatMap(Function<? super A, ? extends Monad<B, IO<?>>> f) {
        return new EqualityAwareIO<>(io.flatMap(f));
    }

    @Override
    public <B> EqualityAwareIO<B> fmap(Function<? super A, ? extends B> f) {
        return new EqualityAwareIO<>(io.fmap(f));
    }

    @Override
    public <B> EqualityAwareIO<B> zip(Applicative<Function<? super A, ? extends B>, IO<?>> appFn) {
        return new EqualityAwareIO<>(io.zip(appFn));
    }

    @Override
    public <B> EqualityAwareIO<B> pure(B b) {
        return new EqualityAwareIO<>(io.pure(b));
    }


    @Override
    public <B> EqualityAwareIO<B> discardL(Applicative<B, IO<?>> appB) {
        return new EqualityAwareIO<>(io.discardL(appB));
    }

    @Override
    public <B> EqualityAwareIO<A> discardR(Applicative<B, IO<?>> appB) {
        return new EqualityAwareIO<>(io.discardR(appB));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        return other instanceof IO && ((IO<A>) other).unsafePerformIO().equals(unsafePerformIO());
    }

    @Override
    public int hashCode() {
        return hash(io);
    }
}
