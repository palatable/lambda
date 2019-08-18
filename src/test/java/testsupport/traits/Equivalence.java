package testsupport.traits;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Objects;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

public final class Equivalence<A> {

    private final A                      value;
    private final Fn1<? super A, Object> eq;

    private Equivalence(A value, Fn1<? super A, Object> eq) {
        this.value = value;
        this.eq = eq;
    }

    public A getValue() {
        return value;
    }

    public Equivalence<A> swap(A a) {
        return invMap(constantly(a));
    }

    public Equivalence<A> invMap(Fn1<? super A, ? extends A> equivalence) {
        return new Equivalence<>(value, equivalence.fmap(this.eq));
    }

    @Override
    public String toString() {
        return value.getClass().getSimpleName() + " (surrogate)";
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Equivalence) {
            @SuppressWarnings("unchecked") Equivalence<A> other = (Equivalence<A>) obj;
            return Objects.equals(eq.apply(value), other.eq.apply(other.value));
        }
        return false;
    }

    public static <A> Equivalence<A> equivalence(A value, Fn1<? super A, Object> equivalence) {
        return new Equivalence<>(value, equivalence);
    }
}
