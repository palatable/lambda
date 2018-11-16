# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [Unreleased]
### Changed
- ***Breaking Change***: `Difference` and `Intersection` no longer instances of `Semigroup` and moved to `functions.builtin.fn2` package
- ***Breaking Change***: `Absent` moved to `semigroup.builtin` package
- ***Breaking Change***: `Effect#accept()` is now the required method to implement in the functional interface
- ***Breaking Change***: `Fn0#apply()` is now the required method to implement in the functional interface
- ***Breaking Change***: `GTBy`, `GT`, `LTBy`, `LT`, `GTEBy`, `GTE`, `LTEBy`, and `LTE` take the right-hand side first for more intuitive partial application
- `RightAny` overload returns `Monoid`
- monoids now all fold with respect to `foldMap`
- monoid folding now implicitly starts with the identity, regardless of iterable population
- `Concat` monoid can now fold infinite iterables
- all `Function<? super XXX, Boolean>` are now `Function<? super XXX, ? extends Boolean>` for better compatibility  
- `Either#diverge` returns a `Choice3`
- `Maybe` is now a `CoProduct2` of `Unit` and `A`    
- `Fn0` now additionally implements `Callable`

### Added
- `Predicate#predicate` static factory method
- `Product2-8` left/right rotation methods
- `Tuple2-8` specializations of left/right product rotation
- `CheckedEffect`, an `Effect` variant that can throw checked exceptions 
- `CheckedFn1#checked`, convenience static factory method to aid inference 
- `LiftA3-8`, higher-arity analogs to `LiftA2`
- `Alter`, for applying an `Effect` to an input and returning it, presumably altered 
- `Clamp`, for clamping a value between two bounds 
- `Between`, for determining if a value is in a closed interval
- `Strong`, profunctor strength  

### Deprecated
- `AddAll` semigroup, in favor of the monoid that no longer mutates any argument 
- Dyadic `Either#flatMap()`, in favor of `Either#match` 

## [3.1.0] - 2018-07-16
### Added
- `Fn3-8` static factory overloads to aid in coercing lambdas
- Adding composition guarantees to `LensLike`
- `CmpEqBy`, `CmpEq`, `GTBy`, `GT`, `LTBy`, `LT`, `GTEBy`, `GTE`, `LTEBy`, and `LTE` inequality checks
- `MinBy`, `MaxBy`, `Min`, and `Max` semigroups
- `Product2-8` interfaces, representing general product types
- `Union`, a monoid that behaves like a lazy set union on `Iterable`s
- `Difference`, a semigroup that behaves like a partially lazy set difference on `Iterable`s
- `LambdaMap`, extension point for `j.u.Map`, similar to `LambdaIterable`
- `Sequence#sequence` overloads for `j.u.Map` that traverse via intermediate `LambdaMap` instances
- `Intersection`, a semigroup that behaves like a lazy set intersection on `Iterable`s
- `Fn0`, a function from `Unit` to some value
- `Fn1#thunk`, producing an `Fn0`
- `Absent`, a monoid over `Maybe` that is absence biased
- `RateLimit`, a function that iterates elements from an `Iterable` according to some rate limit
- `Try#withResources`, `Try`'s expression analog to Java 7's try-with-resources statement 
- `Occurrences`, for counting the occurrences of the members of an `Iterable`
- `Effect`, an `Fn0` returning `UNIT` 
- `Noop`, a no-op `Effect` 
- `Fn1#widen`, add an ignored argument to the beginning of any function to raise its arity by one 

### Changed
- `Tuple2-8` now implement `Product2-8`
- `Into` now accepts `Map.Entry`
- `Into3-8` now accept a product of the same cardinality, instead of requiring a tuple
- `CoProduct2-8#project` now return generalized products
- `Choice2-8#project` return tuples
- `liftA2` receives more parameters to aid inference
- `Compose#getCompose` now supports inference

