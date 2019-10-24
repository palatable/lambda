package com.jnape.palatable.lambda.functions.ordering;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct3;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn3.Compare;

import java.util.Comparator;

/**
 * Specialized {@link CoProduct3} representing the possible results of a ordered comparison.
 * Used by {@link Compare} as the result of a comparison.
 *
 * @see Compare
 */
public abstract class ComparisonRelation
        implements CoProduct3<ComparisonRelation.LessThan, ComparisonRelation.Equal, ComparisonRelation.GreaterThan, ComparisonRelation> {
    private ComparisonRelation() { }

    /**
     * Return a comparison relation from the result of a {@link Comparator} or {@link Comparable} result
     *
     * @param signifier The result of {@link Comparator#compare(Object, Object)} or {@link Comparable#compareTo(Object)}
     * @return The intended {@link ComparisonRelation} of the signifier
     */
    public static ComparisonRelation fromInt(int signifier) {
        return signifier > 0 ? greaterThan() : signifier == 0 ? equal() : lessThan();
    }

    public static GreaterThan greaterThan() {
        return GreaterThan.INSTANCE;
    }

    public static LessThan lessThan() {
        return LessThan.INSTANCE;
    }

    public static Equal equal() {
        return Equal.INSTANCE;
    }

    public final static class LessThan extends ComparisonRelation {
        private static final LessThan INSTANCE = new LessThan();

        private LessThan() {
        }

        @Override
        public String toString() {
            return "LessThan";
        }

        @Override
        public <R> R match(Fn1<? super LessThan, ? extends R> aFn, Fn1<? super Equal, ? extends R> bFn, Fn1<? super GreaterThan, ? extends R> cFn) {
            return aFn.apply(this);
        }
    }

    public final static class Equal extends ComparisonRelation {
        private static final Equal INSTANCE = new Equal();

        private Equal() {
        }

        public String toString() {
            return "Equal";
        }

        @Override
        public <R> R match(Fn1<? super LessThan, ? extends R> aFn, Fn1<? super Equal, ? extends R> bFn, Fn1<? super GreaterThan, ? extends R> cFn) {
            return bFn.apply(this);
        }
    }

    public final static class GreaterThan extends ComparisonRelation {
        private static final GreaterThan INSTANCE = new GreaterThan();

        private GreaterThan() { }

        @Override
        public String toString() {
            return "GreaterThan";
        }

        @Override
        public <R> R match(Fn1<? super LessThan, ? extends R> aFn, Fn1<? super Equal, ? extends R> bFn, Fn1<? super GreaterThan, ? extends R> cFn) {
            return cFn.apply(this);
        }
    }
}
