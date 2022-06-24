# narr
pronounced: <b>(ˈnär, as in gnarly)</b> stands for: <b>Native Array</b><br />

&nbsp;&nbsp;&nbsp;This library provides Scala.js cross projects with an abstraction over features common to js.Array and scala.Array.  It also includes polyfills to imbue js.Array with features like tabulate and fill.

&nbsp;&nbsp;&nbsp;Using narr.ARRAY[T] instead of js.Array[T] and scala.Array[T] ensures that a project will always use the native Array type of the platform it compiles to.

<b>Advantages</b>:<br />
<ul>
<li>Performance!<br />&nbsp;&nbsp;&nbsp;When a Scala.js cross project uses native Array types internally it implicitly eliminates all possible boilerplate and takes advantage of each platform's fastest data structures.  While JVM Arrays inherit the speed and compactness of C/C++ style Arrays, JavaScript Arrays are a special case of JavaScript's Object type: the only data structure it has.  Likewise, making use of native Array types eliminates all performance penalties associated with conversions and wrappers at the boundary between a Scala.js library and native code that makes use of it.</li>
<li>Native Interoperability without Boilerplate<br />&nbsp;&nbsp;When writing a cross compiled Scala.js library with accessibility from native languages, developers have to pay attention to @JSExport annotations and make sure that every method or field of type Array has an analagous js.Array exposed in the JavaScript build.  By simply replacing all references to js.Array or Array with narr.ARRAY, the same methods and fields will interoperate seamlessly with code in either run time environment.</li>
</ul>

<b>Caveats</b>:
<ul>
<li>Type Safety concerns<br />&nbsp;&nbsp;&nbsp;Because the JVM Array has a reference to the type of its elements and the JavaScript Array has no concept of type at all, careless or malicious JavaScript developers could spike a js.Array with unexpected or dangerous values before passing it to an unsuspecting Scala.js method that takes parameters of type ARRAY[T].  Security concerns aside, if abused, these type system distinctions could cause unwanted run time differences between the behavior of JVM and JavaScript versions of a program.  However, using js.Array[T] explicitly doesn't solve these issues, either.  These concerns argue against js.Array in general as much as they argue against narr.ARRAY.
</li>
<li>Size Inconsistency and Mutability Concerns.<br />&nbsp;&nbsp;&nbsp;The JVM Array has a fixed, immutable length while the length of a JavaScript Array can change dynamically.  To mitigate all of the usual dangers involved with exposure to this kind of shared mutable state, users of narr should treat all instances of ARRAY as JVM arrays.  More specifically, restrict all interaction with instances of ARRAY[T] to only three operations:<br />

```scala
// assume: val a:ARRAY[Int]

a(0)       // read element
a(0) = 42  // write element
a.length   // read length
```
</li>
</ul>

<b>When to use narr</b>:
<table>
<tr>
    <td rowspan="2"><b>Native Array Dependency</b></td>    
    <td rowspan="2"><b>Convenience</b></td>
    <td colspan="2"><b>Performance</b></td>
    <td rowspan="2">Notes</td>
</tr>
<tr>
    <td><b>JS</b></td>
    <td><b>JVM</b></td>
</tr>
<tr>
    <td>No Arrays</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>☆☆☆</td>
    <td>narr offers no utility for projects that do not use Array[T] types.</td>
</tr>
<tr>
    <td>js.Array[T]</td>
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
    <td>Array[T] might not perform as optimally as the native js.Array[T] in JavaScript environments.</td>
</tr>
<tr>
    <td>js.Array[T] and Array[T]</td>
    <td>★★★</td>
    <td>★★★</td>
    <td>☆☆☆</td>
    <td>Seamless optimized interop with native code on both platforms without any conversions or wrappers.</td>
</tr>
</table>

narr has no impact on JVM performance, but it can dramatically speed up JavaScript by making use of the natively optimized data structure and eliminating conversions that tend to have O(n) run time complexities.  It also adds convenience methods for js.Array[T] such as fill and tabulate, bit mainly eliminates the need for specially crafted and maintained @JSExport methods and fields for JavaScript interop.

To use this library with SBT:

```scala
resolvers += "dragonfly.ai" at "https://code.dragonfly.ai/"
libraryDependencies += "ai.dragonfly.code" %%% "narr" % "0.01"
```

How to use narr:

```scala
import narr.ARRAY

// constructor call
val a1:ARRAY[String] = new ARRAY[String](5)

// literal
val a2:ARRAY[Int] = ARRAY[Int](2, 4, 8, 16)

// fill
val a3:ARRAY[Double] = ARRAY.fill[Double](10)(42)

// tabulate
val a4:ARRAY[Double] = ARRAY.tabulate[Double](42)(
  (i:Int) => i * Math.random()
)

// multi dimensional
val a2d:ARRAY[ARRAY[Double]] = ARRAY[ARRAY[Double]](
  ARRAY.tabulate[Double](5)( (i:Int) => i * Math.random() ),
  ARRAY.tabulate[Double](5)( (i:Int) => i * Math.random() ),
  ARRAY.tabulate[Double](5)( (i:Int) => i * Math.random() ),
  ARRAY.tabulate[Double](5)( (i:Int) => i * Math.random() )
)

// conveniently expose Scala.js libraries to native JavaScript and Java developers without any need for wrappers or conversions.
@JSExportTopLevel("copy")
def copy(a0:ARRAY[Double]): ARRAY[Double] = ARRAY.tabulate[Double](a0.length)(
  (i:Int) => a0(i)
)
```


Projects that rely on narr:

https://github.com/dragonfly-ai/bitfrost

https://github.com/dragonfly-ai/matrix

https://github.com/dragonfly-ai/color

https://github.com/dragonfly-ai/spatial

https://github.com/dragonfly-ai/img