### Removed
- `MapLens#mappingValues`, deprecated in a prior release
- `CollectionLens#asSet`, deprecated in a prior release
- `CollectionLens#asStream`, deprecated in a prior release

## [3.0.3] - 2018-05-27
### Added
- `Lens#toIso`, for converting a lens to an iso
- `HMap#hMap` overloads up to 8 bindings deep
- `Schema`, schemas for extracting multiple values from `HMap`s by aggregating `TypeSafeKey`s

### Fixed
- Deforested iterables execute in intended nesting order, where essential

## [3.0.2] - 2018-05-21
### Added
- `IterableLens#mapping`, an `Iso` that maps values

### Changed
- `TypeSafeKey.Simple` now has a default `#apply` implementation

### Fixed
- mapped `TypeSafeKey` instances can be used for initial put in an `HMap`, and the base key can be used to retrieve them
- Merged pull request fixing issue storing values at mapped `TypeSafeKey` in `singletonHMap` 

## [3.0.1] - 2018-05-13
### Changed
- `ToMap` accepts an `Iterable` covariant in `Map.Entry`
- `RecursiveResult#invert` is also a `RecursiveResult`
- `First`/`And`/`Or` monoids all utilize short-circuiting
- `Monoid#foldLeft/foldRight` delegate to `Monoid#reduceLeft/reduceRight`, respectively 

### Added
- `Upcast` for safely casting up a type hierarchy
- `SetLens`, lenses operating on `Set`s
- `ToArray`, for converting `Iterable<A>` to `A[]` 

## [3.0.0] - 2018-05-04
### Changed
- ***Breaking Change***: `Sequence` now has two more type parameters to aid in inference
- ***Breaking Change***: `Traversable#traverse` now has three more type parameters to aid in inference
- ***Breaking Change***: `Monad#zip` now forces `m a -> b` before `m a` in default `Applicative#zip` implementation; this is only breaking for types that are sensitive to computation order (the resulting values are the same)
- ***Breaking Change***: `TypeSafeKey` is now dually parametric (single parameter analog is preserved in `TypeSafeKey.Simple`)
- `Bifunctor` is now a `BoundedBifunctor` where both parameter upper bounds are `Object`
- `Peek2` now accepts the more general `BoundedBifunctor`
- `Identity`, `Compose`, and `Const` functors all have better `toString` implementations
- `Into3-8` now supports functions with parameter variance
- `HListLens#tail` is now covariant in `Tail` parameter
- More functions now automatically deforest nested calls (`concat` `cons`, `cycle`, `distinct`, `drop`, `dropwhile`, `filter`, `map`, `reverse`, `snoc`, `take`, `takewhile`, `tail`)
- `Flatten` calls `Iterator#hasNext` less aggressively, allowing for better laziness
- `Lens` subtypes `LensLike`
- `View`/`Set`/`Over` now only require `LensLike`
- `HMap#keys` now returns a `Set`
- `HMap#values` now returns a `Collection`
- `Unfoldr` is now lazier, deferring all computations until `hasNext/next` calls
- `Present` is now a singleton

### Added
- `BoundedBifunctor`, a `Bifunctor` super type that offers upper bounds for both parameters
- `Try`, a `Monad` representing an expression-like analog of `try/catch/finally`
- `CheckedRunnable`, the `Runnable` counterpart to `CheckedSupplier` that can throw checked exceptions
- `Unit`, the lambda analog to `Void`, except actually inhabited by a singleton instance 
- `Kleisli`, the abstract representation of a `Kleisli` arrow (`Monad#flatMap`) as an `Fn1` 
- `These`, a `CoProduct3` of `A`, `B`, or `Tuple2<A,B>`
- `Span`, for splitting an `Iterable` into contiguous elements matching a predicate
- `MagnetizeBy` and `Magnetize`, for grouping elements by pairwise predicate tests
- `Both`, for dually applying two functions and producing a `Tuple2` of their results
- `Lens#both`, for dually focusing with two lenses at once
- `IfThenElse`, an expression form for `if` statements
- `CheckedRunnable` and `CheckedSupplier` conversion and convenience methods
- `LensLike`, common capabilities that make a type usable as if it were a `Lens`
- `Iso`, isomorphisms between two types (invertible functions that are also lenses)
- `Exchange`, a `Profunctor` that can extract the morphisms from an `Iso`
- `HMapLens`, lenses focusing on `HMap`
- `MapLens#mappingValues(Iso)`, a lawful lens that maps the values of a `j.u.Map`
- `Under`, the inverse of `Over` for `Iso`
- `TypeSafeKey` is an `Iso` and supports mapping
- `TypeSafeKey.Simple`, the single parameter version of `TypeSafeKey`
- `Either#trying` overloads that accept `CheckedRunnable`

