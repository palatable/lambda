package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Lift;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.internal.ImmutableQueue;
import com.jnape.palatable.lambda.io.IO;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.MonadT;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.choice.Choice2.a;
import static com.jnape.palatable.lambda.adt.choice.Choice2.b;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn0.fn0;
import static com.jnape.palatable.lambda.functions.Fn1.withSelf;
import static com.jnape.palatable.lambda.functions.builtin.fn2.$.$;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Tupler2.tupler;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.monad.Monad.join;
import static com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT.maybeT;
import static java.util.Arrays.asList;

/**
 * A {@link MonadT monad transformer} over a co-inductive, singly-linked spine of values embedded in effects. This is
 * analogous to Haskell's ListT (<a href="https://wiki.haskell.org/ListT_done_right">done right</a>). All append
 * operations ({@link IterateT#cons(MonadRec) cons}, {@link IterateT#snoc(MonadRec) snoc}, etc.) are O(1) space/time
 * complexity.
 * <p>
 * Due to its singly-linked embedded design, {@link IterateT} is a canonical example of purely-functional streaming
 * computation. For example, to lazily print all lines from a file descriptor, an initial implementation using
 * {@link IterateT} might take the following form:
 * <pre><code>
 * String filePath = "/tmp/a_tale_of_two_cities.txt";
 * IterateT&lt;IO&lt;?&gt;, String&gt; streamLines = IterateT.unfold(
 *         reader -&gt; io(() -&gt; maybe(reader.readLine()).fmap(line -&gt; tuple(line, reader))),
 *         io(() -&gt; Files.newBufferedReader(Paths.get(filePath))));
 *
 * // iterative read and print lines without retaining references
 * IO&lt;Unit&gt; printLines = streamLines.forEach(line -&gt; io(() -&gt; System.out.println(line)));
 * printLines.unsafePerformIO(); // prints "It was the best of times, it was the worst of times, [...]"
 * </code></pre>
 *
 * @param <M> the effect type
 * @param <A> the element type
 */
public class IterateT<M extends MonadRec<?, M>, A> implements MonadT<M, A, IterateT<M, ?>, IterateT<?, ?>> {

    private final Pure<M>                                                                                     pureM;
    private final ImmutableQueue<Choice2<Fn0<MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M>>, MonadRec<A, M>>> spine;

    private IterateT(Pure<M> pureM,
                     ImmutableQueue<Choice2<Fn0<MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M>>, MonadRec<A, M>>> spine) {
        this.pureM = pureM;
        this.spine = spine;
    }

    /**
     * Recover the full structure of the embedded {@link Monad}.
     *
     * @param <MMTA> the witnessed target type
     * @return the embedded {@link Monad}
     */
    public <MMTA extends MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M>> MMTA runIterateT() {
        return pureM.<IterateT<M, A>, MonadRec<IterateT<M, A>, M>>apply(this)
                .<Maybe<Tuple2<A, IterateT<M, A>>>>trampolineM(iterateT -> iterateT.runStep()
                        .fmap(maybeMore -> maybeMore.match(
                                fn0(() -> terminate(nothing())),
                                t -> t.into((Maybe<A> maybeA, IterateT<M, A> as) -> maybeA.match(
                                        fn0(() -> recurse(as)),
                                        a -> terminate(just(tuple(a, as))))))))
                .coerce();
    }

    /**
     * Run a single step of this {@link IterateT}, where a step is the smallest amount of work that could possibly be
     * productive in advancing through the {@link IterateT}. Useful for implementing interleaving algorithms that
     * require {@link IterateT IterateTs} to yield, emit, or terminate as soon as possible, regardless of whether the
     * next element is readily available.
     *
     * @param <MStep> the witnessed target type of the step
     * @return the step
     */
    public <MStep extends MonadRec<Maybe<Tuple2<Maybe<A>, IterateT<M, A>>>, M>> MStep runStep() {
        return spine.head().match(
                fn0(() -> pureM.<Maybe<Tuple2<Maybe<A>, IterateT<M, A>>>, MStep>apply(nothing())),
                thunkOrReal -> thunkOrReal.match(
                        thunk -> thunk.apply().<Maybe<Tuple2<Maybe<A>, IterateT<M, A>>>>fmap(m -> m.match(
                                fn0(() -> just(tuple(nothing(), new IterateT<>(pureM, spine.tail())))),
                                t -> just(t.biMap(Maybe::just,
                                                  as -> new IterateT<>(pureM, as.spine.concat(spine.tail()))))))
                                .coerce(),
                        ma -> ma.fmap(a -> just(tuple(just(a), new IterateT<>(pureM, spine.tail())))).coerce()));
    }

