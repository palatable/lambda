package testsupport.recursion;

import com.jnape.palatable.lambda.recursionschemes.Fix;

public final class List<A> implements Fix<ListF<A, ?>, ListF<A, List<A>>> {

    private final ListF<A, List<A>> unfixed;

    private List(ListF<A, List<A>> unfixed) {
        this.unfixed = unfixed;
    }

    @Override
    public ListF<A, List<A>> unfix() {
        return unfixed;
    }

    public static <A> List<A> nil() {
        return new List<>(ListF.nil());
    }

    public static <A> List<A> cons(A head, List<A> tail) {
        return new List<>(ListF.cons(head, tail));
    }
}