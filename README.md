# NArr
Pronounced: <i>(ˈnär, as in gnarly)</i>.&nbsp;&nbsp;Stands for: <i>Native Array</i><br />

&nbsp;&nbsp;&nbsp;Why Arrays?&nbsp;&nbsp;Because they have the lowest memory footprint and the deepest hardware optimization!&nbsp;&nbsp;As Daniel Spiewak famously understated the matter: <a href="https://youtu.be/n5u7DgFwLGE?t=720">"As good as you think Arrays are, they are better!"</a>&nbsp;&nbsp;Arrays are so light and fast that the <a href="https://www.biblegateway.com/quicksearch/?quicksearch=Array&version=KJV">Hebrew Bible</a> mentions them over 40 times!&nbsp;&nbsp;Unfortunately, this sacred and holy data structure causes a host of problems in cross-compiled Scala projects, mostly because of JavaScript idiosyncracies.&nbsp;&nbsp;More specifically, although Scala Native and Scala JVM share a single unified Array type, Scala.js presents no fewer than 14: `scala.Array`, `js.Array`, `Int8Array`, `Uint8Array`, `Uint8ClampedArray`, `Int16Array`, `Uint16Array`, `Int32Array`, `Uint32Array`, `Float16Array`, `Float32Array`, `Float64Array`, `BigInt64Array`, and `BigUint64Array`.&nbsp;&nbsp;This wide diversity begets a web of frustrating design tradeoffs, but fear not!&nbsp;&nbsp;The following text not only maps them out but also shows how NArr addresses them all. 
<ol>
<li>

&nbsp;&nbsp;Choosing the Right Array Type.

&nbsp;&nbsp;&nbsp;"Scala supports `scala.Array[T]` on all compilation targets, so what's the harm in always using that?"&nbsp;&nbsp;True, but in Scala.js, `scala.Array[T]` wraps either JavaScript's signature associative array or the most relevant `TypedArray` depending on the value of `T`.  As a system of aliases for native types, NArr wraps nothing!  As such, it not only eliminates wrapper related memory overhead, but also all friction related to native interoperability.  For more about how NArr streamlines native interop, see the section about it below.<br />
&nbsp;&nbsp;&nbsp;"What about `js.Array[T]` then?"&nbsp;&nbsp;That improves JavaScript interop and eliminates overhead for Arrays of objects, Bytes, Chars, and Longs, but disqualifies lighter alternatives for Arrays where `T ∈ {Byte, Short, Int, Float, Double}`.&nbsp;&nbsp;Worse, Scala JVM and Native don't support `js.Array[T]` so using it necessitates parallel implementations of methods, classes, or even entire programs.  NArr, by contrast, proliferates the most optimized possible Array type across an entire codebase, eliminating the need for any platform specific code.<br />
&nbsp;&nbsp;&nbsp;Maybe one of the `TypedArray`s?  Again, these aren't supported on JVM or Native.  Trying to rely on them in a cross project requires a lot of duplicate code.<br /><br />
&nbsp;&nbsp;&nbsp;Instead of these, use `narr.NArray[T]` as a drop in replacement for any other Array type because it always reduces to the most optimized native array type available on the compilation target platform.  As a system of type aliases, `narr.NArray[T]` introduces no runtime costs on any platform, necessitates no parallel implementations of Array related methods and classes, and provides seamless interoperability with native code.  The following table articulates the system of type aliases across all three platforms:
<table>
<tr>
    <td>Declaration</td>
    <td>Meaning in JavaScript</td>
    <td>Meaning in JVM and Native</td>
</tr>
<tr>
<td>

```scala
NArray[Byte]
NArray[Short]
NArray[Int]
NArray[Float]
NArray[Double]
NArray[Long]
NArray[String]
NArray[AnyRef]
NArray[NArray[Int]]
NArray[NArray[AnyRef]]
```
</td>
<td>

```scala
Int8Array
Int16Array
Int32Array
Float32Array
Float64Array
js.Array[Long]
js.Array[String]
js.Array[AnyRef]
js.Array[Int32Array]
js.Array[js.Array[AnyRef]]
```
</td>
<td>

