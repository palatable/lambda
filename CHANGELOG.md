# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [Unreleased]
### Deprecated
- `Fn1#then` in favor of `Fn1#andThen` (redundant)
- `Fn1#adapt` in favor of `Fn1#fn1` (rename)
- `Fn2#adapt` in favor of `Fn2#fn2` (rename)

### Added
- `Fn1#andThen` overload to support composition with `Bifunction`
- `Fn1#compose` overload to support composition with `Bifunction`
- `LiftA2` to lift and apply a `Bifunction` to two `Applicative`s
- `Flatten` to lazily flatten nested `Iterable<Iterable<A>>`s to `Iterable<A>`
- `Replicate`, short-hand composition of `take` and `repeat`
- `Distinct` to produce an `Iterable` of distinct values in another `Iterable`
- `Sort` and `SortBy` for eagerly, monolithically sorting `Iterable`s and producing `List`s  

## [1.6.2] - 2017-08-20
### Fixed
- ClassCastException `BiPredicate.flip` 

### Added
- `Uncons`, for destructuring an `Iterable` into its head and tail
- `Compose` semigroup and monoid formed over `CompletableFuture`
- `Monoid` and `Semigroup` both preserve type specificity through `flip` calls 

### Changed
- Removing need for various suppressed unchecked warnings in `ChoiceN` types
- `HList` abstract super type loses both unnecessary parameters 

## [1.6.1] - 2017-06-17
### Changed
- Loosening visibility on `Traversables` methods to `public`

## [1.6.0] - 2017-06-04
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

### Changed
- `Functor`, `Bifunctor`, and `Profunctor` (as well as all instances) get a unification parameter
- `Identity` supports value equality
- `Const` supports value equality
- `partition` now only requires iterables of `CoProudct2`
- `CoProductN`s receive a unification parameter, which trickles down to `Either` and `Choice`s
- `Concat` now represents a monoid for `Iterable`; previous `Concat` semigroup and monoid renamed to more appropriate `AddAll`
- `Lens` is now an instance of `Profunctor`

## [1.5.6] - 2017-02-11
### Added
- `ChoiceN` types, representing concrete coproduct implementations that are also `Functor` and `BiFunctor`
- `toMap`, `last`, `cons`, `prependAll`, `intersperse`
- `Tuple2/3/4#into`, for applying the values in a tuple as positional arguments to a function.
- `First` and `Last` monoids over `Optional`
- `And` and `Or` monoids over `Boolean`

### Changed
- `CoProductN.[a-e]()` static factory methods moved to equivalent `ChoiceN` class. Coproduct interfaces now solely represent methods, no longer have anonymous implementations, and no longer require a `Functor` constraint

## [1.5.5] - 2016-12-17
### Added
- `CoProductN#project`, to project disjoint union types into tuples of `Optional` values
- `CoProductN#converge`, to drop the magnitude of a coproduct down by one type
- `toCollection` and `size`

### Changed
- semigroups and monoids moved under `fn2` package

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
### Added
- Heterogeneous list indexes arrive via `Index`

### Changed
- `Lens` static factory method renaming

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
### Added
- `HList` specializations support random access lookup

### Changed
- `Profunctor` inheritance hierarchy
- Renaming `Identity` to `Id`
- `Monadic/Dyadic/TriadicFunction` is now `Fn1/2/3`

## [1.2] - 2016-06-27
### Added
- `Either#peek`
- `HMap`, heterogeneous maps
- `Tuple2` is now also a `Map.Entry`

### Changed
- `Tuple`s moved under `HList` as specialized subtypes

## [1.1] - 2016-06-21
### Added
- `scanLeft`
- `HList`, heterogenous lists
- Added up to `Tuple5`
- `Either`, specialized coproduct with success/failure semantics

### Changed
- Better interoperability between lambda and `java.util.function` types

## [1.0] - 2015-12-29
### Added
- Initial implementation of first-class curried functions 
- `all`, `any`, `cartesianProduct`, `cycle`, `drop`, `dropWhile`, `filter`, `foldLeft`,
  `foldRight`, `inGroupsOf`, `map`, `partial2`, `partial3`, `reduceLeft`, `reduceRight`,
  `repeat`, `take`, `takeWhile`, `unfoldr`
- `Monadic/Dyadic/TriadicFunction`, `Predicate`, `Tuple2`, `Tuple3`
- `Functor`, `BiFunctor`, `ProFunctor` 

[Unreleased]: https://github.com/palatable/lambda/compare/lambda-1.6.2...HEAD
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
