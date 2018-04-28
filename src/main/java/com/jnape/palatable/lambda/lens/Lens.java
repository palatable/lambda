package com.jnape.palatable.lambda.lens;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Both;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.jnape.palatable.lambda.lens.Lens.Simple.adapt;
import static com.jnape.palatable.lambda.lens.functions.Over.over;
import static com.jnape.palatable.lambda.lens.functions.Set.set;
import static com.jnape.palatable.lambda.lens.functions.View.view;

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
 */
@FunctionalInterface
public interface Lens<S, T, A, B> extends LensLike<S, T, A, B, Lens> {

    @Override
    default <U> Lens<S, U, A, B> fmap(Function<? super T, ? extends U> fn) {
        return LensLike.super.<U>fmap(fn).coerce();
    }

    @Override
    default <U> Lens<S, U, A, B> pure(U u) {
        return lens(view(this), (s, b) -> u);
    }

    @Override
    default <U> Lens<S, U, A, B> zip(Applicative<Function<? super T, ? extends U>, LensLike<S, ?, A, B, Lens>> appFn) {
        return LensLike.super.zip(appFn).coerce();
    }

    @Override
    default <U> Lens<S, U, A, B> discardL(Applicative<U, LensLike<S, ?, A, B, Lens>> appB) {
        return LensLike.super.discardL(appB).coerce();
    }

    @Override
    default <U> Lens<S, T, A, B> discardR(Applicative<U, LensLike<S, ?, A, B, Lens>> appB) {
        return LensLike.super.discardR(appB).coerce();
    }

    @Override
    default <U> Lens<S, U, A, B> flatMap(Function<? super T, ? extends Monad<U, LensLike<S, ?, A, B, Lens>>> f) {
        return lens(view(this), (s, b) -> set(f.apply(set(this, b, s)).<Lens<S, U, A, B>>coerce(), b, s));
    }

    @Override
    default <R> Lens<R, T, A, B> diMapL(Function<? super R, ? extends S> fn) {
        return LensLike.super.<R>diMapL(fn).coerce();
    }

    @Override
    default <U> Lens<S, U, A, B> diMapR(Function<? super T, ? extends U> fn) {
        return LensLike.super.<U>diMapR(fn).coerce();
    }

    @Override
    default <R, U> Lens<R, U, A, B> diMap(Function<? super R, ? extends S> lFn,
                                          Function<? super T, ? extends U> rFn) {
        return LensLike.super.<R, U>diMap(lFn, rFn).coerce();
    }

    @Override
    default <R> Lens<R, T, A, B> contraMap(Function<? super R, ? extends S> fn) {
        return LensLike.super.<R>contraMap(fn).coerce();
    }

    @Override
    default <R> Lens<R, T, A, B> mapS(Function<? super R, ? extends S> fn) {
        return lens(view(this).compose(fn), (r, b) -> set(this, b, fn.apply(r)));
    }

    @Override
    default <U> Lens<S, U, A, B> mapT(Function<? super T, ? extends U> fn) {
        return fmap(fn);
    }

    @Override
    default <C> Lens<S, T, C, B> mapA(Function<? super A, ? extends C> fn) {
        return andThen(lens(fn, (a, b) -> b));
    }

    @Override
    default <Z> Lens<S, T, A, Z> mapB(Function<? super Z, ? extends B> fn) {
        return lens(view(this), (s, z) -> set(this, fn.apply(z), s));
    }

    /**
     * Left-to-right composition of lenses. Requires compatibility between S and T.
     *
     * @param f   the other lens
     * @param <C> the new "smaller" value to read (previously A)
     * @param <D> the new "smaller" update value (previously B)
     * @return the composed lens
     */
    default <C, D> Lens<S, T, C, D> andThen(Lens<A, B, C, D> f) {
        return f.compose(this);
    }

    /**
     * Right-to-left composition of lenses. Requires compatibility between A and B.
     *
     * @param g   the other lens
     * @param <Q> the new "larger" value for reading (previously S)
     * @param <R> the new "larger" value for putting (previously T)
     * @return the composed lens
     */
    default <Q, R> Lens<Q, R, A, B> compose(Lens<Q, R, S, T> g) {
        return lens(view(g).fmap(view(this)), (q, b) -> over(g, set(this, b), q));
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
        return new Lens<S, T, A, B>() {
            @Override
            @SuppressWarnings("unchecked")
            public <F extends Functor, FT extends Functor<T, F>, FB extends Functor<B, F>> FT apply(
                    Function<? super A, ? extends FB> fn,
                    S s) {
                return (FT) fn.apply(getter.apply(s)).fmap(b -> setter.apply(s, b));
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
    interface Simple<S, A> extends Lens<S, S, A, A>, LensLike.Simple<S, A, Lens> {

        /**
         * Compose two simple lenses from right to left.
         *
         * @param g   the other simple lens
         * @param <R> the other simple lens' larger type
         * @return the composed simple lens
         */
        default <R> Lens.Simple<R, A> compose(Lens.Simple<R, S> g) {
            return Lens.Simple.adapt(Lens.super.compose(g));
        }

        /**
         * Compose two simple lenses from left to right.
         *
         * @param f   the other simple lens
         * @param <B> the other simple lens' smaller type
         * @return the composed simple lens
         */
        default <B> Lens.Simple<S, B> andThen(Lens.Simple<A, B> f) {
            return f.compose(this);
        }

        /**
         * Adapt a {@link Lens} with the right variance to a {@link Lens.Simple}.
         *
         * @param lens the lens
         * @param <S>  S/T
         * @param <A>  A/B
         * @return the simple lens
         */
        @SuppressWarnings("unchecked")
        static <S, A> Lens.Simple<S, A> adapt(Lens<S, S, A, A> lens) {
            return lens::apply;
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