```scala
scala.Array[Byte]
scala.Array[Short]
scala.Array[Int]
scala.Array[Float]
scala.Array[Double]
scala.Array[Long]
scala.Array[String]
scala.Array[AnyRef]
scala.Array[Array[Int]]
scala.Array[Array[AnyRef]]
```
</td>
</tr>
</table>
</li>
<li>

&nbsp;&nbsp;👣 Memory Footprint.

&nbsp;&nbsp;&nbsp;Because `narr.NArray[T]` at its core, consists only of type aliases, it will always select the most memory efficient available `TypedArray` or, for objects and Scala's value types that have no native equivalent in JavaScript, it will resort to JavaScript's signature associative Array which benefits from a long history as the only data structure in JavaScript and in turn, extensive optimization.  The system of type aliases itself consists of match types which reduce to `scala.Array[T]` on JVM and Native platforms.  The following code snippet illustrates how they reduce in Scala.js:

```scala
type NArray[T] = T match
  case Byte => scala.scalajs.js.typedarray.Int8Array
  case Short => scala.scalajs.js.typedarray.Int16Array
  case Int => scala.scalajs.js.typedarray.Int32Array
  case Float => scala.scalajs.js.typedarray.Float32Array
  case Double => scala.scalajs.js.typedarray.Float64Array
  case _ => scala.scalajs.js.Array[T]
```
</li>
<li>

&nbsp;&nbsp;🏎 Speed.

&nbsp;&nbsp;&nbsp;As discussed in Choosing the Right Array Type, NArr always reduces to the most optimized possible Array type available to Scala.js.  By simply typing `NArray` instead of `Array` a cross compiled code base automatically benefits from minimum memory footprint and maximum hardware acceleration.<br />&nbsp;
</li>
<li>

&nbsp;&nbsp;Native Interoperability.

&nbsp;&nbsp;&nbsp;Imagine trying to make a cross compiled Scala library accessible to JavaScript developers.&nbsp;&nbsp;Scala.js makes that possible through annotations like `@JSExport("...")`, `@JSExportAll`, and `@JSExportTopLevel("...")`.  Now consider a method that accepts an Array as a parameter and/or returns an Array:

```scala
@JSExportTopLevel("fooBarMagic")
def fooBarMagic(a:scala.Array[Int]): scala.Array[Int] = ...
```

How will a native JavaScript developer procure an array of type: `scala.Array[Int]`?  How will she make use of the return value or pass it onto other JavaScript code?&nbsp;&nbsp;Traditionally, Scala.js developers handle this in one of two ways: either by writing a separate implementation of the library specially for JavaScript, or by providing a conversion method to the js project which calls the shared code.&nbsp;&nbsp;Although carefully writing a separate implementation specially for JavaScript can preserve performance it doubles production and maintenance costs.&nbsp;&nbsp;Most Scala.js projects simply abandon the idea of supporting native JavaScript accessibility, but for the sake of convenience some Scala.js developers opt for writing conversion methods like so:

```scala
@JSExportTopLevel("fooBarMagic")
def fooBarMagicHelper(a:scala.scalajs.js.typedarray.Int32Array): scala.scalajs.js.typedarray.Int32Array = {
  // convert to Array[Int]
  val temp0 = new scala.Array[Int](a.length)
  var i = 0
  while (i < a.length) {
    temp0(i) = a(i)
    i = i + 1
  }
  // invoke fooBarMagic
  val temp1 = fooBarMagic(temp0)
  //  convert back to Int32Array
  val out = new scala.scalajs.js.typedarray.Int32Array(a.length)
  i = 0
  while (i < a.length) {
    out(i) = temp(i)
    i = i + 1
  }
  out
}
```

Although this approach makes use of shared code, and increases developer convenience somewhat, it abandons performance by trippling memory footrpint and requiring two separate `O(n)` array conversions that can't benefit from SIMD capable hardware.&nbsp;&nbsp;NArr by contrast, provides the best of both approaches for the one time cost of a simple refactor of the original code:

