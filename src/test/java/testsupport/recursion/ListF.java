package testsupport.recursion;

import com.jnape.palatable.lambda.functor.Functor;

import java.util.function.Function;

public abstract class ListF<A, B> implements Functor<B, ListF<A, ?>> {

    public static <A, B> ListF<A, B> nil() {
        return new Nil<>();
    }

    public static <A, B> ListF<A, B> cons(@SuppressWarnings("unused") A a,
                                          @SuppressWarnings("unused") B b) {
        return new Cons<>();
    }

    public static final class Nil<A, B> extends ListF<A, B> {

        @Override
        @SuppressWarnings("unchecked")
        public <C> ListF<A, C> fmap(Function<? super B, ? extends C> fn) {
            return (Nil<A, C>) this;
        }
    }

    public static final class Cons<A, B> extends ListF<A, B> {

        @Override
        @SuppressWarnings("unchecked")
        public <C> ListF<A, C> fmap(Function<? super B, ? extends C> fn) {
            return (ListF<A, C>) this;
        }
    }
}
