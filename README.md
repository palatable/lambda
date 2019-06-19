λ
======
[![Build Status](https://travis-ci.com/palatable/lambda.svg?branch=master)](https://travis-ci.com/palatable/lambda)
[![Lambda](https://img.shields.io/maven-central/v/com.jnape.palatable/lambda.svg)](http://search.maven.org/#search%7Cga%7C1%7Ccom.jnape.palatable.lambda)
[![Join the chat at https://gitter.im/palatable/lambda](https://badges.gitter.im/palatable/lambda.svg)](https://gitter.im/palatable/lambda?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Functional patterns for Java

#### Table of Contents

 - [Background](#background)
 - [Installation](#installation)
 - [Examples](#examples)
 - [Semigroups](#semigroups)
 - [Monoids](#monoids)
 - [Functors](#functors)
   - [Bifunctors](#bifunctors)
   - [Profunctors](#profunctors)
   - [Applicatives](#applicatives)
   - [Monads](#monads)
   - [Traversables](#traversables)
 - [ADTs](#adts)
   - [Maybe](#maybe)
   - [HList](#hlist)
     - [Tuples](#tuples)
   - [HMaps](#hmaps)
   - [CoProducts](#coproducts)
     - [Either](#either)
 - [Lenses](#lenses)
 - [Notes](#notes) 
 - [License](#license)

<a name="background">Background</a>
----------

Lambda was born out of a desire to use some of the same canonical functions (e.g. `unfoldr`, `takeWhile`, `zipWith`) and functional patterns (e.g. `Functor` and friends) that are idiomatic in other languages and make them available for Java.

Some things a user of lambda most likely values:

- Lazy evaluation
- Immutability by design
- Composition
- Higher-level abstractions
- Parametric polymorphism

Generally, everything that lambda produces is lazily-evaluated (except for terminal operations like `reduce`), immutable (except for `Iterator`s, since it's effectively impossible), composable (even between different arities, where possible), foundational (maximally contravariant), and parametrically type-checked (even where this adds unnecessary constraints due to a lack of higher-kinded types).

Although the library is currently (very) small, these values should always be the driving forces behind future growth.

<a name="installation">Installation</a>
------------

Add the following dependency to your:

`pom.xml` ([Maven](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)):

```xml
<dependency>
    <groupId>com.jnape.palatable</groupId>
    <artifactId>lambda</artifactId>
    <version>4.0.0</version>
</dependency>
```

`build.gradle` ([Gradle](https://docs.gradle.org/current/userguide/dependency_management.html)):

```gradle
compile group: 'com.jnape.palatable', name: 'lambda', version: '4.0.0'
```

<a name="examples">Examples</a>
--------

First, the obligatory `map`/`filter`/`reduce` example:
```Java
Integer sumOfEvenIncrements =
          reduceLeft((x, y) -> x + y,
              filter(x -> x % 2 == 0,
                  map(x -> x + 1, asList(1, 2, 3, 4, 5))));
//-> 12
```

Every function in lambda is [curried](https://www.wikiwand.com/en/Currying), so we could have also done this:
```Java
Fn1<Iterable<Integer>, Integer> sumOfEvenIncrementsFn =
          map((Integer x) -> x + 1)
          .andThen(filter(x -> x % 2 == 0))
          .andThen(reduceLeft((x, y) -> x + y));

Integer sumOfEvenIncrements = sumOfEvenIncrementsFn.apply(asList(1, 2, 3, 4, 5));
//-> 12
```

How about the positive squares below 100:

```Java
Iterable<Integer> positiveSquaresBelow100 =
          takeWhile(x -> x < 100, map(x -> x * x, iterate(x -> x + 1, 1)));
//-> [1, 4, 9, 16, 25, 36, 49, 64, 81]
```

We could have also used `unfoldr`:

```Java
Iterable<Integer> positiveSquaresBelow100 = unfoldr(x -> {
              int square = x * x;
              return square < 100 ? Optional.of(tuple(square, x + 1)) : Optional.empty();
          }, 1);
//-> [1, 4, 9, 16, 25, 36, 49, 64, 81]
```

What if we want the cross product of a domain and codomain:

```Java
Iterable<Tuple2<Integer, String>> crossProduct =
          take(10, cartesianProduct(asList(1, 2, 3), asList("a", "b", "c")));
//-> (1,"a"), (1,"b"), (1,"c"), (2,"a"), (2,"b"), (2,"c"), (3,"a"), (3,"b"), (3,"c")
```

Let's compose two functions:

```Java
Fn1<Integer, Integer> add = x -> x + 1;
Fn1<Integer, Integer> subtract = x -> x -1;

Fn1<Integer, Integer> noOp = add.andThen(subtract);
// same as
Fn1<Integer, Integer> alsoNoOp = subtract.compose(add);
```

And partially apply some:

```Java
Fn2<Integer, Integer, Integer> add = (x, y) -> x + y;

Fn1<Integer, Integer> add1 = add.apply(1);
add1.apply(2);
//-> 3
```

And have fun with 3s:

```Java
Iterable<Iterable<Integer>> multiplesOf3InGroupsOf3 =
          take(3, inGroupsOf(3, unfoldr(x -> Optional.of(tuple(x * 3, x + 1)), 1)));
//-> [[3, 6, 9], [12, 15, 18], [21, 24, 27]]
```

Check out the [tests](https://github.com/palatable/lambda/tree/master/src/test/java/com/jnape/palatable/lambda/functions/builtin) or [javadoc](http://palatable.github.io/lambda/javadoc/) for more examples.

<a name="semigroups">Semigroups</a>
----

[Semigroups](https://en.wikipedia.org/wiki/Semigroup) are supported via `Semigroup<A>`, a subtype of `Fn2<A,A,A>`, and add left and right folds over an `Iterable<A>`.

```Java
Semigroup<Integer> add = (augend, addend) -> augend + addend;
add.apply(1, 2); //-> 3
add.foldLeft(0, asList(1, 2, 3)); //-> 6
```

Lambda ships some default logical semigroups for lambda types and core JDK types. Common examples are:

- `AddAll` for concatenating two `Collection`s
- `Collapse` for collapsing two `Tuple2`s together 
- `Merge` for merging two `Either`s using left-biasing semantics

Check out the [semigroup](https://palatable.github.io/lambda/javadoc/com/jnape/palatable/lambda/semigroup/builtin/package-summary.html) package for more examples.

<a name="monoids">Monoids</a>
----

[Monoids](https://en.wikipedia.org/wiki/Monoid) are supported via `Monoid<A>`, a subtype of `Semigroup<A>` with an `A #identity()` method, and add left and right reduces over an `Iterable<A>`, as well as `foldMap`.

```Java
Monoid<Integer> multiply = monoid((x, y) -> x * y, 1);
multiple.reduceLeft(emptyList()); //-> 1
multiply.reduceLeft(asList(1, 2, 3)); //-> 6
multiply.foldMap(Integer::parseInt, asList("1", "2", "3")); //-> also 6
```

Some commonly used lambda monoid implementations include:

- `Present` for merging together two `Optional`s
- `Join` for joining two `String`s
- `And` for logical conjunction of two `Boolean`s
- `Or` for logical disjunction of two `Boolean`s

Additionally, instances of `Monoid<A>` can be trivially synthesized from instances of `Semigroup<A>` via the `Monoid#monoid` static factory method, taking the `Semigroup` and the identity element `A` or a supplier of the identity element `Supplier<A>`. 

Check out the [monoid](https://palatable.github.io/lambda/javadoc/com/jnape/palatable/lambda/monoid/builtin/package-summary.html) package for more examples.

<a name="functors">Functors</a>
----

Functors are implemented via the `Functor` interface, and are sub-typed by every function type that lambda exports, as well as many of the [ADTs](#adts). 

```Java
public final class Slot<A> implements Functor<A, Slot> {
    private final A a;

    public Slot(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }

    @Override
    public <B> Slot<B> fmap(Function<? super A, ? extends B> fn) {
        return new Slot<>(fn.apply(a));
    }
}

Slot<Integer> intSlot = new Slot<>(1);
Slot<String> stringSlot = intSlot.fmap(x -> "number: " + x);
stringSlot.getA(); //-> "number: 1"
```

Examples of functors include:
  
- `Fn*`, `Semigroup`, and `Monoid` 
- `SingletonHList` and `Tuple*`
- `Choice*`
- `Either`
- `Const`, `Identity`, and `Compose`
- `Lens`

Implementing `Functor` is as simple as providing a definition for the covariant mapping function `#fmap` (ideally satisfying the [two laws](https://hackage.haskell.org/package/base-4.9.1.0/docs/Data-Functor.html)). 

### <a name="bifunctors">Bifunctors</a>

Bifunctors -- functors that support two parameters that can be covariantly mapped over -- are implemented via the `Bifunctor` interface.

```Java
public final class Pair<A, B> implements Bifunctor<A, B, Pair> {
    private final A a;
    private final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    @Override
    public <C, D> Pair<C, D> biMap(Function<? super A, ? extends C> lFn,
                                   Function<? super B, ? extends D> rFn) {
        return new Pair<>(lFn.apply(a), rFn.apply(b));
    }
}

Pair<String,Integer> stringIntPair = new Pair<>("str", 1);
Pair<Integer, Boolean> intBooleanPair = stringIntPair.biMap(String::length, x -> x % 2 == 0);
intBooleanPair.getA(); //-> 3
intBooleanPair.getB(); //-> false
```

Examples of bifunctors include:

- `Tuple*`
- `Choice*`
- `Either`
- `Const`

Implementing `Bifunctor` requires implementing *either* `biMapL` and `biMapR` *or* `biMap`. As with `Functor`, there are a [few laws](https://hackage.haskell.org/package/base-4.9.1.0/docs/Data-Bifunctor.html) that well-behaved instances of `Bifunctor` should adhere to.

### <a name="profunctors">Profunctors</a>

Profunctors -- functors that support one parameter that can be mapped over contravariantly, and a second parameter that can be mapped over covariantly -- are implemented via the `Profunctor` interface.

```Java
Fn1<Integer, Integer> add2 = (x) -> x + 2;
add2.<String, String>diMap(Integer::parseInt, Object::toString).apply("1"); //-> "3"
```

Examples of profunctors include:

- `Fn*`
- `Lens`

Implementing `Profunctor` requires implementing *either* `diMapL` and `diMapR` *or* `diMap`. As with `Functor` and `Bifunctor`, there are [some laws](https://hackage.haskell.org/package/profunctors-5.2/docs/Data-Profunctor.html) that well behaved instances of `Profunctor` should adhere to.

### <a name="applicatives">Applicatives</a>

Applicative functors -- functors that can be applied together with a 2-arity or higher function -- are implemented via the `Applicative` interface.

```Java
public final class Slot<A> implements Applicative<A, Slot> {
    private final A a;

    public Slot(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }

    @Override
    public <B> Slot<B> fmap(Function<? super A, ? extends B> fn) {
        return pure(fn.apply(a));
    }

    @Override
    public <B> Slot<B> pure(B b) {
        return new Slot<>(b);
    }

    @Override
    public <B> Slot<B> zip(Applicative<Function<? super A, ? extends B>, Slot> appFn) {
        return pure(appFn.<Slot<Function<? super A, ? extends B>>>coerce().getA().apply(getA()));
    }
}

Fn2<Integer, Integer, Integer> add = (x, y) -> x + y;
Slot<Integer> x = new Slot<>(1);
Slot<Integer> y = new Slot<>(2);
Slot<Integer> z = y.zip(x.fmap(add)); //-> Slot{a=3}
```

Examples of applicative functors include:

- `Fn*`, `Semigroup`, and `Monoid`
- `SingletonHList` and `Tuple*`
- `Choice*`
- `Either`
- `Const`, `Identity`, and `Compose`
- `Lens`

In addition to implementing `fmap` from `Functor`, implementing an applicative functor involves providing two methods: `pure`, a method that lifts a value into the functor; and `zip`, a method that applies a lifted function to a lifted value, returning a new lifted value. As usual, there are [some laws](https://hackage.haskell.org/package/base-4.9.1.0/docs/Control-Applicative.html) that should be adhered to. 

### <a name="monads">Monads</a>

Monads are applicative functors that additionally support a chaining operation, `flatMap :: (a -> f b) -> f a -> f b`: a function from the functor's parameter to a new instance of the same functor over a potentially different parameter. Because the function passed to `flatMap` can return a different instance of the same functor, functors can take advantage of multiple constructions that yield different functorial operations, like short-circuiting, as in the following example using `Either`:

```Java
class Person {
    Optional<Occupation> occupation() {
        return Optional.empty();
    } 
}

class Occupation {
}

public static void main(String[] args) {
    Fn1<String, Either<String, Integer>> parseId = str -> Either.trying(() -> Integer.parseInt(str), __ -> str + " is not a valid id"); 

    Map<Integer, Person> database = new HashMap<>();
    Fn1<Integer, Either<String, Person>> lookupById = id -> Either.fromOptional(Optional.ofNullable(database.get(id)),
                                                                                () -> "No person found for id " + id);
    Fn1<Person, Either<String, Occupation>> getOccupation = p -> Either.fromOptional(p.occupation(), () -> "Person was unemployed");

    Either<String, Occupation> occupationOrError = 
        parseId.apply("12") // Either<String, Integer>
            .flatMap(lookupById) // Either<String, Person>
            .flatMap(getOccupation); // Either<String, Occupation>
}
```

In the previous example, if any of `parseId`, `lookupById`, or `getOccupation` fail, no further `flatMap` computations can succeed, so the result short-circuits to the first `left` value that is returned. This is completely predictable from the type signature of `Monad` and `Either`: `Either<L, R>` is a `Monad<R>`, so the single arity `flatMap` can have nothing to map in the case where there is no `R` value. With experience, it generally becomes quickly clear what the logical behavior of `flatMap` *must* be given the type signatures.

That's it. Monads are neither [elephants](http://james-iry.blogspot.com/2007/09/monads-are-elephants-part-1.html) nor are they [burritos](https://blog.plover.com/prog/burritos.html); they're simply types that support a) the ability to lift a value into them, and b) a chaining function `flatMap :: (a -> f b) -> f a -> f b` that can potentially return different instances of the same monad. If a type can do those two things (and obeys [the laws](https://wiki.haskell.org/Monad_laws)), it is a monad.

Further, if a type is a monad, it is necessarily an `Applicative`, which makes it necessarily a `Functor`, so *lambda* enforces this tautology via a hierarchical constraint.

### <a name="traversables">Traversables</a>

Traversable functors -- functors that can be "traversed from left to right" -- are implemented via the `Traversable` interface.

```Java
public abstract class Maybe<A> implements Traversable<A, Maybe> {
    private Maybe() {
    }

    @Override
    public abstract <B, App extends Applicative> Applicative<Maybe<B>, App> traverse(
            Function<? super A, ? extends Applicative<B, App>> fn,
            Function<? super Traversable<B, Maybe>, ? extends Applicative<? extends Traversable<B, Maybe>, App>> pure);

    @Override
    public abstract <B> Maybe<B> fmap(Function<? super A, ? extends B> fn);

    private static final class Just<A> extends Maybe<A> {
        private final A a;

        private Just(A a) {
            this.a = a;
        }

        @Override
        public <B, App extends Applicative> Applicative<Maybe<B>, App> traverse(
                Function<? super A, ? extends Applicative<B, App>> fn,
                Function<? super Traversable<B, Maybe>, ? extends Applicative<? extends Traversable<B, Maybe>, App>> pure) {
            return fn.apply(a).fmap(Just::new);
        }

        @Override
        public <B> Maybe<B> fmap(Function<? super A, ? extends B> fn) {
            return new Just<>(fn.apply(a));
        }
    }

    private static final class Nothing<A> extends Maybe<A> {
        @Override
        @SuppressWarnings("unchecked")
        public <B, App extends Applicative> Applicative<Maybe<B>, App> traverse(
                Function<? super A, ? extends Applicative<B, App>> fn,
                Function<? super Traversable<B, Maybe>, ? extends Applicative<? extends Traversable<B, Maybe>, App>> pure) {
            return pure.apply((Maybe<B>) this).fmap(x -> (Maybe<B>) x);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <B> Maybe<B> fmap(Function<? super A, ? extends B> fn) {
            return (Maybe<B>) this;
        }
    }
}

Maybe<Integer> just1 = Maybe.just(1);
Maybe<Integer> nothing = Maybe.nothing();

Either<String, Maybe<Integer>> traversedJust = just1.traverse(x -> right(x + 1), empty -> left("empty"))
        .fmap(x -> (Maybe<Integer>) x)
        .coerce(); //-> Right(Just(2))

Either<String, Maybe<Integer>> traversedNothing = nothing.traverse(x -> right(x + 1), empty -> left("empty"))
        .fmap(x -> (Maybe<Integer>) x)
        .coerce(); //-> Left("empty")
```

Examples of traversable functors include:

- `SingletonHList` and `Tuple*`
- `Choice*`
- `Either`
- `Const` and `Identity`
- `LambdaIterable` for wrapping `Iterable` in an instance of `Traversable`

In addition to implementing `fmap` from `Functor`, implementing a traversable functor involves providing an implementation of `traverse`.

As always, there are [some laws](https://hackage.haskell.org/package/base-4.9.1.0/docs/Data-Traversable.html) that should be observed.

<a name="adts">ADTs</a>
----

Lambda also supports a few first-class [algebraic data types](https://www.wikiwand.com/en/Algebraic_data_type).

### <a name="maybe">Maybe</a>

`Maybe` is the _lambda_ analog to `java.util.Optional`. It behaves in much of the same way as `j.u.Optional`, except that it quite intentionally does not support the inherently unsafe `j.u.Optional#get`.

```Java
Maybe<Integer> maybeInt = Maybe.just(1); // Just 1
Maybe<String> maybeString = Maybe.nothing(); // Nothing
```

Also, because it's a _lambda_ type, it takes advantage of the full functor hierarchy, as well as some helpful conversion functions:

```Java
Maybe<String> just = Maybe.maybe("string"); // Just "string"
Maybe<String> nothing = Maybe.maybe(null); // Nothing

Maybe<Integer> maybeX = Maybe.just(1);
Maybe<Integer> maybeY = Maybe.just(2);

maybeY.zip(maybeX.fmap(x -> y -> x + y)); // Just 3
maybeY.zip(nothing()); // Nothing
Maybe.<Integer>nothing().zip(maybeX.fmap(x -> y -> x + y)); // Nothing

Either<String, Integer> right = maybeX.toEither(() -> "was empty"); // Right 1
Either<String, Integer> left = Maybe.<Integer>nothing().toEither(() -> "was empty"); // Left "was empty"

Maybe.fromEither(right); // Just 1
Maybe.fromEither(left); // Nothing
```

Finally, for compatibility purposes, `Maybe` and `j.u.Optional` can be trivially converted back and forth:

```Java
Maybe<Integer> just1 = Maybe.just(1); // Just 1
Optional<Integer> present1 = just1.toOptional(); // Optional.of(1)

Optional<String> empty = Optional.empty(); // Optional.empty()
Maybe<String> nothing = Maybe.fromOptional(empty); // Nothing
```

***Note***: One compatibility difference between `j.u.Optional` and `Maybe` is how `map`/`fmap` behave regarding functions that return `null`: `j.u.Optional` re-wraps `null` results from `map` operations in another `j.u.Optional`, whereas `Maybe` considers this to be an error, and throws an exception. The reason `Maybe` throws in this case is because `fmap` is not an operation to be called speculatively, and so any function that returns `null` in the context of an `fmap` operation is considered to be erroneous. Instead of calling `fmap` with a function that might return `null`, the function result should be wrapped in a `Maybe` and `flatMap` should be used, as illustrated in the following example: 

```Java
Function<Integer, Object> nullResultFn = __ -> null;

Optional.of(1).map(nullResultFn); // Optional.empty()
Maybe.just(1).fmap(nullResultFn); // throws NullPointerException

Maybe.just(1).flatMap(nullResultFn.andThen(Maybe::maybe)); // Nothing
```

### <a name="hlists">Heterogeneous Lists (HLists)</a>

HLists are type-safe heterogeneous lists, meaning they can store elements of different types in the same list while facilitating certain type-safe interactions.

The following illustrates how the linear expansion of the recursive type signature for `HList` prevents ill-typed expressions:

```Java
HCons<Integer, HCons<String, HNil>> hList = HList.cons(1, HList.cons("foo", HList.nil()));

System.out.println(hList.head()); // prints 1
System.out.println(hList.tail().head()); // prints "foo"

HNil nil = hList.tail().tail();
//nil.head() won't type-check
```

#### <a name="tuples">Tuples</a>

One of the primary downsides to using `HList`s in Java is how quickly the type signature grows.

To address this, tuples in lambda are specializations of `HList`s up to 8 elements deep, with added support for index-based accessor methods.

```Java
HNil nil = HList.nil();
SingletonHList<Integer> singleton = nil.cons(8);
Tuple2<Integer, Integer> tuple2 = singleton.cons(7);
Tuple3<Integer, Integer, Integer> tuple3 = tuple2.cons(6);
Tuple4<Integer, Integer, Integer, Integer> tuple4 = tuple3.cons(5);
Tuple5<Integer, Integer, Integer, Integer, Integer> tuple5 = tuple4.cons(4);
Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> tuple6 = tuple5.cons(3);
Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple7 = tuple6.cons(2);
Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple8 = tuple7.cons(1);

System.out.println(tuple2._1()); // prints 7
System.out.println(tuple8._8()); // prints 8
```

Additionally, `HList` provides convenience static factory methods for directly constructing lists of up to 8 elements:

```Java
SingletonHList<Integer> singleton = HList.singletonHList(1);
Tuple2<Integer, Integer> tuple2 = HList.tuple(1, 2);
Tuple3<Integer, Integer, Integer> tuple3 = HList.tuple(1, 2, 3);
Tuple4<Integer, Integer, Integer, Integer> tuple4 = HList.tuple(1, 2, 3, 4);
Tuple5<Integer, Integer, Integer, Integer, Integer> tuple5 = HList.tuple(1, 2, 3, 4, 5);
Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> tuple6 = HList.tuple(1, 2, 3, 4, 5, 6);
Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple7 = HList.tuple(1, 2, 3, 4, 5, 6, 7);
Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple8 = HList.tuple(1, 2, 3, 4, 5, 6, 7, 8);
```

`Index` can be used for type-safe retrieval and updating of elements at specific indexes:

```Java
HCons<Integer, HCons<String, HCons<Character, HNil>>> hList = cons(1, cons("2", cons('3', nil())));
HCons<Integer, Tuple2<String, Character>> tuple = tuple(1, "2", '3');
Tuple5<Integer, String, Character, Double, Boolean> longerHList = tuple(1, "2", '3', 4.0d, false);

Index<Character, HCons<Integer, ? extends HCons<String, ? extends HCons<Character, ?>>>> characterIndex =
        Index.<Character>index().<String>after().after();

characterIndex.get(hList); // '3'
characterIndex.get(tuple); // '3'
characterIndex.get(longerHList); // '3'

characterIndex.set('4', hList); // HList{ 1 :: "2" :: '4' }
```

Finally, all `Tuple*` classes are instances of both `Functor` and `Bifunctor`:

```Java
Tuple2<Integer, String> mappedTuple2 = tuple(1, 2).biMap(x -> x + 1, Object::toString);

System.out.println(mappedTuple2._1()); // prints 2
System.out.println(mappedTuple2._2()); // prints "2"

Tuple3<String, Boolean, Integer> mappedTuple3 = tuple("foo", true, 1).biMap(x -> !x, x -> x + 1);

System.out.println(mappedTuple3._1()); // prints "foo"
System.out.println(mappedTuple3._2()); // prints false
System.out.println(mappedTuple3._3()); // prints 2
```

### <a name="hmaps">Heterogeneous Maps</a>

HMaps are type-safe heterogeneous maps, meaning they can store mappings to different value types in the same map; however, whereas HLists encode value types in their type signatures, HMaps rely on the keys to encode the value type that they point to. 

```Java
TypeSafeKey<String> stringKey = TypeSafeKey.typeSafeKey();
TypeSafeKey<Integer> intKey = TypeSafeKey.typeSafeKey();
HMap hmap = HMap.hMap(stringKey, "string value",
                      intKey, 1);

Optional<String> stringValue = hmap.get(stringKey); // Optional["string value"]
Optional<Integer> intValue = hmap.get(intKey); // Optional[1]
Optional<Integer> anotherIntValue = hmap.get(anotherIntKey); // Optional.empty
```

### <a name="coproducts">CoProducts</a>

`CoProduct`s generalize unions of disparate types in a single consolidated type, and the `ChoiceN` ADTs represent canonical implementations of these coproduct types.

```Java
CoProduct3<String, Integer, Character, ?> string = Choice3.a("string");
CoProduct3<String, Integer, Character, ?> integer = Choice3.b(1);
CoProduct3<String, Integer, Character, ?> character = Choice3.c('a');
```

Rather than supporting explicit value unwrapping, which would necessarily jeopardize type safety, `CoProduct`s support a `match` method that takes one function per possible value type and maps it to a final common result type:

```Java
CoProduct3<String, Integer, Character, ?> string = Choice3.a("string");
CoProduct3<String, Integer, Character, ?> integer = Choice3.b(1);
CoProduct3<String, Integer, Character, ?> character = Choice3.c('a');

Integer result = string.<Integer>match(String::length, identity(), Character::charCount); // 6
```

Additionally, because a `CoProduct2<A, B, ?>` guarantees a subset of a `CoProduct3<A, B, C, ?>`, the `diverge` method exists between `CoProduct` types of single magnitude differences to make it easy to use a more convergent `CoProduct` where a more divergent `CoProduct` is expected:

```Java
CoProduct2<String, Integer, ?> coProduct2 = Choice2.a("string");
CoProduct3<String, Integer, Character, ?> coProduct3 = coProduct2.diverge(); // still just the coProduct2 value, adapted to the coProduct3 shape
```

There are `CoProduct` and `Choice` specializations for type unions of up to 8 different types: `CoProduct2` through `CoProduct8`, and `Choice2` through `Choice8`, respectively.

### <a name="either">Either</a>

`Either<L, R>` represents a specialized `CoProduct2<L, R>`, which resolve to one of two possible values: a left value wrapping an `L`, or a right value wrapping an `R` (typically an exceptional value or a successful value, respectively).

As with `CoProduct2`, rather than supporting explicit value unwrapping, `Either` supports many useful comprehensions to help facilitate type-safe interactions:    

```Java
Either<String, Integer> right = Either.right(1);
Either<String, Integer> left = Either.left("Head fell off");

Integer result = right.orElse(-1);
//-> 1

List<Integer> values = left.match(l -> Collections.emptyList(), Collections::singletonList);
//-> [] 
```

Check out the tests for [more examples](https://github.com/palatable/lambda/blob/master/src/test/java/com/jnape/palatable/lambda/adt/EitherTest.java) of ways to interact with `Either`.

<a name="lenses">Lenses</a>
----

Lambda also ships with a first-class <a href="https://www.youtube.com/watch?v=cefnmjtAolY&feature=youtu.be&hd=1">lens</a> type, as well as a small library of useful general lenses:

```Java
Lens<List<String>, List<String>, Optional<String>, String> stringAt0 = ListLens.at(0);

List<String> strings = asList("foo", "bar", "baz");
view(stringAt0, strings); // Optional[foo]
set(stringAt0, "quux", strings); // [quux, bar, baz]
over(stringAt0, s -> s.map(String::toUpperCase).orElse(""), strings); // [FOO, bar, baz]
```

There are three functions that lambda provides that interface directly with lenses: `view`, `over`, and `set`. As the name implies, `view` and `set` are used to retrieve values and store values, respectively, whereas `over` is used to apply a function to the value a lens is focused on, alter it, and store it (you can think of `set` as a specialization of `over` using `constantly`).

Lenses can be easily created. Consider the following `Person` class:

```Java
public final class Person {
    private final int age;

    public Person(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public Person setAge(int age) {
        return new Person(age);
    }

    public Person setAge(LocalDate dob) {
        return setAge((int) YEARS.between(dob, LocalDate.now()));
    }
}
```

...and a lens for getting and setting `age` as an `int`:

```Java
Lens<Person, Person, Integer, Integer> ageLensWithInt = Lens.lens(Person::getAge, Person::setAge);

//or, when each pair of type arguments match...

Lens.Simple<Person, Integer> alsoAgeLensWithInt = Lens.simpleLens(Person::getAge, Person::setAge);
```

If we wanted a lens for the `LocalDate` version of `setAge`, we could use the same method references and only alter the type signature:

```Java
Lens<Person, Person, Integer, LocalDate> ageLensWithLocalDate = Lens.lens(Person::getAge, Person::setAge);
```

Compatible lenses can be trivially composed:

```Java
Lens<List<Integer>, List<Integer>, Optional<Integer>, Integer> at0 = ListLens.at(0);
Lens<Map<String, List<Integer>>, Map<String, List<Integer>>, List<Integer>, List<Integer>> atFoo = MapLens.atKey("foo", emptyList());

view(atFoo.andThen(at0), singletonMap("foo", asList(1, 2, 3))); // Optional[1]
```

Lens provides independent `map` operations for each parameter, so incompatible lenses can also be composed: 

```Java
Lens<List<Integer>, List<Integer>, Optional<Integer>, Integer> at0 = ListLens.at(0);
Lens<Map<String, List<Integer>>, Map<String, List<Integer>>, Optional<List<Integer>>, List<Integer>> atFoo = MapLens.atKey("foo");
Lens<Map<String, List<Integer>>, Map<String, List<Integer>>, Optional<Integer>, Integer> composed =
        atFoo.mapA(optL -> optL.orElse(singletonList(-1)))
                .andThen(at0);

view(composed, singletonMap("foo", emptyList())); // Optional.empty
```

Check out the tests or the [javadoc](http://palatable.github.io/lambda/javadoc/) for more info.

<a name="notes">Notes</a>
-----

Wherever possible, _lambda_ maintains interface compatibility with similar, familiar core Java types. Some examples of where this works well is with both `Fn1` and `Predicate`, which extend `j.u.f.Function` and `j.u.f.Predicate`, respectively. In these examples, they also override any implemented methods to return their _lambda_-specific counterparts (`Fn1.compose` returning `Fn1` instead of `j.u.f.Function` as an example).

Unfortunately, due to Java's type hierarchy and inheritance inconsistencies, this is not always possible. One surprising example of this is how `Fn1` extends `j.u.f.Function`, but `Fn2` does not extend `j.u.f.BiFunction`. This is because `j.u.f.BiFunction` itself does not extend `j.u.f.Function`, but it does define methods that collide with `j.u.f.Function`. For this reason, both `Fn1` and `Fn2` cannot extend their Java counterparts without sacrificing their own inheritance hierarchy. These types of asymmetries are, unfortunately, not uncommon; however, wherever these situations arise, measures are taken to attempt to ease the transition in and out of core Java types (in the case of `Fn2`, a supplemental `#toBiFunction` method is added). I do not take these inconveniences for granted, and I'm regularly looking for ways to minimize the negative impact of this as much as possible. Suggestions and use cases that highlight particular pain points here are particularly appreciated.

<a name="license">License</a>
-------

_lambda_ is part of [palatable](http://www.github.com/palatable), which is distributed under [The MIT License](http://choosealicense.com/licenses/mit/).