```scala
@JSExportTopLevel("fooBarMagic")
def fooBarMagic(a:narr.NArray[Int]): narr.NArray[Int] = ...
```

In this way, all platforms share the exact same code without any conversions or wrappers.&nbsp;&nbsp;What's more, Java, C/C++, and JavaScript developers can seamlessly interact with the library using the native Array types most familiar to their respective platforms. 
</li>
<li>

&nbsp;&nbsp;Code Redundancy.

&nbsp;&nbsp;&nbsp;As described in Native Interoperability, NArr eliminates the need for platform specific Array optimizations.&nbsp;&nbsp;
</li>
<li>

&nbsp;&nbsp;ArrayOps: Mixed Support for Scala Semantics. 

&nbsp;&nbsp;&nbsp;A major impediment to using JavaScript `TypedArray`s in Scala.js projects comes from the fact that while `scala.Array[T]` and `js.Array[T]` have their respective `ArrayOps` utilities, no such functionality has ever existed for `Int8Array`, `Int16Array`, `Int32Array`, `Float32Array`, and `Float64Array`.&nbsp;&nbsp;Fortunately NArr polyfills almost all of these so Scala developers can enjoy highly optimized Scala semantics on every kind of Array.
<br /><a href="https://dragonfly-ai.github.io/narr/FeatureGrid">Click here to compare NArr features to those built into Scala JVM/Native and Scala.js</a>.
</li>
</ol>

&nbsp;&nbsp;&nbsp;In short, NArr shrinks code bases and memory footprint; saves time: run, code, and maintenance; and also simplifies native interoperability.<br />

<br />


## Caveats:

<ul>
<li>NArr relies heavily on Scala 3 features and sadly offers no support for Scala 2.</li>
<li>

In some cases, type inference fails on higher kinds, for example, consider the following class:

```scala
class Foo[T](a:NArray[T])
```

We might expect the compiler to infer that `T = Int` from statements like: 

```scala
val a = NArray(1, 2, 3)
val f = new Foo(a)
```
or even:

```scala
val a = NArray[Int](1, 2, 3)
val f = new Foo(a)
```

however, the compiler fails to infer the correct type for `T` when its evidence spans multiple lines.&nbsp;&nbsp;Luckily, we can avoid these situations by providing type parameters explicitly.&nbsp;&nbsp;The following alternatives all compile and run correctly:

```scala
// multi-line
val a = NArray(1, 2, 3)
val f = new Foo[Int](a)

val a = NArray[Int](1, 2, 3)
val f = new Foo[Int](a)

// single line
val f = new Foo(NArray(1, 2, 3))
val f = new Foo(NArray[Int](1, 2, 3))
val f = new Foo[Int](NArray[Int](1, 2, 3))
```

</li>
</ul>

&nbsp;&nbsp;&nbsp;Although the `TypedArray` family of data structures avoids the following issues, they pertain to the more ubiquitous `js.Array`, or, in NArr terms: `NArray[Boolean]`, `NArray[Char]`, `NArray[Long]`, `NArray[String]`, `NArray[AnyRef]`, etc.
<ul>
<li>Type Safety Concerns.<br />

&nbsp;&nbsp;&nbsp;Because the JVM `Array` has a reference to the type of its elements and the JavaScript `Array` has no concept of type at all, careless or malicious JavaScript developers could spike a js.Array with unexpected or dangerous values before passing it to an unsuspecting Scala.js method that takes parameters of type `NArray[T]`.  Security concerns aside, if abused, these type system distinctions could cause unwanted run time differences between the behavior of JVM and JavaScript versions of a program.  However, using `js.Array[T]` explicitly doesn't solve these issues, either.  These concerns reflect design flaws in JavaScript not NArr.
</li>
<li>Size Inconsistency and Mutability Concerns.<br />