    /**
     * Add an element inside an effect to the front of this {@link IterateT}.
     *
     * @param head the element
     * @return the cons'ed {@link IterateT}
     */
    public final IterateT<M, A> cons(MonadRec<A, M> head) {
        return new IterateT<>(pureM, spine.pushFront(b(head)));
    }

    /**
     * Add an element inside an effect to the back of this {@link IterateT}.
     *
     * @param last the element
     * @return the snoc'ed {@link IterateT}
     */
    public final IterateT<M, A> snoc(MonadRec<A, M> last) {
        return new IterateT<>(pureM, spine.pushBack(b(last)));
    }

    /**
     * Concat this {@link IterateT} in front of the <code>other</code> {@link IterateT}.
     *
     * @param other the other {@link IterateT}
     * @return the concatenated {@link IterateT}
     */
    public IterateT<M, A> concat(IterateT<M, A> other) {
        return new IterateT<>(pureM, spine.concat(other.spine));
    }

    /**
     * Monolithically fold the spine of this {@link IterateT} by {@link MonadRec#trampolineM(Fn1) trampolining} the
     * underlying effects (for iterative folding, use {@link IterateT#trampolineM(Fn1) trampolineM} directly).
     *
     * @param fn   the folding function
     * @param acc  the starting accumulation effect
     * @param <B>  the accumulation type
     * @param <MB> the witnessed target result type
     * @return the folded effect result
     */
    public <B, MB extends MonadRec<B, M>> MB fold(Fn2<? super B, ? super A, ? extends MonadRec<B, M>> fn,
                                                  MonadRec<B, M> acc) {
        return foldCut((b, a) -> fn.apply(b, a).fmap(RecursiveResult::recurse), acc);
    }

    /**
     * Monolithically fold the spine of this {@link IterateT} (with the possibility of early termination) by
     * {@link MonadRec#trampolineM(Fn1) trampolining} the underlying effects (for iterative folding, use
     * {@link IterateT#trampolineM(Fn1) trampolineM} directly).
     *
     * @param fn   the folding function
     * @param acc  the starting accumulation effect
     * @param <B>  the accumulation type
     * @param <MB> the witnessed target result type
     * @return the folded effect result
     */
    public <B, MB extends MonadRec<B, M>> MB foldCut(
            Fn2<? super B, ? super A, ? extends MonadRec<RecursiveResult<B, B>, M>> fn,
            MonadRec<B, M> acc) {
        return acc.fmap(tupler(this))
                .trampolineM(into((as, b) -> maybeT(as.runIterateT())
                        .flatMap(into((a, aas) -> maybeT(fn.apply(b, a).fmap(Maybe::just)).fmap(tupler(aas))))
                        .runMaybeT()
                        .fmap(maybeR -> maybeR.match(
                                __ -> terminate(b),
                                into((rest, rr) -> rr.biMapL(tupler(rest)))))))
                .coerce();
    }

