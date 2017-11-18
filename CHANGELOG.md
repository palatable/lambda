# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [Unreleased]
### Added
- `Tuple6` through `Tuple8`

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

[Unreleased]: https://github.com/palatable/lambda/compare/lambda-2.0.0...HEAD
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