&nbsp;&nbsp;&nbsp;Every `scala.Array` has an immutable length while any `js.Array` can grow or shrink dynamically.&nbsp;&nbsp;To mitigate the usual dangers of shared mutable state, users of NArr had better treat all instances of `NArray` like JVM arrays by prohibiting all length altering operations.
</li>
<li>

Output from `toString()` differs by platform.  For example:

```scala
println(new NArray[Int](3))
```

yields different results depending on the platform on which it runs.
<ul>
<li>

Scala.js prints the contents of the array: `0,0,0`

</li>
<li>

On the JVM, it prints something like: `[I@176c3251`

</li>
<li>

In Scala Native, it prints something like: `scala.scalanative.runtime.IntArray@24da9104`

</li>
</ul>
<br />
For consistency, please make use of the `mkString` method:

```scala
println(new NArray[Int](3).mkString(","))
```

</li>
<li>

In Scala.js, accessing an index of newly allocated Array yields: `undefined` and, on the JVM and Native, yields `null`.

</li>
<li>

Sorting in JavaScript environments.<br />
&nbsp;&nbsp;&nbsp;Every native JavaScript array type has a <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/sort">builtin `sort(compareFn)` method</a> where `compareFn` indicates an optional comparison lambda.  For the `TypedArray` family, default sorting behaves like default sorting in Scala, however, the default behavior of `js.Array[T <: AnyRef]` sorts its elements alphabetically by their `toString()` representations.  From Scala development perspectives, we almost never want that, so NArr provides extension methods that connect Scala `Ordering[T]` objects to native JavaScript sorting methods.  In general, you can assure consistent sorting behavior across platforms by explicitly providing the desired sort order.  The most illuminating example comes from trying to sort `NArray[Long]`:

```scala
// Sorting a NArray[Long]:
val sla: NArray[Long] = NArray[Long](555L, 4444L, 9L, 11111L, 88L)
sla.sort() // default JavaScript sorting calls toString() on each element.
// yields: NArray[Long](11111, 4444, 555, 88, 9) sorted in alphabetical order.  :(
sla.sort(Ordering.Long) // Better pass the ordering explicitly!
// yields: NArray[Long](9, 88, 555, 4444, 11111) sorted in numerically ascending order.  :)
```
</li>

<li>

Unsupported `ArrayOps` Methods:
<br />

&nbsp;&nbsp;&nbsp;Unfortunately some `ArrayOps` methods proved untenable to implement.  If the reader has any ideas about how to implement them, the project welcomes pull requests or suggestions.

```scala
def stepper[S <: Stepper[_]](implicit shape: StepperShape[T, S]): S with EfficientSplit
def withFilter(p: T => Boolean): ArrayOps.WithFilter[T]
def lazyZip[B](that: Iterable[B]): LazyZip2[T, B, Array[T]]
def transpose[B](implicit asArray: T => Array[B]): Array[Array[B]]
def combinations(n: Int): Iterator[Array[T]]
def permutations: Iterator[Array[T]]
```
</li>

<li>

Renamed `ArrayOps` methods:
<br />

&nbsp;&nbsp;&nbsp;By contrast, these methods could be implemented, but because of overloaded method limitations in JavaScript had to take different names:
```scala
def startsWith[B >: T](that: IterableOnce[B], offset: Int = 0): Boolean
// instead use:
def startsWithIterable[B >: T](that: IterableOnce[B], offset: Int = 0): Boolean

def endsWith[B >: T](that: Iterable[B]): Boolean
// instead use:
def endsWithIterable[B >: T](that: Iterable[B]): Boolean
```
</li>
</ul>
<br />

## When to use NArr:

<table>
<tr>
    <td><b>Array Dependency</b></td>
    <td><b>Convenience</b></td>
    <td colspan="3"><b>Performance Increase</b></td>
    <td><b>Notes</b></td>
</tr>
<tr>
    <td colspan="2"></td>
    <td><b>JS</b></td>
    <td><b>JVM</b></td>
    <td><b>Native</b></td>
    <td></td>
</tr>
<tr>
    <td>No Arrays</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>NArr offers no utility for projects that do not use Arrays.</td>
