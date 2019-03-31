package testsupport;

import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.function.Function;

import static com.jnape.palatable.lambda.io.IO.io;
import static java.util.Objects.hash;

public final class EqualityAwareIO<A> implements Monad<A, EqualityAwareIO<?>> {
    private final IO<A> io;

    public EqualityAwareIO(IO<A> io) {
        this.io = io;
    }

    @Override
    public <B> EqualityAwareIO<B> flatMap(Function<? super A, ? extends Monad<B, EqualityAwareIO<?>>> f) {
        return new EqualityAwareIO<>(io.flatMap(f.andThen(x -> x.<EqualityAwareIO<B>>coerce().io)));
    }

    @Override
    public <B> EqualityAwareIO<B> pure(B b) {
        return new EqualityAwareIO<>(io(b));
    }

    @Override
    public <B> EqualityAwareIO<B> fmap(Function<? super A, ? extends B> fn) {
        return new EqualityAwareIO<>(io.fmap(fn));
    }

    @Override
    public <B> EqualityAwareIO<B> zip(Applicative<Function<? super A, ? extends B>, EqualityAwareIO<?>> appFn) {
        return new EqualityAwareIO<>(io.zip(appFn.<EqualityAwareIO<Function<? super A, ? extends B>>>coerce().io));
    }

    @Override
    public <B> EqualityAwareIO<B> discardL(Applicative<B, EqualityAwareIO<?>> appB) {
        return new EqualityAwareIO<>(io.discardL(appB.<EqualityAwareIO<B>>coerce().io));
    }

    @Override
    public <B> EqualityAwareIO<A> discardR(Applicative<B, EqualityAwareIO<?>> appB) {
        return new EqualityAwareIO<>(io.discardR(appB.<EqualityAwareIO<B>>coerce().io));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        return other instanceof EqualityAwareIO
                && ((EqualityAwareIO<A>) other).io.unsafePerformIO().equals(io.unsafePerformIO());
    }

    @Override
    public int hashCode() {
        return hash(io);
    }
}
