package com.jnape.palatable.lambda.io;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;

import static com.jnape.palatable.lambda.adt.Maybe.maybe;
import static com.jnape.palatable.lambda.adt.Try.trying;
import static com.jnape.palatable.lambda.functions.Fn0.fn0;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.io.IO.io;
import static com.jnape.palatable.lambda.io.IO.throwing;

/**
 * A mutable single element memory cell with blocking operations {@link MVar#take()} and {@link MVar#put(A)};
 *
 * @param <A> The type of object the MVar contains
 */
public final class MVar<A> {

    private final ArrayBlockingQueue<A> queue;

    private MVar() {
        queue = new ArrayBlockingQueue<>(1, true);
    }

    private MVar(A a) {
        queue = new ArrayBlockingQueue<>(1, true, Collections.singletonList(a));
    }

    /**
     * Construct a new empty {@link MVar}
     *
     * @param <A> The {@link MVar} parameter type
     * @return An {@link IO} that produces the empty {@link MVar}
     */
    public static <A> IO<MVar<A>> newMVar() {
        return io(fn0(MVar::new));
    }

    /**
     * Construct a new occupied {@link MVar}
     *
     * @param a   The initial value
     * @param <A> The {@link MVar} parameter type
     * @return An {@link IO} that produces the occupied {@link MVar}
     */
    public static <A> IO<MVar<A>> newMVar(A a) {
        return io(() -> new MVar<>(a));
    }

    /**
     * Blocking retrieval of the value inside. If the {@link MVar} is currently empty the returned {@link IO} will block until it becomes occupied.
     *
     * @return An {@link IO} which will block and return a value from inside the {@link MVar}
     */
    public IO<A> take() {
        return io(queue::take);
    }

    /**
     * Blocking placement of the value inside. If the {@link MVar} is currently occupied the returned {@link IO} will block until it becomes empty.
     *
     * @return An {@link IO} which will block and return the {@link MVar} after mutation.
     */
    public IO<MVar<A>> put(A a) {
        return io(() -> {
            queue.put(a);
            return this;
        });
    }

    /**
     * Read the contents inside without emptying.
     * <p>
     * Note that this operation is not atomic and could block on put if there are multiple producers.
     *
     * @return An {@link IO} with the value inside of the {@link MVar}
     */
    public IO<A> read() {
        return take()
                .flatMap(a -> put(a)
                        .fmap(constantly(a)));
    }

    /**
     * Swap the value inside the {@link MVar} with another value and return the original.
     * <p>
     * Note that this operation is not atomic in the presence of multiple producers
     *
     * @param a The new value to be inside
     * @return An {@link IO} that produces the original value
     */
    public IO<A> swap(A a) {
        return take()
                .flatMap(a1 -> put(a)
                        .fmap(constantly(a1)));
    }

    /**
     * A non-blocking version of {@link MVar#take()} that returns {@link Maybe#nothing()} if empty.
     *
     * @return An {@link IO} that produces a {@link Maybe} of the contents inside
     */
    public IO<Maybe<A>> poll() {
        return io(() -> maybe(queue.poll()));
    }

    /**
     * A non-blocking version of {@link MVar#put(A)}.
     *
     * @return An {@link IO} that adds the value to the {@link MVar} and produces true if added and false if not
     */
    public IO<Boolean> add(A a) {
        return io(() -> trying(() -> queue.add(a))
                .catching(IllegalStateException.class, constantly(false))
                .orThrow());
    }

    /**
     * Modifies the contents inside the {@link MVar} while assuring it remains unaltered
     * if the provided {@link IO} throws
     * <p>
     * Note that this operation is not atomic in the presence of multiple producers
     *
     * @param fn The function that returns the new value to be contained
     * @return AN {@link IO} that produces the modified {@link MVar}
     */
    public IO<MVar<A>> modify(Fn1<A, IO<A>> fn) {
        return take()
                .flatMap(a -> fn.apply(a)
                        .flatMap(this::put)
                        .catchError(t -> put(a)));
    }

    /**
     * Transform the value from the {@link MVar} while placing it back if the given {@link IO} throws
     * <p>
     * Note that this operation is not atomic in the presence of multiple producers
     *
     * @param fn  The function resulting in the transformed value which may throw inside the {@link IO}
     * @param <B> The type of the resulting value
     * @return An {@link IO} of the resulting value
     */
    public <B> IO<B> with(Fn1<A, IO<B>> fn) {
        return take()
                .flatMap(a -> fn.apply(a)
                        .flatMap(b -> put(a).fmap(constantly(b)))
                        .catchError(t -> put(a).flatMap(constantly(throwing(t)))));
    }

    /**
     * Check whether the {@link MVar} is unoccupied
     *
     * @return An {@link IO} that produces whether or not the {@link MVar} is empty
     */
    public IO<Boolean> empty() {
        return io(queue::isEmpty);
    }
}
