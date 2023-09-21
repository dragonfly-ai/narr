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
def size: Int
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
def head: A
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def last: A
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def headOption: Option[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def lastOption: Option[A]
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
def slice(from: Int, until: Int): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def tail: Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def init: Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def tails: Iterator[Array[A]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def inits: Iterator[Array[A]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def take(n: Int): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def drop(n: Int): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def takeRight(n: Int): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def dropRight(n: Int): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def takeWhile(p: A => Boolean): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def dropWhile(p: A => Boolean): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def iterator: Iterator[A]
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
  implicit shape: StepperShape[A, S]
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
def grouped(size: Int): Iterator[Array[A]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def span(p: A => Boolean): (Array[A], Array[A])
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def splitAt(n: Int): (Array[A], Array[A])
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def partition(p: A => Boolean): (Array[A], Array[A])
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def partitionMap[A1: ClassTag, A2: ClassTag](
  f: A => Either[A1, A2]
): (Array[A1], Array[A2])
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def reverse: Array[A]
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
def reverseIterator: Iterator[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def filter(p: A => Boolean): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def filterNot(p: A => Boolean): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def sorted[B >: A](implicit ord: Ordering[B]): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def sortWith(lt: (A, A) => Boolean): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def sortBy[B](f: A => B)(implicit ord: Ordering[B]): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def withFilter(p: A => Boolean): ArrayOps.WithFilter[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def indexOf(elem: A, from: Int = 0): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def indexWhere(p: A => Boolean, from: Int = 0): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def lastIndexOf(elem: A, end: Int = xs.length - 1): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def lastIndexWhere(p: A => Boolean, end: Int = xs.length - 1): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def find(p: A => Boolean): Option[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def exists(p: A => Boolean): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def forall(p: A => Boolean): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def foldLeft[B](z: B)(op: (B, A) => B): B
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def scanLeft[ B : ClassTag ](z: B)(op: (B, A) => B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def scan[B >: A : ClassTag](z: B)(op: (B, B) => B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def scanRight[ B : ClassTag ](z: B)(op: (A, B) => B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def foldRight[B](z: B)(op: (A, B) => B): B
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def fold[A1 >: A](z: A1)(op: (A1, A1) => A1): A1
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def map[B](f: A => B)(implicit ct: ClassTag[B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def mapInPlace(f: A => A): Array[A]
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
  f: A => IterableOnce[B]
): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def flatMap[BS, B](f: A => BS)(
  implicit asIterable: BS => Iterable[B], m: ClassTag[B]
): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def flatten[B](
  implicit asIterable: A => IterableOnce[B], m: ClassTag[B]
): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def collect[B: ClassTag](pf: PartialFunction[A, B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def collectFirst[B](pf: PartialFunction[A, B]): Option[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def zip[B](that: IterableOnce[B]): Array[(A, B)]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def lazyZip[B](that: Iterable[B]): LazyZip2[A, B, Array[A]]
```
</td>
<td>✓</td>
<td>❌</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def zipAll[A1 >: A, B](
  that: Iterable[B], thisElem: A1, thatElem: B
): Array[(A1, B)]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def zipWithIndex: Array[(A, Int)]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def appended[B >: A : ClassTag](x: B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def :+ [B >: A : ClassTag](x: B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def prepended[B >: A : ClassTag](x: B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def +: [B >: A : ClassTag](x: B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def prependedAll[B >: A : ClassTag](prefix: IterableOnce[B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def prependedAll[B >: A : ClassTag](prefix: Array[_ <: B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def ++: [B >: A : ClassTag](prefix: IterableOnce[B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def ++: [B >: A : ClassTag](prefix: Array[_ <: B]): Array[B]

```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def appendedAll[B >: A : ClassTag](suffix: IterableOnce[B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def appendedAll[B >: A : ClassTag](suffix: Array[_ <: B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def :++ [B >: A : ClassTag](suffix: IterableOnce[B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def :++ [B >: A : ClassTag](suffix: Array[_ <: B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def concat[B >: A : ClassTag](suffix: IterableOnce[B]): Array[B]
```
</td>
<td>✓</td>
<td>❌</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def concat[B >: A : ClassTag](suffix: Array[_ <: B]): Array[B]
```
</td>
<td>✓</td>
<td>❌</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def ++[B >: A : ClassTag](xs: IterableOnce[B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def ++[B >: A : ClassTag](xs: Array[_ <: B]): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def contains(elem: A): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def patch[B >: A : ClassTag](
  from: Int, other: IterableOnce[B], replaced: Int
): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def unzip[A1, A2](
  implicit asPair: A => (A1, A2), ct1: ClassTag[A1], ct2: ClassTag[A2]
): (Array[A1], Array[A2])
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def unzip3[A1, A2, A3](
  implicit asTriple: A => (A1, A2, A3),
  ct1: ClassTag[A1],
  ct2: ClassTag[A2],
  ct3: ClassTag[A3]
): (Array[A1], Array[A2], Array[A3])
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def transpose[B](implicit asArray: A => Array[B]): Array[Array[B]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def foreach[U](f: A => U): Unit
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def distinct: Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def distinctBy[B](f: A => B): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def padTo[B >: A : ClassTag](len: Int, elem: B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
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
def groupBy[K](f: A => K): immutable.Map[K, Array[A]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def groupMap[K, B : ClassTag](
  key: A => K
)(
  f: A => B): immutable.Map[K, Array[B]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def toSeq: immutable.Seq[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def toIndexedSeq: immutable.IndexedSeq[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def copyToArray[B >: A](xs: Array[B]): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def copyToArray[B >: A](xs: Array[B], start: Int): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def copyToArray[B >: A](xs: Array[B], start: Int, len: Int): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def toArray[B >: A: ClassTag]: Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def count(p: A => Boolean): Int
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def startsWith[B >: A](that: Array[B]): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def startsWith[B >: A](that: Array[B], offset: Int): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def endsWith[B >: A](that: Array[B]): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>✓</td>
</tr>
<tr>
<td>

```scala
def updated[B >: A : ClassTag](index: Int, elem: B): Array[B]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def view: IndexedSeqView[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def diff[B >: A](that: Seq[B]): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def intersect[B >: A](that: Seq[B]): Array[A]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def sliding(size: Int, step: Int = 1): Iterator[Array[A]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def combinations(n: Int): Iterator[Array[A]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def permutations: Iterator[Array[A]]
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def startsWith[B >: A](that: IterableOnce[B], offset: Int = 0): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
<td>

```scala
def endsWith[B >: A](that: Iterable[B]): Boolean
```
</td>
<td>✓</td>
<td>✓</td>
<td>❌</td>
</tr>
<tr>
</table>