    /**
     * Convenience method for {@link IterateT#fold(Fn2, MonadRec) folding} the spine of this {@link IterateT} with
     * an action to perform on each element without accumulating any results.
     *
     * @param fn   the action to perform on each element
     * @param <MU> the witnessed target result type
     * @return the folded effect result
     */
    public <MU extends MonadRec<Unit, M>> MU forEach(Fn1<? super A, ? extends MonadRec<Unit, M>> fn) {
        return fold((__, a) -> fn.apply(a), runIterateT().pure(UNIT));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B, N extends MonadRec<?, N>> IterateT<N, B> lift(MonadRec<B, N> nb) {
        return singleton(nb);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("RedundantTypeArguments")
    public <B> IterateT<M, B> trampolineM(
            Fn1<? super A, ? extends MonadRec<RecursiveResult<A, B>, IterateT<M, ?>>> fn) {
        return $(withSelf(
                (self, queued) -> suspended(
                        () -> pureM.<IterateT<M, RecursiveResult<A, B>>, MonadRec<IterateT<M, RecursiveResult<A, B>>, M>>apply(queued)
                                .trampolineM(q -> q.runIterateT().<RecursiveResult<IterateT<M, RecursiveResult<A, B>>, Maybe<Tuple2<B, IterateT<M, B>>>>>fmap(m -> m.match(
                                        __ -> terminate(nothing()),
                                        into((rr, tail) -> rr.biMap(
                                                a -> fn.apply(a).<IterateT<M, RecursiveResult<A, B>>>coerce().concat(tail),
                                                b -> just(tuple(b, self.apply(tail)))))))),
                        pureM)),
                 flatMap(fn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IterateT<M, B> flatMap(Fn1<? super A, ? extends Monad<B, IterateT<M, ?>>> f) {
        return suspended(() -> maybeT(runIterateT())
                                 .trampolineM(into((a, as) -> maybeT(
                                         f.apply(a).<IterateT<M, B>>coerce().runIterateT()
                                                 .flatMap(maybePair -> maybePair.match(
                                                         fn0(() -> as.runIterateT()
                                                                 .fmap(maybeResult -> maybeResult.fmap(RecursiveResult::recurse))),
                                                         t -> pureM.apply(just(terminate(t.fmap(mb -> mb.concat(as.flatMap(f))))))
                                                 )))))
                                 .runMaybeT(),
                         pureM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IterateT<M, B> fmap(Fn1<? super A, ? extends B> fn) {
        return MonadT.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IterateT<M, B> pure(B b) {
        return singleton(pureM.<B, MonadRec<B, M>>apply(b));
    }

    /**
     * Force the underlying spine of this {@link IterateT} into a {@link Collection} of type <code>C</code> inside the
     * context of the monadic effect, using the provided <code>cFn0</code> to construct the initial instance.
     * <p>
     * Note that this is a fundamentally monolithic operation - meaning that incremental progress is not possible - and
     * as such, calling this on an infinite {@link IterateT} will result in either heap exhaustion (e.g. in the case of
     * {@link List lists}) or non-termination (e.g. in the case of {@link Set sets}).
     *
     * @param cFn0  the {@link Collection} construction function
     * @param <C>   the {@link Collection} type
     * @param <MAS> the witnessed target type
     * @return the {@link List} inside of the effect
     */
    public <C extends Collection<A>, MAS extends MonadRec<C, M>> MAS toCollection(Fn0<C> cFn0) {
        MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M> mmta = runIterateT();
        return fold((c, a) -> {
            c.add(a);
            return mmta.pure(c);
        }, mmta.pure(cFn0.apply()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IterateT<M, B> zip(Applicative<Fn1<? super A, ? extends B>, IterateT<M, ?>> appFn) {
        return suspended(() -> {
            MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M> mmta = runIterateT();
            return join(maybeT(mmta).zip(
                    maybeT(appFn.<IterateT<M, Fn1<? super A, ? extends B>>>coerce().runIterateT())
                            .fmap(into((f, fs) -> into((a, as) -> maybeT(
                                    as.<B>fmap(f)
                                            .cons(mmta.pure(f.apply(a)))
                                            .concat(as.cons(mmta.pure(a)).zip(fs))
                                            .runIterateT()))))))
                    .runMaybeT();
        }, pureM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<IterateT<M, B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, IterateT<M, ?>>> lazyAppFn) {
        return lazyAppFn.fmap(this::zip);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IterateT<M, B> discardL(Applicative<B, IterateT<M, ?>> appB) {
        return MonadT.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IterateT<M, A> discardR(Applicative<B, IterateT<M, ?>> appB) {
        return MonadT.super.discardR(appB).coerce();
    }

    /**
     * Static factory method for creating an empty {@link IterateT}.
     *
     * @param pureM the {@link Pure} method for the effect
     * @param <M>   the effect type
     * @param <A>   the element type
     * @return the empty {@link IterateT}
     */
    public static <M extends MonadRec<?, M>, A> IterateT<M, A> empty(Pure<M> pureM) {
        return new IterateT<>(pureM, ImmutableQueue.empty());
    }

    /**
     * Static factory method for creating an {@link IterateT} from a single element.
     *
     * @param ma  the element
     * @param <M> the effect type
     * @param <A> the element type
     * @return the singleton {@link IterateT}
     */
    public static <M extends MonadRec<?, M>, A> IterateT<M, A> singleton(MonadRec<A, M> ma) {
        return IterateT.<M, A>empty(Pure.of(ma)).cons(ma);
    }

    /**
     * Static factory method for wrapping an uncons of an {@link IterateT} in an {@link IterateT}.
     *
     * @param unwrapped the uncons
     * @param <M>       the effect type
     * @param <A>       the element type
     * @return the wrapped {@link IterateT}
     */
    public static <M extends MonadRec<?, M>, A> IterateT<M, A> iterateT(
            MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M> unwrapped) {
        return suspended(() -> unwrapped, Pure.of(unwrapped));
    }

    /**
     * Static factory method for creating an {@link IterateT} from a spine represented by one or more elements.
     *
     * @param ma  the head element
     * @param mas the tail elements
     * @param <M> the effect type
     * @param <A> the element type
     * @return the {@link IterateT}
     */
    @SafeVarargs
    public static <M extends MonadRec<?, M>, A> IterateT<M, A> of(
            MonadRec<A, M> ma, MonadRec<A, M>... mas) {
        @SuppressWarnings("varargs")
        List<MonadRec<A, M>> as = asList(mas);
        return foldLeft(IterateT::snoc, singleton(ma), as);
    }

    /**
     * Lazily unfold an {@link IterateT} from an unfolding function <code>fn</code> and a starting seed value
     * <code>mb</code> by successively applying <code>fn</code> to the latest seed value, producing {@link Maybe maybe}
     * a value to yield out and the next seed value for the subsequent computation.
     *
     * @param fn  the unfolding function
     * @param mb  the starting seed value
     * @param <M> the effect type
     * @param <A> the element type
     * @param <B> the seed type
     * @return the lazily unfolding {@link IterateT}
     */
    public static <M extends MonadRec<?, M>, A, B> IterateT<M, A> unfold(
            Fn1<? super B, ? extends MonadRec<Maybe<Tuple2<A, B>>, M>> fn, MonadRec<B, M> mb) {
        Pure<M> pureM = Pure.of(mb);
        return $(withSelf((self, mmb) -> suspended(() -> maybeT(mmb.flatMap(fn))
                .fmap(ab -> ab.<IterateT<M, A>>fmap(b -> self.apply(pureM.apply(b))))
                .runMaybeT(), pureM)), mb);
    }

    /**
     * Create an {@link IterateT} from a suspended computation that yields the spine of the {@link IterateT} inside the
     * effect.
     *
     * @param thunk the suspended computation
     * @param pureM the {@link Pure} method for the effect
     * @param <M>   the effect type
     * @param <A>   the element type
     * @return the {@link IterateT}
     */
    public static <M extends MonadRec<?, M>, A> IterateT<M, A> suspended(
            Fn0<MonadRec<Maybe<Tuple2<A, IterateT<M, A>>>, M>> thunk, Pure<M> pureM) {
        return new IterateT<>(pureM, ImmutableQueue.singleton(a(thunk)));
    }

    /**
     * Lazily unfold an {@link IterateT} from an {@link Iterator} inside {@link IO}.
     *
     * @param as  the {@link Iterator}
     * @param <A> the element type
     * @return the {@link IterateT}
     */
    public static <A> IterateT<IO<?>, A> fromIterator(Iterator<A> as) {
        return unfold(it -> io(() -> {
            if (as.hasNext())
                return just(tuple(as.next(), as));
            return nothing();
        }), io(() -> as));
    }

    /**
     * The canonical {@link Pure} instance for {@link IterateT}.
     *
     * @param pureM the argument {@link Monad} {@link Pure}
     * @param <M>   the argument {@link Monad} witness
     * @return the {@link Pure} instance
     */
    public static <M extends MonadRec<?, M>> Pure<IterateT<M, ?>> pureIterateT(Pure<M> pureM) {
        return new Pure<IterateT<M, ?>>() {
            @Override
            public <A> IterateT<M, A> checkedApply(A a) {
                return liftIterateT().apply(pureM.<A, MonadRec<A, M>>apply(a));
            }
        };
    }

    /**
     * {@link Lift} for {@link IterateT}.
     *
     * @return the {@link Monad} lifted into {@link IterateT}
     */
    public static Lift<IterateT<?, ?>> liftIterateT() {
        return IterateT::singleton;
    }
}