</tr>
<tr>
    <td>js.Array[T <: AnyRef]</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>js.Array[T] will suffice</td>
</tr>
<tr>
    <td>Array[T]</td>
    <td>☆☆☆</td>
    <td>★☆☆</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>Array[T] might not perform as optimally as the native js.Array[T] in JavaScript environments.</td>
</tr>
<tr>
    <td>js.Array[T] and Array[T]</td>
    <td>★★★</td>
    <td>★★★</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>Seamless optimized interop with native code on both platforms without any conversions or wrappers.</td>
</tr>
<tr>
    <td>Int8Array or Array[Byte]</td>
    <td>★★★</td>
    <td>★★★</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>Seamless optimized interop with native code on both platforms without any conversions or wrappers.</td>
</tr>
<tr>
    <td>Int16Array or Array[Short]</td>
    <td>★★★</td>
    <td>★★★</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>Seamless optimized interop with native code on both platforms without any conversions or wrappers.</td>
</tr>
<tr>
    <td>Int32Array or Array[Int]</td>
    <td>★★★</td>
    <td>★★★</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>Seamless optimized interop with native code on both platforms without any conversions or wrappers.</td>
</tr>
<tr>
    <td>Float32Array or Array[Float]</td>
    <td>★★★</td>
    <td>★★★</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>Seamless optimized interop with native code on both platforms without any conversions or wrappers.</td>
</tr>
<tr>
    <td>Float64Array or Array[Double]</td>
    <td>★★★</td>
    <td>★★★</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>Seamless optimized interop with native code on both platforms without any conversions or wrappers.</td>
</tr>
<tr>
    <td>Other JavaScript `TypedArray`s</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>Good JVM analogues do not exist.  NArr can't help, but what can?</td>
</tr>
</table>

NArr has no impact on JVM or Native performance, but it can dramatically speed up JavaScript by making use of natively optimized data structures and eliminating conversions that tend to have `O(n)` run time complexities.  It also adds convenience methods for `js.Array[T]` such as `fill` and `tabulate`, but mainly eliminates the need for specially crafted and maintained `@JSExport` methods and fields for JavaScript interop.

To use this library with SBT:

```scala
libraryDependencies += "ai.dragonfly" %%% "narr" % "<LATEST_VERSION>"
```

How to use NArr:

```scala
import narr.*

// constructor call
val a1:NArray[String] = new NArray[String](5)  // in JavaScript this resolves to js.Array[String]

// literal
val a2:NArray[Int] = NArray[Int](2, 4, 8, 16)  // in JavaScript this resolves to Int32Array

// fill
val a3:NArray[Double] = NArray.fill[Double](10)(42)  // in JavaScript this resolves to Float64Array

// tabulate
val a4:NArray[Double] = NArray.tabulate[Double](42)(
  (i:Int) => i * Math.random()
)

// multi dimensional
val a2d:NArray[NArray[Double]] = NArray[NArray[Double]](
  NArray.tabulate[Double](5)( (i:Int) => i * Math.random() ),
  NArray.tabulate[Double](5)( (i:Int) => i * Math.random() ),
  NArray.tabulate[Double](5)( (i:Int) => i * Math.random() ),
  NArray.tabulate[Double](5)( (i:Int) => i * Math.random() )
) // in JavaScript this resolves to js.Array[Float64Array]

// enjoy Scala semantics
a2d.foreach((d:Double) => println(d))

// conveniently expose Scala.js libraries to native JavaScript and Java developers without any need for wrappers or conversions.
@JSExportTopLevel("copy")
def copy(a0:NArray[Double]): NArray[Double] = NArray.tabulate[Double](a0.length)(
  (i:Int) => a0(i)
)

```
<hr />
Projects that rely on NArr:

https://github.com/dragonfly-ai/uriel

https://github.com/dragonfly-ai/vector

https://github.com/dragonfly-ai/matrix

https://github.com/dragonfly-ai/color

https://github.com/dragonfly-ai/spatial

https://github.com/dragonfly-ai/img
