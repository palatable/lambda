package com.jnape.palatable.lambda.functor.builtin;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn1.fn1;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;
import static com.jnape.palatable.lambda.functions.recursion.Trampoline.trampoline;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Cartesian;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadReader;
import com.jnape.palatable.lambda.monad.MonadRec;

/**
 * The lazy reader monad, a monad for any {@link Fn1 function} from some
 * environment of type <code>R</code>.
 *
 * @param <R> the environment type
 * @param <A> the embedded output type
 */
public class Reader<R, A>
		implements MonadReader<R, A, Reader<R, ?>>, Cartesian<R, A, Reader<?, ?>>, MonadRec<A, Reader<R, ?>> {
	private final Fn1<? super R, ? extends Tuple2<A, R>> readerFn;

	public Reader(Fn1<? super R, ? extends Tuple2<A, R>> f) {
		this.readerFn = f;
	}

	public Tuple2<A, R> run(R r) {
		return readerFn.apply(r);
	}

	public A eval(R r) {
		return run(r)._1();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader<R, A> local(Fn1<? super R, ? extends R> fn) {
		return reader(r -> eval(fn.apply(r)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <B> Reader<R, B> fmap(Fn1<? super A, ? extends B> fn) {
		return MonadRec.super.<B>fmap(fn).coerce();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <B> Reader<R, B> zip(Applicative<Fn1<? super A, ? extends B>, Reader<R, ?>> appFn) {
		return MonadRec.super.zip(appFn).coerce();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <B> Lazy<Reader<R, B>> lazyZip(
			Lazy<? extends Applicative<Fn1<? super A, ? extends B>, Reader<R, ?>>> lazyAppFn) {
		return MonadRec.super.lazyZip(lazyAppFn).fmap(MonadRec<B, Reader<R, ?>>::coerce);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <B> Reader<R, B> flatMap(Fn1<? super A, ? extends Monad<B, Reader<R, ?>>> f) {
		return reader(r -> f.apply(eval(r)).<Reader<R, B>>coerce().eval(r));
	}

	 /**
     * {@inheritDoc}
     */
	@Override
	public <B> Reader<R, B> rflatMap(Fn2<? super R, ? super A, ? extends MonadReader<R, B, Reader<R, ?>>> f) {
		return reader(r -> f.apply(r, eval(r)).<Reader<R, B>>coerce().eval(r));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <B> Reader<R, B> pure(B b) {
		return reader(r -> b);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <B> Reader<R, B> trampolineM(Fn1<? super A, ? extends MonadRec<RecursiveResult<A, B>, Reader<R, ?>>> fn) {
		return new Reader<>(
				fn1(this::run).fmap(trampoline(into((a, r) -> fn.apply(a).<Reader<R, RecursiveResult<A, B>>>coerce()
						.run(r).into((aOrB, r_) -> aOrB.biMap(a_ -> tuple(a_, r_), b -> tuple(b, r_)))))));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <B> Reader<R, B> discardL(Applicative<B, Reader<R, ?>> appB) {
		return MonadRec.super.discardL(appB).coerce();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <B> Reader<R, A> discardR(Applicative<B, Reader<R, ?>> appB) {
		return MonadRec.super.discardR(appB).coerce();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <B> Reader<Tuple2<B, R>, Tuple2<B, A>> cartesian() {
		return new Reader<>(t -> run(t._2()).fmap(tupler(t._1())).biMapL(tupler(t._1())));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <Z, C> Reader<Z, C> diMap(Fn1<? super Z, ? extends R> lFn, Fn1<? super A, ? extends C> rFn) {
		return reader(z -> run(lFn.apply(z)).biMapL(rFn)._1());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <Q> Reader<Q, A> diMapL(Fn1<? super Q, ? extends R> fn) {
		return (Reader<Q, A>) Cartesian.super.<Q>diMapL(fn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <B> Reader<R, B> diMapR(Fn1<? super A, ? extends B> fn) {
		return (Reader<R, B>) Cartesian.super.<B>diMapR(fn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <Q> Reader<Q, A> contraMap(Fn1<? super Q, ? extends R> fn) {
		return (Reader<Q, A>) Cartesian.super.<Q>contraMap(fn);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader<R, Tuple2<R, A>> carry() {
		return (Reader<R, Tuple2<R, A>>) Cartesian.super.carry();
	}

	/**
	 * Construct a {@link Reader} from a value.
	 *
	 * @param a   the output value
	 * @param <W> the accumulation type
	 * @param <A> the value type
	 * @return the {@link Writer}
	 */
	public static <R, A> Reader<R, A> listen(A a) {
		return reader(r -> a);
	}

	/**
	 * Lift a {@link Fn1 function} into a {@link Reader} instance.
	 *
	 * @param fn  the function
	 * @param <R> the input type
	 * @param <A> the embedded output type
	 * @return the {@link Reader}
	 */
	public static <R, A> Reader<R, A> reader(Fn1<? super R, ? extends A> fn) {
		return new Reader<>(r -> tuple(fn.apply(r), r));
	}

	/**
	 * The canonical {@link Pure} instance for {@link Reader}.
	 *
	 * @param <R> the input type
	 * @return the {@link Pure} instance
	 */
	public static <R> Pure<Reader<R, ?>> pureReader() {
		return new Pure<Reader<R, ?>>() {
			@Override
			public <A> Reader<R, A> checkedApply(A a) {
				return listen(a);
			}
		};
	}
}
