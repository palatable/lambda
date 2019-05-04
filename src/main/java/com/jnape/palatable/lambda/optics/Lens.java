package com.jnape.palatable.lambda.optics;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Both;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.jnape.palatable.lambda.optics.Iso.iso;
import static com.jnape.palatable.lambda.optics.Lens.Simple.adapt;
import static com.jnape.palatable.lambda.optics.functions.Set.set;
import static com.jnape.palatable.lambda.optics.functions.View.view;

/**
 * An approximation of <a href="http://www.twanvl.nl/blog/haskell/cps-functional-references">van Laarhoven lenses</a>.
 * <p>
 * A "lens" can be considered in its simplest form as the conjugation of a "getter" and a "setter"; that is, a
 * unification type representing the way to retrieve a "smaller" value <code>A</code> from a "larger" value
 * <code>S</code>, as well as a way to update a "smaller" value <code>B</code> of a "larger" value <code>S</code>,
 * producing another "larger" value <code>T</code>.
 * <p>
 * Consider the following example:
 * <pre>
 * {@code
 * public final class Person {
 *     private final int age;
 *
 *     public Person(int age) {
 *         this.age = age;
 *     }
 *
 *     public int getAge() {
 *         return age;
 *     }
 *
 *     public Person setAge(int age) {
 *         return new Person(age);
 *     }
 * }
 * }
 * </pre>
 * A lens that focused on the <code>age</code> field of an instance of <code>Person</code> might look like this:
 * <pre>
 * {@code
 * Lens<Person, Person, Integer, Integer> ageLens = Lens.lens(Person::getAge, Person::setAge);
 *
 * Person adult = new Person(18);
 * Integer age = view(ageLens, adult); // 18
 *
 * Person olderAdult = set(ageLens, 19, adult);
 * Integer olderAge = view(ageLens, olderAdult); // 19
 * }
 * </pre>
 * The pattern of a getter and setter that mutually agree on both <code>A</code> and <code>B</code> as well as on both
 * <code>S</code> and <code>T</code> is so common that this can be given a simplified type signature:
 * <pre>
 * {@code
 * Lens.Simple<Person, Integer> ageLens = Lens.simpleLens(Person::getAge, Person::setAge);
 *
 * Person adult = new Person(18);
 * Integer age = view(ageLens, adult); // 18
 *
 * Person olderAdult = set(ageLens, 19, adult);
 * Integer olderAge = view(ageLens, olderAdult); // 19
 * }
 * </pre>
 * However, consider if <code>age</code> could be updated on a <code>Person</code> by being provided a date of birth, in
 * the form of a <code>LocalDate</code>:
 * <pre>
 * {@code
 * public final class Person {
 *     private final int age;
 *
 *     public Person(int age) {
 *         this.age = age;
 *     }
 *
 *     public int getAge() {
 *         return age;
 *     }
 *
 *     public Person setAge(int age) {
 *         return new Person(age);
 *     }
 *
 *     public Person setAge(LocalDate dob) {
 *         return setAge((int) YEARS.between(dob, LocalDate.now()));
 *     }
 * }
 * }
 * </pre>
 * This is why Lens has both an <code>A</code> and a <code>B</code>: <code>A</code> is the value for "getting", and
 * <code>B</code> is the potentially different value for "setting". This distinction makes lenses powerful enough to
 * express the more complicated <code>setAge</code> case naturally:
 * <pre>
 * {@code
 * Lens<Person, Person, Integer, LocalDate> ageDobLens = Lens.lens(Person::getAge, Person::setAge);
 *
 * Person adult = new Person(18);
 * Integer age = view(ageDobLens, adult); // 18
 *
 * Person olderAdult = set(ageDobLens, LocalDate.of(1997, 1, 1), adult);
 * Integer olderAge = view(ageDobLens, olderAdult); // 19 at the time of this writing...anyone else feel old?
 * }
 * </pre>
 * Additionally, we might imagine a lens that produces a different "larger" value on updating than what was given.
 * Consider a lens that reads the first string from a list, but produces a Set of strings on update:
 * <pre>
 * {@code
 * Lens<List<String>, Set<String>, String, String> lens = Lens.lens(
 *         l -> l.get(0),
 *         (l, s) -> {
 *             List<String> copy = new ArrayList<>(l);
 *             copy.set(0, s);
 *             return new HashSet<>(copy);
 *         });
 *
 * String firstElement = view(lens, asList("foo", "bar")); // "foo
 * System.out.println(firstElement);
 *
 * set(lens, "oof", asList("foo", "bar")); // ["bar", "oof"]
 * set(lens, "bar", asList("foo", "bar")); // ["bar"]
 * }
 * </pre>
 * For more information, <a href="https://www.youtube.com/watch?v=cefnmjtAolY">learn</a>
 * <a href="http://skillsmatter.com/podcast/scala/lenses-compositional-data-access-and-manipulation">about</a>
 * <a href="http://r6.ca/blog/20120623T104901Z.html">lenses</a>.
 *
 * @param <S> the type of the "larger" value for reading
 * @param <T> the type of the "larger" value for putting
 * @param <A> the type of the "smaller" value that is read
 * @param <B> the type of the "smaller" update value
 * @see Optic
 * @see Iso
 */