### Deprecated
- `MapLens#mappingValues(Function)` is now deprecated in favor of the overload that takes an <code>Iso</code>

## [2.1.1] - 2018-01-16
### Changed
- ***Breaking Change***: Moved `Trampoline` and `RecursiveResult` to better package

## [2.1.0] - 2018-01-14
### Changed
- ***Breaking Change***: `CollectionLens#asSet` is now lawful and preserves new incoming values in the update set 
- ***Breaking Change***: `IterableLens#head` is now a `Lens.Simple<Iterable<A>, Maybe<A>>` and is lawful
- ***Breaking Change***: `ListLens#elementAt` is now a `Lens.Simple<List<X>, Maybe<X>>` supporting defensive copies
- ***Breaking Change***: `MapLens#valueAt` is now a `Lens.Simple<Map<K,V>, Maybe<V>>` supporting defensive copies
- `MapLens#keys` now uses defensive copies and does not alter the focused on map
- `MapLens#values` now uses defensive copies and does not alter the focused on map
- `MapLens#inverted` now uses defensive copies and does not alter the focused on map
- `HListLens#head` is now covariant in the tail of both `S` and `T`
- `Predicate#contraMap` is now covariant in its return type
- `BiPredicate#contraMap` and `BiPredicate#diMapL` are now both covariant in their return types

### Added
- `Fn3#fn3` and `Fn4#fn4` static factory methods
- `Fn5` through `Fn8`
- `Tuple5#into` 
- `Tuple6` through `Tuple8`
- `CoProduct6` through `CoProduct8` and `Choice6` through `Choice8`
- `CoProduct5#diverge` and `Choice5#diverge`
- `Into3` through `Into8`, for applying a `Tuple*` to an `Fn*`
- `Times`, for successively accumulating a result by iterating a function over a value some number of times
- `Slide`, for "sliding" a window of some number of elements across an `Iterable`
- `Either#filter` overload supporting a function from `R` to `L` in the failing predicate case 
- `CollectionLens#asSet(Function)`, a proper analog of `CollectionLens#asSet()` that uses defensive copies
- `CollectionLens#asStream(Function)`, a proper analog of `CollectionLens#asStream()` that uses defensive copies
- Explicitly calling attention to all unlawful lenses in their documentation
- `Peek` and `Peek2`, for "peeking" at the value contained inside any given `Functor` or `Bifunctor` with given side-effects
- `Trampoline` and `RecursiveResult` for modeling primitive tail-recursive functions that can be trampolined 

### Removed
- `Either#toOptional`, deprecated in previous release
- `Either#fromOptional`, deprecated in previous release
- `sequence` overloads supporting `Optional`, deprecated in previous release
- `OptionalLens`, deprecated in previous release
- `TraversableIterable`, deprecated in previous release
- `Traversables`, deprecated in previous release

### Deprecated
- `CollectionLens#asSet()` in favor of `CollectionLens#asSet(Function)`
- `CollectionLens#asStream()` in favor of `CollectionLens#asStream(Function)`

