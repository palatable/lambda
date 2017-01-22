package testsupport.recursion;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.Objects;
import java.util.function.Function;

public abstract class NatF<A> implements Functor<A, NatF>, CoProduct2<NatF.Z<A>, NatF.S<A>> {

    public static <A> NatF<A> z() {
        return new Z<>();
    }

    public static <A> NatF<A> s(A a) {
        return new S<>(a);
    }

    public static final class Z<A> extends NatF<A> {

        private Z() {
        }

        @Override
        @SuppressWarnings("unchecked")
        public <B> NatF<B> fmap(Function<? super A, ? extends B> fn) {
            return (NatF<B>) this;
        }

        @Override
        public <R> R match(Function<? super Z<A>, ? extends R> aFn, Function<? super S<A>, ? extends R> bFn) {
            return aFn.apply(this);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Z;
        }

        @Override
        public int hashCode() {
            return 31;
        }
    }

    public static final class S<A> extends NatF<A> {
        private final A a;

        private S(A a) {
            this.a = a;
        }

        public A carrier() {
            return a;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <B> NatF<B> fmap(Function<? super A, ? extends B> fn) {
            return new S<>(fn.apply(a));
        }

        @Override
        public <R> R match(Function<? super Z<A>, ? extends R> aFn, Function<? super S<A>, ? extends R> bFn) {
            return bFn.apply(this);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof S && Objects.equals(carrier(), ((S) obj).carrier());
        }
    }
}