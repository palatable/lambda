package com.jnape.palatable.lambda.lens;

import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
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
 * @param <S> The type of the "larger" value for reading
 * @param <T> The type of the "larger" value for putting
 * @param <A> The type of the "smaller" value that is read
 * @param <B> The type of the "smaller" update value
 */
@FunctionalInterface
public interface Lens<S, T, A, B> extends Functor<T> {

    <FT extends Functor<T>, FB extends Functor<B>> FT apply(Function<? super A, ? extends FB> fn, S s);

    /**
     * Fix this lens against some functor, producing a non-polymorphic runnable lens as an {@link Fn2}.
     * <p>
     * Although the Java type system does not allow enforceability, the functor instance FT should be the same as FB,
     * only differentiating in their parameters.
     *
     * @param <FT> The type of the lifted T
     * @param <FB> The type of the lifted B
     * @return the lens, "fixed" to the functor
     */
    default <FT extends Functor<T>, FB extends Functor<B>> Fixed<S, T, A, B, FT, FB> fix() {
        return this::apply;
    }

    @Override
    default <U> Lens<S, U, A, B> fmap(Function<? super T, ? extends U> fn) {
        return this.compose(Lens.<S, U, S, T>lens(id(), (s, t) -> fn.apply(t)));
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
     * @param <S>    The type of the "larger" value for reading
     * @param <T>    The type of the "larger" value for putting
     * @param <A>    The type of the "smaller" value that is read
     * @param <B>    The type of the "smaller" update value
     * @return the lens
     */
    static <S, T, A, B> Lens<S, T, A, B> lens(Function<? super S, ? extends A> getter,
                                              BiFunction<? super S, ? super B, ? extends T> setter) {
        return new Lens<S, T, A, B>() {
            @Override
            @SuppressWarnings("unchecked")
            public <FT extends Functor<T>, FB extends Functor<B>> FT apply(Function<? super A, ? extends FB> fn,
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
     * @param <S>    The type of both "larger" values
     * @param <A>    The type of both "smaller" values
     * @return the lens
     */
    @SuppressWarnings("unchecked")
    static <S, A> Lens.Simple<S, A> simpleLens(Function<? super S, ? extends A> getter,
                                               BiFunction<? super S, ? super A, ? extends S> setter) {
        return lens(getter, setter)::apply;
    }

    /**
     * A convenience type with a simplified type signature for common lenses with both unified "larger" values and
     * unified "smaller" values.
     *
     * @param <S> The type of both "larger" values
     * @param <A> The type of both "smaller" values
     */
    @FunctionalInterface
    interface Simple<S, A> extends Lens<S, S, A, A> {

        @Override
        default <FS extends Functor<S>, FA extends Functor<A>> Fixed<S, A, FS, FA> fix() {
            return this::apply;
        }

        @SuppressWarnings("unchecked")
        default <Q> Lens.Simple<Q, A> compose(Lens.Simple<Q, S> g) {
            return Lens.super.compose(g)::apply;
        }

        default <B> Lens.Simple<S, B> andThen(Lens.Simple<A, B> f) {
            return f.compose(this);
        }

        /**
         * A convenience type with a simplified type signature for fixed simple lenses.
         *
         * @param <S>  The type of both "larger" values
         * @param <A>  The type of both "smaller" values
         * @param <FS> The type of the lifted s
         * @param <FA> The type of the lifted A
         */
        @FunctionalInterface
        interface Fixed<S, A, FS extends Functor<S>, FA extends Functor<A>>
                extends Lens.Fixed<S, S, A, A, FS, FA> {
        }
    }

    /**
     * A lens that has been fixed to a functor. Because the lens is no longer polymorphic, it can additionally be safely
     * represented as an Fn2.
     *
     * @param <S>  The type of the "larger" value for reading
     * @param <T>  The type of the "larger" value for putting
     * @param <A>  The type of the "smaller" value that is read
     * @param <B>  The type of the "smaller" update value
     * @param <FT> The type of the lifted T
     * @param <FB> The type of the lifted B
     */
    @FunctionalInterface
    interface Fixed<S, T, A, B, FT extends Functor<T>, FB extends Functor<B>>
            extends Fn2<Function<? super A, ? extends FB>, S, FT> {
    }
}