## [2.0.0] - 2017-11-13
### Changed
- ***Breaking Change***: `java.util.Optional` replaced with `Maybe` across the board
- `Profunctor#diMap/L/R` parameters allow variance
- `Either#toOptional` no longer allows `null` values in the right side, and is now in sync with CoProduct#projectB
- `Unfoldr` allows variance on input

### Fixed
- `CoProductN#embed` no longer eagerly invokes functions
- `PrependAll` now only creates `O(1)` `Iterable`s instead of `O(3n + 1)` 

### Added
- `Monad` arrives. The following `Applicative`s are now also `Monad`:
  - `Lens`
  - `Const`
  - `Tuple*`
  - `Choice*`
  - `Identity`
  - `Either`
  - `Fn*`
  - `LambdaIterable`
  - `Maybe`
  - `SingletonHList` 
- `Force`, for forcing iteration of an `Iterable` to perform any side-effects
- `Snoc`, for lazily appending an element to the end of an `Iterable`
- `Coalesce`, for folding an `Iterable<Either<L, R>>` into an `Either<Iterable<L>, Iterable<R>>`
- `And`, `Or`, and `Xor` all gain `BiPredicate<Boolean, Boolean>` properties
- `LambdaIterable`, an adapter `Iterable` that support lambda types
- `Maybe`, lambda's analog of `java.util.Optional` conforming to all the lambda types
- `Contravariant`, an interface representing functors that map contravariantly over their parameters
- `Profunctor` extends `Contravariant`
- `Tails`, for iterating all the tail element subsequences of an `Iterable`
- `Inits`, for iterating all the initial element subsequences of an `Iterable`  
- `Init`, for iterating all but the last element of an `Iterable`
- `CatMaybes`, for unwrapping the present values in an `Iterable<Maybe<A>>` to produce an `Iterable<A>`

### Removed
- `Fn1#then(Function<? super B, ? extends C>)`, deprecated in previous release 
- `Fn1#adapt(Function<A, B> function)`, deprecated in previous release  
- `Fn2#adapt(BiFunction<A, B, C> biFunction)`, deprecated in previous release  

### Deprecated
- `Traversables` and all methods therein, in favor of either `LambdaIterable` or `Maybe`
- `TraversableOptional` in favor of `Maybe`
- `TraversableIterable` in favor of `LambdaIterable`
- `Sequence` overloads supporting `Optional` in favor of converting `Optional` to `Maybe` and then sequencing
- `Either#toOptional` and `Either#fromOptional` in favor of its `Maybe` counterparts

## [1.6.3] - 2017-09-27
### Changed
- Loosening variance on `Fn2#fn2` and `Fn1#fn1`

### Fixed
- `ConcatenatingIterator` bug where deeply nested `xs` skip elements

### Deprecated
- `Fn1#then` in favor of `Fn1#andThen` (redundant)
- `Fn1#adapt` in favor of `Fn1#fn1` (rename)
- `Fn2#adapt` in favor of `Fn2#fn2` (rename)

### Added
- `Fn1#andThen` overload to support composition with `Bifunction`
- `Fn1#compose` overload to support composition with `Bifunction` and `Fn2`
- `LiftA2` to lift and apply a `Bifunction` to two `Applicative`s
- `Flatten` to lazily flatten nested `Iterable<Iterable<A>>`s to `Iterable<A>`
- `Replicate`, short-hand composition of `take` and `repeat`
- `Distinct` to produce an `Iterable` of distinct values in another `Iterable`
- `Sort` and `SortBy` for eagerly, monolithically sorting `Iterable`s and producing `List`s  
- `IterableLens`, general lenses over `Iterable`
- `Xor`, a monoid representing logical exclusive-or

## [1.6.2] - 2017-08-20
### Changed
- Removing need for various suppressed unchecked warnings in `ChoiceN` types
- `HList` abstract super type loses both unnecessary parameters

### Fixed
- ClassCastException `BiPredicate.flip` 

