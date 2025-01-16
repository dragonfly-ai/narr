<b>ArrayOps Features</b>:<br />
<table style="">
<tr>
<td>Method Signature</td>
<td>

`ArrayOps`
<br/>(JVM / Native)
</td>
<td>

`js.ArrayOps`
<br />(js.Array)</td>
<td>

`narr.Extensions`
<br />(NArr)</td>
</tr>
<tr>
<td>

```scala
def width: Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def knownSize: Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def isEmpty: Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def nonEmpty: Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def head: T
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def last: T
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def headOption: Option[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def lastOption: Option[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def sizeCompare(otherSize: Int): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def lengthCompare(len: Int): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def sizeIs: Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def lengthIs: Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def slice(from: Int, until: Int): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def tail: Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def init: Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def tails: Iterator[Array[T]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def inits: Iterator[Array[T]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def take(n: Int): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def drop(n: Int): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def takeRight(n: Int): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def dropRight(n: Int): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def takeWhile(p: T => Boolean): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def dropWhile(p: T => Boolean): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def iterator: Iterator[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def stepper[S <: Stepper[_]](
  implicit shape: StepperShape[T, S]
): S with EfficientSplit
```
</td>
<td>✓</td>
<td>❌</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def grouped(width: Int): Iterator[Array[T]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def span(p: T => Boolean): (Array[T], Array[T])
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def splitAt(n: Int): (Array[T], Array[T])
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def partition(p: T => Boolean): (Array[T], Array[T])
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def partitionMap[A1: ClassTag, A2: ClassTag](
  f: T => Either[A1, A2]
): (Array[A1], Array[A2])
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def reverse: Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>
<tr>
<td>

```scala
def reverseIterator: Iterator[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def filter(p: T => Boolean): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def filterNot(p: T => Boolean): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def sorted[B >: T](implicit ord: Ordering[B]): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def sortWith(lt: (T, T) => Boolean): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def sortBy[B](f: T => B)(implicit ord: Ordering[B]): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def withFilter(p: T => Boolean): ArrayOps.WithFilter[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def indexOf(elem: T, from: Int = 0): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def indexWhere(p: T => Boolean, from: Int = 0): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def lastIndexOf(elem: T, end: Int = xs.length - 1): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def lastIndexWhere(p: T => Boolean, end: Int = xs.length - 1): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def find(p: T => Boolean): Option[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def exists(p: T => Boolean): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def forall(p: T => Boolean): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def foldLeft[B](z: B)(op: (B, T) => B): B
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def scanLeft[ B : ClassTag ](z: B)(op: (B, T) => B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def scan[B >: T : ClassTag](z: B)(op: (B, B) => B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def scanRight[ B : ClassTag ](z: B)(op: (T, B) => B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def foldRight[B](z: B)(op: (T, B) => B): B
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def fold[A1 >: T](z: A1)(op: (A1, A1) => A1): A1
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def map[B](f: T => B)(implicit ct: ClassTag[B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def mapInPlace(f: T => T): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def flatMap[B : ClassTag](
  f: T => IterableOnce[B]
): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def flatMap[BS, B](f: T => BS)(
  implicit asIterable: BS => Iterable[B], m: ClassTag[B]
): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def flatten[B](
  implicit asIterable: T => IterableOnce[B], m: ClassTag[B]
): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def collect[B: ClassTag](pf: PartialFunction[T, B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def collectFirst[B](pf: PartialFunction[T, B]): Option[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def zip[B](that: IterableOnce[B]): Array[(T, B)]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def lazyZip[B](that: Iterable[B]): LazyZip2[T, B, Array[T]]
```
</td>
<td>✓</td>
<td>❌</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def zipAll[A1 >: T, B](
  that: Iterable[B], thisElem: A1, thatElem: B
): Array[(A1, B)]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def zipWithIndex: Array[(T, Int)]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def appended[B >: T : ClassTag](x: B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def :+ [B >: T : ClassTag](x: B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def prepended[B >: T : ClassTag](x: B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def +: [B >: T : ClassTag](x: B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def prependedAll[B >: T : ClassTag](prefix: IterableOnce[B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def prependedAll[B >: T : ClassTag](prefix: Array[_ <: B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def ++: [B >: T : ClassTag](prefix: IterableOnce[B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def ++: [B >: T : ClassTag](prefix: Array[_ <: B]): Array[B]

```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def appendedAll[B >: T : ClassTag](suffix: IterableOnce[B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def appendedAll[B >: T : ClassTag](suffix: Array[_ <: B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def :++ [B >: T : ClassTag](suffix: IterableOnce[B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def :++ [B >: T : ClassTag](suffix: Array[_ <: B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def concat[B >: T : ClassTag](suffix: IterableOnce[B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def concat[B >: T : ClassTag](suffix: Array[_ <: B]): Array[B]
```
</td>
<td>✓</td>
<td>❌</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def ++[B >: T : ClassTag](xs: IterableOnce[B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def ++[B >: T : ClassTag](xs: Array[_ <: B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def contains(elem: T): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def patch[B >: T : ClassTag](
  from: Int, other: IterableOnce[B], replaced: Int
): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def unzip[A1, A2](
  implicit asPair: T => (A1, A2), ct1: ClassTag[A1], ct2: ClassTag[A2]
): (Array[A1], Array[A2])
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def unzip3[A1, A2, A3](
  implicit asTriple: T => (A1, A2, A3),
  ct1: ClassTag[A1],
  ct2: ClassTag[A2],
  ct3: ClassTag[A3]
): (Array[A1], Array[A2], Array[A3])
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def transpose[B](implicit asArray: T => Array[B]): Array[Array[B]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def foreach[U](f: T => U): Unit
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def distinct: Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def distinctBy[B](f: T => B): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def padTo[B >: T : ClassTag](len: Int, elem: B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def indices: Range
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def groupBy[K](f: T => K): immutable.Map[K, Array[T]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def groupMap[K, B : ClassTag](
  key: T => K
)(
  f: T => B): immutable.Map[K, Array[B]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def toSeq: immutable.Seq[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def toIndexedSeq: immutable.IndexedSeq[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def copyToArray[B >: T](xs: Array[B]): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def copyToArray[B >: T](xs: Array[B], start: Int): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def copyToArray[B >: T](xs: Array[B], start: Int, len: Int): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def toArray[B >: T: ClassTag]: Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def count(p: T => Boolean): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def startsWith[B >: T](that: Array[B]): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def startsWith[B >: T](that: Array[B], offset: Int): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def endsWith[B >: T](that: Array[B]): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def updated[B >: T : ClassTag](index: Int, elem: B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def view: IndexedSeqView[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def diff[B >: T](that: Seq[B]): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def intersect[B >: T](that: Seq[B]): Array[T]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def sliding(width: Int, step: Int = 1): Iterator[Array[T]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def combinations(n: Int): Iterator[Array[T]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def permutations: Iterator[Array[T]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def startsWith[B >: T](that: IterableOnce[B], offset: Int = 0): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def endsWith[B >: T](that: Iterable[B]): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
</table>