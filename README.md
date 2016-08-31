λ
======
[![Build Status](https://travis-ci.org/palatable/lambda.svg)](https://travis-ci.org/palatable/lambda)
[![Lambda](https://img.shields.io/maven-central/v/com.jnape.palatable/lambda.svg)](http://search.maven.org/#search%7Cga%7C1%7Ccom.jnape.palatable.lambda)

Functional patterns for Java 8

#### Table of Contents

 - [Background](#background)
 - [Installation](#installation)
 - [Examples](#examples)
 - [ADTs](#adts)
   - [HLists](#hlists)
     - [Tuples](#tuples)
   - [HMaps](#hmaps)
   - [Either](#either)
 - [Lenses](#lenses)
 - [Notes](#notes) 
 - [License](#license)

<a name="background">Background</a>
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

<a name="installation">Installation</a>
------------

Add the following dependency to your:
 
`pom.xml` ([Maven](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)):
 
```xml
<dependency>
    <groupId>com.jnape.palatable</groupId>
    <artifactId>lambda</artifactId>
    <version>1.5.1</version>
</dependency>
```
 
`build.gradle` ([Gradle](https://docs.gradle.org/current/userguide/dependency_management.html)):
 
```gradle
compile group: 'com.jnape.palatable', name: 'lambda', version: '1.5.1'
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
Fn1<Integer, Integer> add = x -> x + 1;
Fn1<Integer, Integer> subtract = x -> x -1;

Fn1<Integer, Integer> noOp = add.then(subtract);
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
          take(10, inGroupsOf(3, unfoldr(x -> Optional.of(tuple(x * 3, x + 1)), 1)));
//-> [[3, 6, 9], [12, 15, 18], [21, 24, 27]]
```

Check out the [tests](https://github.com/palatable/lambda/tree/master/src/test/java/com/jnape/palatable/lambda/functions/builtin) or [javadoc](http://palatable.github.io/lambda/javadoc/) for more examples.

<a name="adts">ADTs</a>
----

In addition to the functions above, lambda also supports a few first-class [algebraic data types](https://www.wikiwand.com/en/Algebraic_data_type).

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

To address this, tuples in lambda are specializations of `HList`s up to 5 elements deep, with added support for index-based accessor methods.

```Java
HNil nil = HList.nil();
SingletonHList<Integer> singleton = nil.cons(5);
Tuple2<Integer, Integer> tuple2 = singleton.cons(4);
Tuple3<Integer, Integer, Integer> tuple3 = tuple2.cons(3);
Tuple4<Integer, Integer, Integer, Integer> tuple4 = tuple3.cons(2);
Tuple5<Integer, Integer, Integer, Integer, Integer> tuple5 = tuple4.cons(1);

System.out.println(tuple2._1()); // prints 4
System.out.println(tuple5._5()); // prints 5
```

Additionally, `HList` provides convenience static factory methods for directly constructing lists of up to 5 elements:

```Java
SingletonHList<Integer> singleton = HList.singletonHList(1);
Tuple2<Integer, Integer> tuple2 = HList.tuple(1, 2);
Tuple3<Integer, Integer, Integer> tuple3 = HList.tuple(1, 2, 3);
Tuple4<Integer, Integer, Integer, Integer> tuple4 = HList.tuple(1, 2, 3, 4);
Tuple5<Integer, Integer, Integer, Integer, Integer> tuple5 = HList.tuple(1, 2, 3, 4, 5);
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

### <a name="either">Either</a>

Binary tagged unions are represented as `Either<L, R>`s, which resolve to one of two possible values: a `Left` value wrapping an `L`, or a `Right` value wrapping an `R` (typically an exceptional value or a successful value, respectively).

Rather than supporting explicit value unwrapping, `Either` supports many useful comprehensions to help facilitate type-safe interactions. For example, `Either#match` is used to resolve an `Either<L,R>` to a different type.    

```Java
Either<String, Integer> right = Either.right(1);
Either<String, Integer> left = Either.left("Head fell off");

Boolean successful = right.match(l -> false, r -> true);
//-> true

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