@FunctionalInterface
public interface Lens<S, T, A, B> extends
        Optic<Fn1<?, ?>, Functor<?, ?>, S, T, A, B>,
        Monad<T, Lens<S, ?, A, B>>,
        Profunctor<S, T, Lens<?, ?, A, B>> {

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Lens<S, U, A, B> fmap(Function<? super T, ? extends U> fn) {
        return Monad.super.<U>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Lens<S, U, A, B> pure(U u) {
        return lens(view(this), (s, b) -> u);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Lens<S, U, A, B> zip(Applicative<Function<? super T, ? extends U>, Lens<S, ?, A, B>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Lens<S, U, A, B> discardL(Applicative<U, Lens<S, ?, A, B>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Lens<S, T, A, B> discardR(Applicative<U, Lens<S, ?, A, B>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Lens<S, U, A, B> flatMap(Function<? super T, ? extends Monad<U, Lens<S, ?, A, B>>> f) {

        return lens(view(this), (s, b) -> set(f.apply(set(this, b, s)).<Lens<S, U, A, B>>coerce(), b, s));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <R> Lens<R, T, A, B> diMapL(Function<? super R, ? extends S> fn) {
        return (Lens<R, T, A, B>) Profunctor.super.<R>diMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Lens<S, U, A, B> diMapR(Function<? super T, ? extends U> fn) {
        return (Lens<S, U, A, B>) Profunctor.super.<U>diMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <R, U> Lens<R, U, A, B> diMap(Function<? super R, ? extends S> lFn,
                                          Function<? super T, ? extends U> rFn) {
        return this.<R>mapS(lFn).mapT(rFn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <R> Lens<R, T, A, B> contraMap(Function<? super R, ? extends S> fn) {
        return (Lens<R, T, A, B>) Profunctor.super.<R>contraMap(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <R> Lens<R, T, A, B> mapS(Function<? super R, ? extends S> fn) {
        return lens(Optic.super.mapS(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> Lens<S, U, A, B> mapT(Function<? super T, ? extends U> fn) {
        return lens(Optic.super.mapT(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Lens<S, T, C, B> mapA(Function<? super A, ? extends C> fn) {
        return lens(Optic.super.mapA(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Lens<S, T, A, Z> mapB(Function<? super Z, ? extends B> fn) {
        return lens(Optic.super.mapB(fn));
    }

    /**
     * Produce an {@link Iso} from this {@link Lens} by providing a default <code>S</code> value.
     *
     * @param s the default <code>S</code>
     * @return an {@link Iso}
     */
    default Iso<S, T, A, B> toIso(S s) {
        return iso(view(this), set(this).flip().apply(s));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C, D> Lens<S, T, C, D> andThen(Optic<? super Fn1<?, ?>, ? super Functor<?, ?>, A, B, C, D> f) {
        return lens(Optic.super.andThen(f));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <R, U> Lens<R, U, A, B> compose(Optic<? super Fn1<?, ?>, ? super Functor<?, ?>, R, U, S, T> g) {
        return lens(Optic.super.compose(g));
    }

    /**
     * Static factory method for creating a lens from a getter function and a setter function.
     *
     * @param getter the getter function
     * @param setter the setter function
     * @param <S>    the type of the "larger" value for reading
     * @param <T>    the type of the "larger" value for putting
     * @param <A>    the type of the "smaller" value that is read
     * @param <B>    the type of the "smaller" update value
     * @return the lens
     */
    static <S, T, A, B> Lens<S, T, A, B> lens(Function<? super S, ? extends A> getter,
                                              BiFunction<? super S, ? super B, ? extends T> setter) {
        return lens(Optic.<Fn1<?, ?>, Functor<?, ?>,
                S, T, A, B,
                Functor<B, ? extends Functor<?, ?>>,
                Functor<T, ? extends Functor<?, ?>>,
                Fn1<A, Functor<B, ? extends Functor<?, ?>>>,
                Fn1<S, Functor<T, ? extends Functor<?, ?>>>>optic(afb -> s -> afb.apply(getter.apply(s))
                .fmap(b -> setter.apply(s, b))));
    }

    /**
     * Promote an optic with compatible bounds to a {@link Lens}.
     *
     * @param optic the {@link Optic}
     * @param <S>   the type of the "larger" value for reading
     * @param <T>   the type of the "larger" value for putting
     * @param <A>   the type of the "smaller" value that is read
     * @param <B>   the type of the "smaller" update value
     * @return the {@link Lens}
     */
    static <S, T, A, B> Lens<S, T, A, B> lens(Optic<? super Fn1<?, ?>, ? super Functor<?, ?>, S, T, A, B> optic) {
        return new Lens<S, T, A, B>() {
            @Override
            public <CoP extends Profunctor<?, ?, ? extends Fn1<?, ?>>, CoF extends Functor<?, ? extends Functor<?, ?>>,
                    FB extends Functor<B, ? extends CoF>, FT extends Functor<T, ? extends CoF>,
                    PAFB extends Profunctor<A, FB, ? extends CoP>,
                    PSFT extends Profunctor<S, FT, ? extends CoP>> PSFT apply(PAFB pafb) {
                return optic.apply(pafb);
            }
        };
    }

    /**
     * Static factory method for creating a simple lens from a getter function and a setter function.
     *
     * @param getter the getter function
     * @param setter the setter function
     * @param <S>    the type of both "larger" values
     * @param <A>    the type of both "smaller" values
     * @return the lens
     */
    static <S, A> Lens.Simple<S, A> simpleLens(Function<? super S, ? extends A> getter,
                                               BiFunction<? super S, ? super A, ? extends S> setter) {
        return adapt(lens(getter, setter));
    }

    /**
     * Dually focus on two lenses at the same time. Requires <code>S</code> and <code>T</code> to be invariant between
     * lenses.
     *
     * @param f   the first lens
     * @param g   the second lens
     * @param <S> both larger values
     * @param <A> f's smaller viewing value
     * @param <B> g's smaller viewing value
     * @param <C> f's smaller setting value
     * @param <D> g's smaller setting value
     * @return the dual-focus lens
     */
    static <S, A, B, C, D> Lens<S, S, Tuple2<A, B>, Tuple2<C, D>> both(Lens<S, S, A, C> f, Lens<S, S, B, D> g) {
        return lens(Both.both(view(f), view(g)), (s, cd) -> cd.biMap(set(f), set(g)).into(Fn1::compose).apply(s));
    }

    /**
     * Dually focus on two simple lenses at the same time.
     *
     * @param f   the first lens
     * @param g   the second lens
     * @param <S> both larger values
     * @param <A> both smaller viewing values
     * @param <B> both smaller setting values
     * @return the dual-focus simple lens
     */
    static <S, A, B> Lens.Simple<S, Tuple2<A, B>> both(Lens.Simple<S, A> f, Lens.Simple<S, B> g) {
        return adapt(both((Lens<S, S, A, A>) f, g));
    }

    /**
     * A convenience type with a simplified type signature for common lenses with both unified "larger" values and
     * unified "smaller" values.
     *
     * @param <S> the type of both "larger" values
     * @param <A> the type of both "smaller" values
     */
    @FunctionalInterface
    interface Simple<S, A> extends Lens<S, S, A, A>, Optic.Simple<Fn1<?, ?>, Functor<?, ?>, S, A> {

        /**
         * {@inheritDoc}
         */
        @Override
        default <B> Lens.Simple<S, B> andThen(Optic.Simple<? super Fn1<?, ?>, ? super Functor<?, ?>, A, B> f) {
            return Lens.Simple.adapt(Lens.super.andThen(f));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        default <R> Lens.Simple<R, A> compose(Optic.Simple<? super Fn1<?, ?>, ? super Functor<?, ?>, R, S> g) {
            return Lens.Simple.adapt(Lens.super.compose(g));
        }

        /**
         * Adapt a {@link Lens} with the right variance to a {@link Lens.Simple}.
         *
         * @param lens the lens
         * @param <S>  S/T
         * @param <A>  A/B
         * @return the simple lens
         */
        static <S, A> Lens.Simple<S, A> adapt(Optic<? super Fn1<?, ?>, ? super Functor<?, ?>, S, S, A, A> lens) {
            return new Lens.Simple<S, A>() {
                @Override
                public <CoP extends Profunctor<?, ?, ? extends Fn1<?, ?>>,
                        CoF extends Functor<?, ? extends Functor<?, ?>>, FB extends Functor<A, ? extends CoF>,
                        FT extends Functor<S, ? extends CoF>, PAFB extends Profunctor<A, FB, ? extends CoP>,
                        PSFT extends Profunctor<S, FT, ? extends CoP>> PSFT apply(PAFB pafb) {
                    return lens.apply(pafb);
                }
            };
        }

        /**
         * Specialization of {@link Lens#both(Lens, Lens)} for simple lenses.
         *
         * @param f   the first lens
         * @param g   the second lens
         * @param <S> both lens larger values
         * @param <A> lens f smaller values
         * @param <B> lens g smaller values
         * @return the dual-focus simple lens
         */
        static <S, A, B> Lens.Simple<S, Tuple2<A, B>> both(Lens<S, S, A, A> f, Lens<S, S, B, B> g) {
            return Lens.Simple.adapt(Lens.both(f, g));
        }
    }
}