### Added
- `Uncons`, for destructuring an `Iterable` into its head and tail
- `Compose` semigroup and monoid formed over `CompletableFuture`
- `Monoid` and `Semigroup` both preserve type specificity through `flip` calls 

## [1.6.1] - 2017-06-17
### Changed
- Loosening visibility on `Traversables` methods to `public`

## [1.6.0] - 2017-06-04
### Changed
- `Functor`, `Bifunctor`, and `Profunctor` (as well as all instances) get a unification parameter
- `Identity` supports value equality
- `Const` supports value equality
- `partition` now only requires iterables of `CoProudct2`
- `CoProductN`s receive a unification parameter, which trickles down to `Either` and `Choice`s
- `Concat` now represents a monoid for `Iterable`; previous `Concat` semigroup and monoid renamed to more appropriate `AddAll`
- `Lens` is now an instance of `Profunctor`

### Added
- `Either#invert` is pulled up into `CoProduct2` and additionally specialized for `Choice2`
- `CoProductN#embed`
- `not`, used for negating predicate functions
- `empty`, used to test if an Iterable is empty
- `groupBy`, for folding an Iterable into a Map given a key function
- `Applicative` arrives; all functors gain applicative properties
- `Traversable` arrives; `SingletonHList`, `Tuple*`, `Choice*`, `Either`, `Identity`, and `Const` gain traversable properties
- `TraversableOptional` and `TraversableIterable` for adapting `Optional` and `Iterable`, respectively, to `Traversable`
- `sequence` for wrapping a traversable in an applicative during traversal 
- `Compose`, an applicative functor that represents type-level functor composition

## [1.5.6] - 2017-02-11
### Changed
- `CoProductN.[a-e]()` static factory methods moved to equivalent `ChoiceN` class. Coproduct interfaces now solely represent methods, no longer have anonymous implementations, and no longer require a `Functor` constraint

### Added
- `ChoiceN` types, representing concrete coproduct implementations that are also `Functor` and `BiFunctor`
- `toMap`, `last`, `cons`, `prependAll`, `intersperse`
- `Tuple2/3/4#into`, for applying the values in a tuple as positional arguments to a function.
- `First` and `Last` monoids over `Optional`
- `And` and `Or` monoids over `Boolean`

## [1.5.5] - 2016-12-17
### Changed
- semigroups and monoids moved under `fn2` package

### Added
- `CoProductN#project`, to project disjoint union types into tuples of `Optional` values
- `CoProductN#converge`, to drop the magnitude of a coproduct down by one type
- `toCollection` and `size`

## [1.5.4] - 2016-11-27
### Added
- `Fn1/2#adapt` to switch between lambda and `java.util.function` types more easily
- `eq`, `head`, `find`, and `tail`
- `BiPredicate`
- `Monoid#foldMap`
- `HMap#toMap` to go from a heterogeneous map to a `java.util.Map`

## [1.5.3] - 2016-11-06
### Added
- `Semigroup` and `Monoid`
- `Either#invert`
- `partition`
- Generalized coproducts implemented as `CoProduct2` through `CoProduct5`
- `Either` is now a `CoProduct2` 

## [1.5.2] - 2016-09-24
### Changed
- `Lens` static factory method renaming

### Added
- Heterogeneous list indexes arrive via `Index`

## [1.5.1] - 2016-08-30
### Added
- Independent `Lens` parameter mapping via `mapS`, `mapT`, `mapA`, and `mapB`

## [1.5] - 2016-08-28
### Added
- Initial lens support with `Lens` and `SimpleLens` types and `view`, `set`, and `over` functions
- `Const` and `Identity` functors
- `Either#toOptional`
- `HMap#remove` and `HMap#removeAll`

## [1.4] - 2016-08-08
### Changed
- All function input values become `java.util.function` types, and all function output values remain lambda types, for better compatibility

## [1.3] - 2016-07-31
### Changed
- `Profunctor` inheritance hierarchy
- Renaming `Identity` to `Id`
- `Monadic/Dyadic/TriadicFunction` is now `Fn1/2/3`

