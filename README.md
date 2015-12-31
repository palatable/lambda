lambda
======
[![Build Status](https://travis-ci.org/palatable/lambda.svg)](https://travis-ci.org/palatable/lambda)

Functional patterns for Java 8

Installation
------------

Add the following dependency to your:
 
`pom.xml` ([Maven](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)):
 
 ```xml
 <dependency>
     <groupId>com.jnape.palatable</groupId>
     <artifactId>lambda</artifactId>
     <version>1.0</version>
 </dependency>
 ```
 
 `build.gradle` ([Gradle](https://docs.gradle.org/current/userguide/dependency_management.html)):
 
 ```gradle
  compile group: 'com.jnape.palatable', name: 'lambda', version: '1.0'
  ```
  
Background
----------

Lambda was born out of a desire to use some of the same canonical functions (e.g. `unfoldr`, `takeWhile`, `zipWith`) and functional patterns (e.g. `Functor` and friends) that are idiomatic in other languages and make them available for Java.

Some things a user of lambda most likely values:

- Lazy evaluation
- Immutablility by design
- Composition
- Higher-level abstractions
- Parametric polymorphism

Generally, everything that lambda produces is lazily-evaluated (except for terminal operations like `reduce`), immutable (except for `Iterator`s, since it's effectively impossible), composable (even between different arities, where possible), foundational (maximally contravariant), and parametrically type-checked (even where this adds unnecessary constraints due to a lack of higher-kinded types).

Although the library is currently (very) small, these values should always be the driving forces behind future growth.

Examples
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
  MonadicFunction<Iterable<Integer>, Integer> sumOfEvenIncrementsFn =
            map((Integer x) -> x + 1)
            .then(filter(x -> x % 2 == 0))
            .then(reduceLeft((x, y) -> x + y));
  
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
  MonadicFunction<Integer, Integer> add = x -> x + 1;
  MonadicFunction<Integer, Integer> subtract = x -> x -1;

  MonadicFunction<Integer, Integer> noOp = add.then(subtract);
  // same as
  MonadicFunction<Integer, Integer> alsoNoOp = subtract.fmap(add);
```

And partially apply some:

```Java
  DyadicFunction<Integer, Integer, Integer> add = (x, y) -> x + y;

  MonadicFunction<Integer, Integer> add1 = add.apply(1);
  add1.apply(2);
  //-> 3
```

And have fun with 3s:

```Java
  Iterable<Iterable<Integer>> multiplesOf3InGroupsOf3 =
            take(10, inGroupsOf(3, unfoldr(x -> Optional.of(tuple(x * 3, x + 1)), 1)));
  //-> [[3, 6, 9], [12, 15, 18], [21, 24, 27]]
```

Or check out [the tests](https://github.com/palatable/lambda/tree/master/src/test/java/com/jnape/palatable/lambda/functions/builtin) for more examples.

license
-------

_lambda_ is part of [palatable](http://www.github.com/palatable), which is distributed under [The MIT License](http://choosealicense.com/licenses/mit/).
