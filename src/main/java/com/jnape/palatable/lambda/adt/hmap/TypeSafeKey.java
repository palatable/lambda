package com.jnape.palatable.lambda.adt.hmap;

import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.optics.Iso;
import com.jnape.palatable.lambda.optics.Optic;

import java.util.Objects;

/**
 * An interface representing a parametrized key for use in {@link HMap}s. Additionally, every {@link TypeSafeKey} is an
 * {@link Iso} from the type the value is stored as to the type it's viewed and set as (on the way in / on the way out).
 * This allows multiple keys to map to the same value, but to view the value as different types.
 * <p>
 * This is intentionally an interface so user-defined implementations are possible; however, it's important to note
 * that all hopes of type-safety hinge on equality being implemented such that no two {@link TypeSafeKey}s with
 * differing value-type parameters may be considered equal. Reference equality is used here as the default, as that is
 * sufficient.
 *
 * @param <A> The raw type of the value that this key maps to inside an {@link HMap}
 * @param <B> The mapped type of the value that this key maps to inside an {@link HMap}
 */
public interface TypeSafeKey<A, B> extends Iso.Simple<A, B> {


    @Override
    default <U> TypeSafeKey<A, B> discardR(Applicative<U, Iso<A, ?, B, B>> appB) {
        Iso.Simple<A, B> discarded = Iso.Simple.super.discardR(appB);
        return new TypeSafeKey<A, B>() {
            @Override
            public <CoP extends Profunctor<?, ?, ? extends Profunctor<?, ?, ?>>, CoF extends Functor<?, ? extends Functor<?, ?>>, FB extends Functor<B, ? extends CoF>, FT extends Functor<A, ? extends CoF>, PAFB extends Profunctor<B, FB, ? extends CoP>, PSFT extends Profunctor<A, FT, ? extends CoP>> PSFT apply(
                    PAFB pafb) {
                return discarded.apply(pafb);
            }

            @Override
            public int hashCode() {
                return TypeSafeKey.this.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                return TypeSafeKey.this.equals(obj);
            }
        };
    }

    /**
     * Left-to-right composition of this {@link TypeSafeKey} with some other {@link Iso}. Because the first parameter
     * fundamentally represents an already stored value type, this is the only composition that is possible for
     * {@link TypeSafeKey}, which is why only this (and not {@link Iso#compose(Optic)}) is overridden.
     * <p>
     * Particularly of note is the fact that values stored at this key are still stored as their original manifest
     * type, and are not duplicated - which is to say, putting a value at a key, yielding a new key via composition,
     * and putting a new value at the new key still only results in a single entry in the {@link HMap}. Additionally,
     * all previous keys involved in the new key's composition are still able to resolve the value in their native type.
     *
     * @param f   the other simple iso
     * @param <C> the new value type
     * @return the new {@link TypeSafeKey}
     */
    @Override
    default <C> TypeSafeKey<A, C>andThen(Iso.Simple<B, C> f) {
        Iso.Simple<A, C> composed = Iso.Simple.super.andThen(f);
        return new TypeSafeKey<A, C>() {
            @Override
            public <CoP extends Profunctor<?, ?, ? extends Profunctor<?, ?, ?>>, CoF extends Functor<?, ? extends Functor<?, ?>>, FB extends Functor<C, ? extends CoF>, FT extends Functor<A, ? extends CoF>, PAFB extends Profunctor<C, FB, ? extends CoP>, PSFT extends Profunctor<A, FT, ? extends CoP>> PSFT apply(
                    PAFB pafb) {
                return composed.apply(pafb);
            }

            @Override
            public int hashCode() {
                return TypeSafeKey.this.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                return TypeSafeKey.this.equals(obj);
            }
        };
    }

    /**
     * Static factory method for creating a simple type-safe key
     *
     * @param <A> the type of value stored at this key
     * @return a unique type-safe key
     */
    static <A> Simple<A> typeSafeKey() {
        return new TypeSafeKey.Simple<A>() {
            @Override
            public boolean equals(Object obj) {
                return obj instanceof Simple ? this == obj : Objects.equals(obj, this);
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }
        };
    }

    /**
     * A simplified {@link TypeSafeKey} that can only view a value of type <code>A</code> as an <code>A</code>.
     *
     * @param <A> The type of the value that this key maps to inside an {@link HMap}
     */
    interface Simple<A> extends TypeSafeKey<A, A> {

        @Override
        @SuppressWarnings("unchecked")
        default <CoP extends Profunctor<?, ?, ? extends Profunctor<?, ?, ?>>,
                CoF extends Functor<?, ? extends Functor<?, ?>>,
                FB extends Functor<A, ? extends CoF>,
                FT extends Functor<A, ? extends CoF>,
                PAFB extends Profunctor<A, FB, ? extends CoP>,
                PSFT extends Profunctor<A, FT, ? extends CoP>> PSFT apply(
                PAFB pafb) {
            return (PSFT) pafb;
        }
    }
}