### Added
- `HList` specializations support random access lookup

## [1.2] - 2016-06-27
### Changed
- `Tuple`s moved under `HList` as specialized subtypes

### Added
- `Either#peek`
- `HMap`, heterogeneous maps
- `Tuple2` is now also a `Map.Entry`

## [1.1] - 2016-06-21
### Changed
- Better interoperability between lambda and `java.util.function` types

### Added
- `scanLeft`
- `HList`, heterogeneous lists
- Added up to `Tuple5`
- `Either`, specialized coproduct with success/failure semantics

## [1.0] - 2015-12-29
### Added
- Initial implementation of first-class curried functions 
- `all`, `any`, `cartesianProduct`, `cycle`, `drop`, `dropWhile`, `filter`, `foldLeft`,
  `foldRight`, `inGroupsOf`, `map`, `partial2`, `partial3`, `reduceLeft`, `reduceRight`,
  `repeat`, `take`, `takeWhile`, `unfoldr`
- `Monadic/Dyadic/TriadicFunction`, `Predicate`, `Tuple2`, `Tuple3`
- `Functor`, `BiFunctor`, `ProFunctor` 

[Unreleased]: https://github.com/palatable/lambda/compare/lambda-3.1.0...HEAD
[3.1.0]: https://github.com/palatable/lambda/compare/lambda-3.0.3...lambda-3.1.0
[3.0.3]: https://github.com/palatable/lambda/compare/lambda-3.0.2...lambda-3.0.3
[3.0.2]: https://github.com/palatable/lambda/compare/lambda-3.0.1...lambda-3.0.2
[3.0.1]: https://github.com/palatable/lambda/compare/lambda-3.0.0...lambda-3.0.1
[3.0.0]: https://github.com/palatable/lambda/compare/lambda-2.1.1...lambda-3.0.0
[2.1.1]: https://github.com/palatable/lambda/compare/lambda-2.1.0...lambda-2.1.1
[2.1.0]: https://github.com/palatable/lambda/compare/lambda-2.0.0...lambda-2.1.0
[2.0.0]: https://github.com/palatable/lambda/compare/lambda-1.6.3...lambda-2.0.0
[1.6.3]: https://github.com/palatable/lambda/compare/lambda-1.6.2...lambda-1.6.3
[1.6.2]: https://github.com/palatable/lambda/compare/lambda-1.6.1...lambda-1.6.2
[1.6.1]: https://github.com/palatable/lambda/compare/lambda-1.6.0...lambda-1.6.1
[1.6.0]: https://github.com/palatable/lambda/compare/lambda-1.5.6...lambda-1.6.0
[1.5.6]: https://github.com/palatable/lambda/compare/lambda-1.5.5...lambda-1.5.6
[1.5.5]: https://github.com/palatable/lambda/compare/lambda-1.5.4...lambda-1.5.5
[1.5.4]: https://github.com/palatable/lambda/compare/lambda-1.5.3...lambda-1.5.4
[1.5.3]: https://github.com/palatable/lambda/compare/lambda-1.5.2...lambda-1.5.3
[1.5.2]: https://github.com/palatable/lambda/compare/lambda-1.5.1...lambda-1.5.2
[1.5.1]: https://github.com/palatable/lambda/compare/lambda-1.5...lambda-1.5.1
[1.5]: https://github.com/palatable/lambda/compare/lambda-1.4...lambda-1.5
[1.4]: https://github.com/palatable/lambda/compare/lambda-1.3...lambda-1.4
[1.3]: https://github.com/palatable/lambda/compare/lambda-1.2...lambda-1.3
[1.2]: https://github.com/palatable/lambda/compare/lambda-1.1...lambda-1.2
[1.1]: https://github.com/palatable/lambda/compare/lambda-1.0...lambda-1.1
[1.0]: https://github.com/palatable/lambda/commits/lambda-1.0
