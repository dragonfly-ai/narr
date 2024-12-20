/*
 * Copyright 2023 dragonfly.ai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package narr.native

import narr.*

import scala.collection.{IndexedSeqView, immutable, mutable}
import scala.scalajs.js
import scala.language.implicitConversions
import scala.math.Ordering
import scala.reflect.ClassTag

// to imbue js.Array with scala.Array semantics


object Extensions {

  /** Avoid an allocation in [[collect]]. */
  private val fallback: Any => Any = _ => fallback

  given orderingToCompareFunction[T]: Conversion[Ordering[T], js.Function2[T, T, Int]] with
    def apply(o: Ordering[T]): js.Function2[T, T, Int] = (a: T, b: T) => o.compare(a, b)

  extension (a: ByteArray) {
    inline def head: Byte = a(0)
    def sort(): ByteArray = sortByteArray(a)
    def sort(ord: Ordering[Byte]): ByteArray = sortByteArray(a, ord)

    def sorted: ByteArray = sorted(Ordering.Byte)
    def sorted(ord: Ordering[Byte]): ByteArray = sortByteArray(copy[Byte](a), ord)

    def unzip[A1, A2](using asPair: Byte => (A1, A2), ct1: ClassTag[A1], ct2: ClassTag[A2]): (NArray[A1], NArray[A2]) = {
      narr.native.NArray.unzip[Byte, A1, A2](a)
    }
  }

  extension (a: ShortArray) {
    inline def head: Short = a(0)
    def sort(): ShortArray = sortShortArray(a)
    def sort(ord: Ordering[Short]): ShortArray = sortShortArray(a, ord)

    def sorted: ShortArray = sorted(Ordering.Short)
    def sorted(ord: Ordering[Short]): ShortArray = sortShortArray(copy[Short](a), ord)

    def unzip[A1, A2](using asPair: Short => (A1, A2), ct1: ClassTag[A1], ct2: ClassTag[A2]): (NArray[A1], NArray[A2]) = {
      narr.native.NArray.unzip[Short, A1, A2](a)
    }
  }

  extension (a: IntArray) {
    def sort(): IntArray = sortIntArray(a)
    def sort(ord: Ordering[Int]): IntArray = sortIntArray(a, ord)

    def sorted:IntArray = sorted(Ordering.Int)
    def sorted(ord:Ordering[Int]): IntArray = sortIntArray(copy[Int](a), ord)

    def unzip[A1, A2](using asPair: Int => (A1, A2), ct1: ClassTag[A1], ct2: ClassTag[A2]): (NArray[A1], NArray[A2]) = {
      narr.native.NArray.unzip[Int, A1, A2](a)
    }
  }

  extension (a: FloatArray) {
    def sort(): FloatArray = sortFloatArray(a)
    //sort(Ordering.Float.TotalOrdering)
    def sort(ord: Ordering[Float]): FloatArray = sortFloatArray(a, ord)
    def sorted: FloatArray = sorted(Ordering.Float.TotalOrdering)
    def sorted(ord: Ordering[Float]): FloatArray = sortFloatArray(copy[Float](a), ord)

    def unzip[A1, A2](using asPair: Float => (A1, A2), ct1: ClassTag[A1], ct2: ClassTag[A2]): (NArray[A1], NArray[A2]) = {
      narr.native.NArray.unzip[Float, A1, A2](a)
    }
  }

  extension (a: DoubleArray) {
    def sort(): DoubleArray = sortDoubleArray(a)
    def sort(ord: Ordering[Double]): DoubleArray = sortDoubleArray(a, ord)
    def sorted: DoubleArray = sorted(Ordering.Double.TotalOrdering)
    def sorted(ord: Ordering[Double]): DoubleArray = sortDoubleArray(copy[Double](a), ord)

    def unzip[A1, A2](using asPair: Double => (A1, A2), ct1: ClassTag[A1], ct2: ClassTag[A2]): (NArray[A1], NArray[A2]) = {
      narr.native.NArray.unzip[Double, A1, A2](a)
    }
  }

  extension[T <: AnyRef | Boolean | Char | Long | Unit] (a:NArray[T]) {
    def sort(ord: Ordering[T]): NArray[T] = a.asInstanceOf[SortableNArr[T]].sort(ord).asInstanceOf[NArray[T]]
  }

  extension[T] (a:NArray[T]) {
    inline def copy: NArray[T] = {
      val temp = a.asInstanceOf[NArr[T]]
      temp.slice(0, temp.length).asInstanceOf[NArray[T]]
    }

    /** The width of this NArray.
     *
     * @return the number of elements in this NArray.
     */
    inline def size: Int = a.length


    /** The width of this NArray.
     *
     * @return the number of elements in this NArray.
     */
    inline def knownSize: Int = a.length

    /** Tests whether the NArray is empty.
     *
     * @return `true` if the NArray contains no elements, `false` otherwise.
     */
    inline def isEmpty: Boolean = a.length == 0

    /** Tests whether the NArray is not empty.
     *
     * @return `true` if the NArray contains at least one element, `false` otherwise.
     */
    inline def nonEmpty: Boolean = a.length != 0

    inline def head: T = a(0)

    inline def last: T = try {
      a(a.length - 1)
    } catch {
      case _: ArrayIndexOutOfBoundsException => throw new NoSuchElementException("last of empty array")
    }

    /** Optionally selects the first element.
     *
     * @return the first element of this NArray if it is nonempty,
     *         `None` if it is empty.
     */
    inline def headOption: Option[T] = if (isEmpty) None else Some(head)

    /** Optionally selects the last element.
     *
     * @return the last element of this NArray$ if it is nonempty,
     *         `None` if it is empty.
     */
    inline def lastOption: Option[T] = if (isEmpty) None else Some(last)

    /** Compares the width of this NArray to a test value.
     *
     * @param otherSize the test value that gets compared with the width.
     * @return A value `x` where
     * {{{
     *        x <  0       if this.width <  otherSize
     *        x == 0       if this.width == otherSize
     *        x >  0       if this.width >  otherSize
     *            }}}
     */
    inline def sizeCompare(otherSize: Int): Int = Integer.compare(a.length, otherSize)

    /** Compares the length of this NArray to a test value. *
     *   @param   len the test value that gets compared with the le h.
     *   @return A value `x`  re
     *   {{{
     *        x <  0       if this.length <  len
     *        x == 0       if this.length == len
     *        x >  0       if this.length >  len
     * }}}
     */
    inline def lengthCompare(len: Int): Int = Integer.compare(a.length, len)

    /** Method mirroring [[SeqOps.sizeIs]] for consistency, except it returns an `Int`
     * because `width` is known and comparison is constant-time.
     *
     * These operations are equivalent to [[sizeCompare(Int) `sizeCompare(Int)`]], and
     * allow the following more readable usages:
     *
     * {{{
     * this.sizeIs < width     // this.sizeCompare(width) < 0
     * this.sizeIs <= width    // this.sizeCompare(width) <= 0
     * this.sizeIs == width    // this.sizeCompare(width) == 0
     * this.sizeIs != width    // this.sizeCompare(width) != 0
     * this.sizeIs >= width    // this.sizeCompare(width) >= 0
     * this.sizeIs > width     // this.sizeCompare(width) > 0
     * }}}
     */
    inline def sizeIs: Int = a.length

    /** Method mirroring [[SeqOps.lengthIs]] for consistency, except it returns an `Int`
     * because `length` is known and comparison is constant-time.
     *
     * These operations are equivalent to [[lengthCompare(Int) `lengthCompare(Int)`]], and
     * allow the following more readable usages:
     *
     * {{{
     * this.lengthIs < len     // this.lengthCompare(len) < 0
     * this.lengthIs <= len    // this.lengthCompare(len) <= 0
     * this.lengthIs == len    // this.lengthCompare(len) == 0
     * this.lengthIs != len    // this.lengthCompare(len) != 0
     * this.lengthIs >= len    // this.lengthCompare(len) >= 0
     * this.lengthIs > len     // this.lengthCompare(len) > 0
     * }}}
     */
    inline def lengthIs: Int = a.length

    /** Selects an interval of elements. The returned array is made up
     * of all elements `x` which satisfy the invariant:
     * {{{
     *   from <= indexOf(x) < until
     * }}}
     *
     * @param from  the lowest index to include from this array.
     * @return an array containing the elements greater than or equal to
     *         index `from` extending through the end of this array.
     */
    inline def slice(from: Int): NArray[T] = slice(from, a.length)

    /** Selects an interval of elements. The returned array is made up
     * of all elements `x` which satisfy the invariant:
     * {{{
     *   from <= indexOf(x) < until
     * }}}
     *
     * @param from  the lowest index to include from this array.
     * @param until the lowest index to EXCLUDE from this array.
     * @return an array containing the elements greater than or equal to
     *         index `from` extending up to (but not including) index `until`
     *         of this array.
     */
    inline def slice(from: Int, until: Int): NArray[T] = {
      ((a.asInstanceOf[NArr[T]]).slice(from, until)).asInstanceOf[NArray[T]]
    }

    /** The rest of the NArray without its first element. */
    inline def tail: NArray[T] = slice(1, a.length)

    /** The initial part of the NArray without its last element. */
    inline def init: NArray[T] = slice(0, a.length - 1)

    /** An NArray containing the first `n` elements of this NArray. */
    inline def take(n: Int): NArray[T] = slice(0, n)

    /** The rest of the NArray without its `n` first elements. */
    inline def drop(n: Int): NArray[T] = slice(n, a.length)

    /** An NArray containing the last `n` elements of this NArray. */
    inline def takeRight(n: Int): NArray[T] = drop(a.length - Math.max(n, 0))

    /** The rest of the NArray without its `n` last elements. */
    inline def dropRight(n: Int): NArray[T] = take(a.length - Math.max(n, 0))

    /** Takes longest prefix of elements that satisfy a predicate.
     *
     * @param p The predicate used to test ele nts.
     * @return the longest prefix of this NArray whose elements all satisfy
     *         the predicate `p`.
     */
    inline def takeWhile(p: T => Boolean): NArray[T] = {
      var i: Int = 0
      while (i < a.length && p(a(i))) i += 1
      slice(0, i)
    }

    /** Drops longest prefix of elements that satisfy a predicate.
     *
     * @param p The predicate used to test ele nts.
     * @return the longest suffix of this NArray whose first element
     *         does not satisfy the predicate `p`.
     */
    inline def dropWhile(p: T => Boolean): NArray[T] = {
      var i: Int = 0
      while (i < a.length && p(a(i))) i += 1
      slice(i, a.length)
    }

    /**
     * @return an iterator for this AT
     */
    def iterator: Iterator[T] = new scala.collection.AbstractIterator[T] {
      var i: Int = -1
      override val knownSize: Int = a.length
      val end: Int = knownSize - 1

      def hasNext: Boolean = i < end

      def next(): T = {
        i += 1
        a(i).asInstanceOf[T]
      }
    }


    /** Returns a new array with the elements in reversed order. */
    def reverse: NArray[T] = {
      val len = a.length
      val res = slice(0, len)
      var i = 0
      while (i < len) {
        res(len - i - 1) = a(i)
        i += 1
      }
      res
    }

    /** An iterator yielding elements in reversed order.
     *
     * Note: `xs.reverseIterator` is the same as `xs.reverse.iterator` but implemented more efficiently.
     *
     * @return an iterator yielding the elements of this array in reversed order
     */
    def reverseIterator: Iterator[T] = new scala.collection.AbstractIterator[T] {
      private[this] var i: Int = a.length
      override val knownSize: Int = a.length
      val end: Int = 0

      def hasNext: Boolean = i > end

      def next(): T = {
        i -= 1
        a(i)
      }

      override def drop(n: Int): Iterator[T] = {
        if (n > 0) i = Math.max(-1, i - n)
        this
      }
    }

    def grouped(groupSize: Int): Iterator[NArray[T]] = new scala.collection.AbstractIterator[NArray[T]] {
      private[this] var i = 0

      def hasNext: Boolean = i < a.length

      def next(): NArray[T] = {
        if (i >= a.length) throw new NoSuchElementException
        val r = a.slice(i, i + groupSize)
        i += groupSize
        r
      }
    }

    /** Splits this array into a prefix/suffix pair according to a predicate.
     *
     * Note: `c span p`  is equivalent to (but more efficient than)
     * `(c takeWhile p, c dropWhile p)`, provided the evaluation of the
     * predicate `p` does not cause any side-effects.
     *
     * @param p the test predicate
     * @return a pair consisting of the longest prefix of this array whose
     *         elements all satisfy `p`, and the rest of this array.
     */
    def span(p: T => Boolean): (NArray[T], NArray[T]) = {
      val i = indexWhere(x => !p(x))
      val idx = if (i < 0) a.length else i
      (slice(0, idx), slice(idx, a.length))
    }


    /** Splits this array into two at a given position.
     * Note: `c splitAt n` is equivalent to `(c take n, c drop n)`.
     *
     * @param n the position at which to split.
     * @return a pair of arrays consisting of the first `n`
     *         elements of this array, and the other elements.
     */
    def splitAt(n: Int): (NArray[T], NArray[T]) = (take(n), drop(n))

    /** A pair of, first, all elements that satisfy predicate `p` and, second, all elements that do not. */
    def partition(p: T => Boolean)(using ClassTag[T], ClassTag[NArray[T]]): (NArray[T], NArray[T]) = {
      val nab1 = NArrayBuilder[T]()
      val nab2 = NArrayBuilder[T]()

      var i = 0; while (i < a.length) {
        val x = a(i)
        if (p(x)) nab1 += x else nab2 += x
        i += 1
      }
      (nab1.result, nab2.result)
    }

    /** Sorts this array according to a comparison function.
     *
     * The sort is stable. That is, elements that are equal (as determined by
     * `lt`) appear in the same order in the sorted sequence as in the original.
     *
     * @param lt the comparison function which tests whether
     *           its first argument precedes its second argument in
     *           the desired ordering.
     * @return an array consisting of the elements of this array
     *         sorted according to the comparison function `lt`.
     */
    def sortWith(lt: (T, T) => Boolean): NArray[T] = a.asInstanceOf[SortableNArr[T]].sort(
      orderingToCompareFunction.apply(Ordering.fromLessThan[T](lt))
    ).asInstanceOf[NArray[T]]

    /** Sorts this array according to the Ordering which results from transforming
     * an implicitly given Ordering with a transformation function.
     *
     * @see [[scala.math.Ordering]]
     * @param f   the transformation function mapping elements
     *            to some other domain `B`.
     * @param ord the ordering assumed on domain `B`.
     * @tparam B the target type of the transformation `f`, and the type where
     *           the ordering `ord` is defined.
     * @return an array consisting of the elements of this array
     *         sorted according to the ordering where `x < y` if
     *         `ord.lt(f(x), f(y))`.
     */
    def sortBy[B](f: T => B)(implicit ord: Ordering[B]): NArray[T] = {
      val temp = orderingToCompareFunction.apply(ord)
      a.asInstanceOf[SortableNArr[T]].sort(
        (t1:T, t2:T) => temp(f(t1), f(t2))
      ).asInstanceOf[NArray[T]]
    }

    /** Finds index of first occurrence of some value in this array.
     *
     * @param elem the element value to search for.
     * @return the index `>= from` of the first element of this array that is equal (as determined by `==`)
     *         to `elem`, or `-1`, if none exists.
     */
    inline def indexOf(elem: T): Int = indexOf(elem, 0)

    /** Finds index of first occurrence of some value in this array after or at some start index.
     *
     * @param elem the element value to search for.
     * @param from the start index
     * @return the index `>= from` of the first element of this array that is equal (as determined by `==`)
     *         to `elem`, or `-1`, if none exists.
     */
    def indexOf(elem: T, from: Int): Int = {
      var i = from
      while (i < a.length) {
        if (elem == a(i)) return i
        i += 1
      }
      -1
    }

    /** Finds index of the first element satisfying some predicate.
     *
     * @param p    the predicate used to test elements.
     * @return the index `>= 0` of the first element of this array that satisfies the predicate `p`,
     *         or `-1`, if none exists.
     */
    inline def indexWhere(p: T => Boolean): Int = indexWhere(p, 0)

    /** Finds index of the first element satisfying some predicate after or at some start index.
     *
     * @param p    the predicate used to test elements.
     * @param from the start index
     * @return the index `>= from` of the first element of this array that satisfies the predicate `p`,
     *         or `-1`, if none exists.
     */
    def indexWhere(p: T => Boolean, from: Int = 0):Int = {
      var i = from
      while (!p(a(i)) && i < a.length) i += 1
      if (i >= a.length) -1
      else i
    }


    /** Finds index of last occurrence of some value in this array.
     *
     * @param elem the element value to search for.
     * @return the index `<= length - 1` of the last element of this array that is equal (as determined by `==`)
     *         to `elem`, or `-1`, if none exists.
     */
    inline def lastIndexOf(elem: T): Int = lastIndexOf(elem, a.length - 1)

    /** Finds index of last occurrence of some value in this array before or at a given end index.
     *
     * @param elem the element value to search for.
     * @param end  the end index.
     * @return the index `<= end` of the last element of this array that is equal (as determined by `==`)
     *         to `elem`, or `-1`, if none exists.
     */
    def lastIndexOf(elem: T, end: Int): Int = {
      var i = Math.min(end, a.length - 1)
      while (i >= 0) {
        if (elem == a(i)) return i
        i -= 1
      }
      -1
    }

    /** Finds index of last element satisfying some predicate.
     *
     * @param p the predicate used to test elements.
     * @return the index of the last element of this array that satisfies the predicate `p`,
     *         or `-1`, if none exists.
     */
    inline def lastIndexWhere(p: T => Boolean): Int = lastIndexWhere(p, a.length - 1)

    /** Finds index of last element satisfying some predicate before or at given end index.
     *
     * @param p the predicate used to test elements.
     * @return the index `<= end` of the last element of this array that satisfies the predicate `p`,
     *         or `-1`, if none exists.
     */
    def lastIndexWhere(p: T => Boolean, end: Int): Int = {
      var i = Math.min(end, a.length - 1)
      while (i >= 0) {
        if (p(a(i))) return i
        i -= 1
      }
      -1
    }

    /** Finds the first element of the array satisfying a predicate, if any.
     *
     * @param p the predicate used to test elements.
     * @return an option value containing the first element in the array
     *         that satisfies `p`, or `None` if none exists.
     */
    def find(p: T => Boolean): Option[T] = {
      val idx = indexWhere(p)
      if (idx == -1) None else Some(a(idx))
    }

    /** Tests whether a predicate holds for at least one element of this array.
     *
     * @param p the predicate used to test elements.
     * @return `true` if the given predicate `p` is satisfied by at least one element of this array, otherwise `false`
     */
    def exists(p: T => Boolean): Boolean = indexWhere(p) >= 0

    /** Tests whether a predicate holds for all elements of this array.
     *
     * @param p the predicate used to test elements.
     * @return `true` if this array is empty or the given predicate `p`
     *         holds for all elements of this array, otherwise `false`.
     */
    def forall(p: T => Boolean): Boolean = {
      var i = 0
      while (i < a.length) {
        if (!p(a(i))) return false
        i += 1
      }
      true
    }


    /** Applies the given binary operator `op` to the given initial value `z` and
     * all elements of this array, going left to right. Returns the initial value
     * if this array is empty.
     *
     * If `x,,1,,`, `x,,2,,`, ..., `x,,n,,` are the elements of this array, the
     * result is `op( op( ... op( op(z, x,,1,,), x,,2,,) ... ), x,,n,,)`.
     *
     * @param z  An initial value.
     * @param op A binary operator.
     * @tparam B The result type of the binary operator.
     * @return The result of applying `op` to `z` and all elements of this array,
     *         going left to right. Returns `z` if this array is empty.
     */
    def foldLeft[B](z: B)(op: (B, T) => B): B = {
      var v: B = z
      var i = 0
      while (i < a.length) {
        v = op(v, a(i))
        i += 1
      }
      v
    }

    /** Produces an array containing cumulative results of applying the binary
     * operator going left to right.
     *
     * @param z  the start value.
     * @param op the binary operator.
     * @tparam B the result type of the binary operator.
     * @return array with intermediate values.
     *
     *         Example:
     * {{{
     *    Array(1, 2, 3, 4).scanLeft(0)(_ + _) == Array(0, 1, 3, 6, 10)
     *   }}}
     *
     */
    def scanLeft[ B : ClassTag ](z: B)(op: (B, T) => B): NArray[B] = {
      var v = z
      var i = 0
      val res = NArray.ofSize[B](a.length + 1)
      while(i < a.length) {
        res(i) = v
        v = op(v, a(i))
        i += 1
      }
      res(i) = v
      res
    }


    /** Computes a prefix scan of the elements of the array.
     *
     * Note: The neutral element `z` may be applied more than once.
     *
     * @tparam B element type of the resulting array
     * @param z  neutral element for the operator `op`
     * @param op the associative operator for the scan
     * @return a new array containing the prefix scan of the elements in this array
     */
    def scan[B >: T : ClassTag](z: B)(op: (B, B) => B): NArray[B] = scanLeft(z)(op)

    /** Produces an array containing cumulative results of applying the binary
     * operator going right to left.
     *
     * @param z  the start value.
     * @param op the binary operator.
     * @tparam B the result type of the binary operator.
     * @return array with intermediate values.
     *
     *         Example:
     * {{{
     *    Array(4, 3, 2, 1).scanRight(0)(_ + _) == Array(10, 6, 3, 1, 0)
     *   }}}
     *
     */
    def scanRight[ B : ClassTag ](z: B)(op: (T, B) => B): Array[B] = {
      var v = z
      var i = a.length - 1
      val res = new Array[B](a.length + 1)
      res(a.length) = z
      while(i >= 0) {
        v = op(a(i), v)
        res(i) = v
        i -= 1
      }
      res
    }

    /** Applies the given binary operator `op` to all elements of this array and
     * the given initial value `z`, going right to left. Returns the initial
     * value if this array is empty.
     *
     * If `x,,1,,`, `x,,2,,`, ..., `x,,n,,` are the elements of this array, the
     * result is `op(x,,1,,, op(x,,2,,, op( ... op(x,,n,,, z) ... )))`.
     *
     * @param z  An initial value.
     * @param op A binary operator.
     * @tparam B The result type of the binary operator.
     * @return The result of applying `op` to all elements of this array
     *         and `z`, going right to left. Returns `z` if this array
     *         is empty.
     */
    def foldRight[B](z: B)(op: (T, B) => B): B = {
      var v = z
      var i = a.length - 1
      while (i >= 0) {
        v = op(a(i), v)
        i -= 1
      }
      v
    }

    /** Alias for [[foldLeft]].
     *
     * The type parameter is more restrictive than for `foldLeft` to be
     * consistent with [[IterableOnceOps.fold]].
     *
     * @tparam A1 The type parameter for the binary operator, a supertype of `A`.
     * @param z  An initial value.
     * @param op A binary operator.
     * @return The result of applying `op` to `z` and all elements of this array,
     *         going left to right. Returns `z` if this string is empty.
     */
    def fold[A1 >: T](z: A1)(op: (A1, A1) => A1): A1 = foldLeft(z)(op)

    /** Builds a new array by applying a function to all elements of this array.
     *
     * @param f the function to apply to each element.
     * @tparam B the element type of the returned array.
     * @return a new array resulting from applying the given function
     *         `f` to each element of this array and collecting the results.
     */
    def map[B: ClassTag](f: T => B): NArray[B] = {
      val out:NArray[B] = NArray.ofSize[B](a.length)
      var i = 0; while (i < a.length) {
        out(i) = f(a(i))
        i = i + 1
      }
      out
    }

    /** Maps each element of this array to a new element of the same type.
     * This 'in place' operation overwrites the original data.
     *
     * @param f the function to apply to each element.
     * @return a reference to this array, after mapping took place.
     */

    def mapInPlace(f: T => T): NArray[T] = {
      var i = 0
      while (i < a.length) {
        a(i) = f(a(i))
        i = i + 1
      }
      a
    }

    /** Builds a new array by applying a function to all elements of this array
     * and using the elements of the resulting collections.
     *
     * @param f the function to apply to each element.
     * @tparam B the element type of the returned array.
     * @return a new array resulting from applying the given collection-valued function
     *         `f` to each element of this array and concatenating the results.
     */
    def flatMap[B: ClassTag](f: T => IterableOnce[B]): NArray[B] = {
      val b = NArrayBuilder[B]()
      var i = 0
      while (i < a.length) {
        b ++= f(a(i))
        i += 1
      }
      b.result
    }

    def flatMap[BS, B](f: T => BS)(using asIterable: BS => Iterable[B], m: ClassTag[B]): NArray[B] =
      flatMap[B](x => asIterable(f(x)))

    /** Flattens a two-dimensional array by concatenating all its rows
     * into a single array.
     *
     * @tparam B Type of row elements.
     * @param asIterable A function that converts elements of this array to rows - Iterables of type `B`.
     * @return An array obtained by concatenating rows of this array.
     */
    def flatten[B:ClassTag](using asIterable: T => IterableOnce[B]): NArray[B] = {
      val len = a.length
      var size = 0
      var i = 0
      while (i < len) {
        a(i) match {
          case it: IterableOnce[?] =>
            val k = it.knownSize
            if (k > 0) size += k
          case ba:ByteArray => size += ba.length
          case sa:ShortArray => size += sa.length
          case ia:IntArray => size += ia.length
          case fa:FloatArray => size += fa.length
          case da:DoubleArray => size += da.length
          case na: NativeArray[?] => size += na.length
          case _ =>
        }
        i += 1
      }
      val b = NArrayBuilder[B](size)
      i = 0
      while (i < len) {
        b ++= asIterable(a(i))
        i += 1
      }
      b.result
    }

    /** Builds a new array by applying a partial function to all elements of this array
     * on which the function is defined.
     *
     * @param pf the partial function which filters and maps the array.
     * @tparam B the element type of the returned array.
     * @return a new array resulting from applying the given partial function
     *         `pf` to each element on which it is defined and collecting the results.
     *         The order of the elements is preserved.
     */
    def collect[B: ClassTag](pf: PartialFunction[T, B]): NArray[B] = {
      val fallback: Any => Any = Any => Extensions.fallback
      val b = NArrayBuilder[B]()
      var i = 0
      while (i < a.length) {
        val v = pf.applyOrElse(a(i), fallback)
        if (v.asInstanceOf[AnyRef] ne fallback) b.addOne(v.asInstanceOf[B])
        i += 1
      }
      b.result
    }


    /** Finds the first element of the array for which the given partial function is defined, and applies the
     * partial function to it. */
    def collectFirst[B](pf: PartialFunction[T, B]): Option[B] = {
      val fallback: Any => Any = Extensions.fallback
      var i = 0
      while (i < a.length) {
        val v = pf.applyOrElse(a(i), fallback)
        if (v.asInstanceOf[AnyRef] ne fallback) return Some(v.asInstanceOf[B])
        i += 1
      }
      None
    }


    /** Returns an array formed from this array and another iterable collection
     * by combining corresponding elements in pairs.
     * If one of the two collections is longer than the other, its remaining elements are ignored.
     *
     * @param that The iterable providing the second half of each result pair
     * @tparam B the type of the second half of the returned pairs
     * @return a new array containing pairs consisting of corresponding elements of this array and `that`.
     *         The length of the returned array is the minimum of the lengths of this array and `that`.
     */
    def zip[B](that: IterableOnce[B]): NArray[(T, B)] = {
      val k = that.knownSize
      val b = NArrayBuilder.builderFor[(T, B)]( if (k >= 0) Math.min(k, a.length) else a.length )

      var i = 0
      val it = that.iterator
      while (i < a.length && it.hasNext) {
        b += ((a(i), it.next()))
        i += 1
      }
      b.result
    }


    /** Returns an array formed from this array and another iterable collection
     * by combining corresponding elements in pairs.
     * If one of the two collections is shorter than the other,
     * placeholder elements are used to extend the shorter collection to the length of the longer.
     *
     * @param that     the iterable providing the second half of each result pair
     * @param thisElem the element to be used to fill up the result if this array is shorter than `that`.
     * @param thatElem the element to be used to fill up the result if `that` is shorter than this array.
     * @return a new array containing pairs consisting of corresponding elements of this array and `that`.
     *         The length of the returned array is the maximum of the lengths of this array and `that`.
     *         If this array is shorter than `that`, `thisElem` values are used to pad the result.
     *         If `that` is shorter than this array, `thatElem` values are used to pad the result.
     */
    def zipAll[A1 >: T, B](that: Iterable[B], thisElem: A1, thatElem: B): NArray[(A1, B)] = {
      val b = NArrayBuilder.builderFor[(A1, B)](Math.max(that.knownSize, a.length))
      var i = 0
      val it = that.iterator
      while (i < a.length && it.hasNext) {
        b += ((a(i), it.next()))
        i += 1
      }
      while (it.hasNext) {
        b += ((thisElem, it.next()))
        i += 1
      }
      while (i < a.length) {
        b += ((a(i), thatElem))
        i += 1
      }
      b.result
    }

    /** Zips this array with its indices.
     *
     * @return A new array containing pairs consisting of all elements of this array paired with their index.
     *         Indices start at `0`.
     */
    def zipWithIndex: NArray[(T, Int)] = NArray.tabulate[(T, Int)](a.length)(
      (i:Int) => ((a(i), i))
    )

    /** A copy of this array with an element appended. */
    def appended[B >: T : ClassTag](x: B): NArray[B] = {
      val dest = NArray.ofSize[B](a.length + 1)
      dest(a.length) = x
      NArray.copy(a.asInstanceOf[NArray[B]], 0, dest, 0, a.length)
      dest
    }


    inline def :+[B >: T : ClassTag](x: B): NArray[B] = appended(x)

    /** A copy of this array with an element prepended. */
    def prepended[B >: T : ClassTag](x: B): NArray[B] = {
      val dest = NArray.ofSize[B](a.length + 1)
      dest(0) = x
      NArray.copy(a.asInstanceOf[NArray[B]], 0, dest, 1, a.length)
      dest
    }

    inline def +:[B >: T : ClassTag](x: B): NArray[B] = prepended(x)

    /** A copy of this array with all elements of a collection prepended. */
    def prependedAll[B >: T : ClassTag](prefix: IterableOnce[B]): NArray[B] = {
      val k = prefix.knownSize
      val b = NArrayBuilder[B]((if (k >= 0) k else 0) + a.length)
      b.addAll(prefix)
      b.addAll(a.asInstanceOf[NArray[B]])
      b.result
    }

    /** A copy of this array with all elements of an array prepended. */
    def prependedAll[B >: T : ClassTag](prefix: NArray[B] ): NArray[B] = {
      val out = NArray.ofSize[B](prefix.length + a.length)
      NArray.copy[B](prefix, out, 0)
      NArray.copy[B](a.asInstanceOf[NArray[B]], out, prefix.length)
      out
    }

    inline def ++:[B >: T : ClassTag](prefix: IterableOnce[B]): NArray[B] = prependedAll(prefix)

    inline def ++:[B >: T : ClassTag](prefix: NArray[B] ): NArray[B] = prependedAll[B](prefix)

    /** A copy of this array with all elements of a collection appended. */
    def appendedAll[B >: T : ClassTag](suffix: IterableOnce[B]): NArray[B] = {
      val nab = NArrayBuilder[B]()
      nab.addAll(a.asInstanceOf[NArray[B]])
      val itr = suffix.iterator
      while (itr.hasNext) nab += itr.next()
      nab.result
    }

    /** A copy of this array with all elements of an array appended. */
    def appendedAll[B >: T : ClassTag](suffix: NArray[B]): NArray[B] = {
      val out = NArray.ofSize[B](a.length + suffix.length)
      NArray.copy[B](a.asInstanceOf[NArray[B]], out, 0)
      NArray.copy[B](suffix, out, a.length)
      out
    }

    inline def :++ [B >: T : ClassTag](suffix: IterableOnce[B]): NArray[B] = appendedAll(suffix)

    inline def :++ [B >: T : ClassTag](suffix: NArray[B]): NArray[B] = appendedAll[B](suffix)

    inline def concat[B >: T : ClassTag](suffix: IterableOnce[B]): NArray[B] = appendedAll(suffix)

    inline def concat[B >: T : ClassTag](suffix: NArray[B]): NArray[B] = appendedAll[B](suffix)

    inline def ++[B >: T : ClassTag](xs: IterableOnce[B]): NArray[B] = appendedAll(xs)

    inline def ++[B >: T : ClassTag](suffix: NArray[B]): NArray[B] = appendedAll[B](suffix)

    /** Tests whether this array contains a given value as an element.
     *
     * @param elem the element to test.
     * @return `true` if this array has an element that is equal (as
     *         determined by `==`) to `elem`, `false` otherwise.
     */
    def contains(elem: T): Boolean = {
      var i:Int = 0; while (i < a.length) {
        if (a(i) == elem) return true
        i += 1
      }
      false
    }


    /** Returns a copy of this array with patched values.
     * Patching at negative indices is the same as patching starting at 0.
     * Patching at indices at or larger than the length of the original array appends the patch to the end.
     * If more values are replaced than actually exist, the excess is ignored.
     *
     * @param from     The start index from which to patch
     * @param other    The patch values
     * @param replaced The number of values in the original array that are replaced by the patch.
     */
    def patch[B >: T : ClassTag](from: Int, other: IterableOnce[B], replaced: Int): NArray[B] = {
      val k = other.knownSize
      val r = if (replaced < 0) 0 else replaced
      val b = NArrayBuilder[B](if (k >= 0) a.length + k - r else 0)
      val chunk1 = if (from > 0) Math.min(from, a.length) else 0
      if (chunk1 > 0) b.addAll(a.asInstanceOf[NArray[B]], 0, chunk1)
      b.addAll(other)
      val remaining = a.length - chunk1 - r
      if (remaining > 0) b.addAll(a.asInstanceOf[NArray[B]], a.length - remaining, remaining)
      b.result
    }

    /** Converts an array of pairs into an array of first elements and an array of second elements.
     *
     * @tparam A1 the type of the first half of the element pairs
     * @tparam A2 the type of the second half of the element pairs
     * @param asPair an implicit conversion which asserts that the element type
     *               of this Array is a pair.
     * @param ct1    a class tag for `A1` type parameter that is required to create an instance
     *               of `Array[A1]`
     * @param ct2    a class tag for `A2` type parameter that is required to create an instance
     *               of `Array[A2]`
     * @return a pair of Arrays, containing, respectively, the first and second half
     *         of each element pair of this Array.
     */
    def unzip[A1, A2](using asPair: T => (A1, A2), ct1: ClassTag[A1], ct2: ClassTag[A2]): (NArray[A1], NArray[A2]) = {
      narr.native.NArray.unzip[T, A1, A2](a)
    }

    /** Converts an array of triples into three arrays, one containing the elements from each position of the triple.
     *
     * @tparam A1 the type of the first of three elements in the triple
     * @tparam A2 the type of the second of three elements in the triple
     * @tparam A3 the type of the third of three elements in the triple
     * @param asTriple an implicit conversion which asserts that the element type
     *                 of this Array is a triple.
     * @param ct1      a class tag for T1 type parameter that is required to create an instance
     *                 of Array[T1]
     * @param ct2      a class tag for T2 type parameter that is required to create an instance
     *                 of Array[T2]
     * @param ct3      a class tag for T3 type parameter that is required to create an instance
     *                 of Array[T3]
     * @return a triple of Arrays, containing, respectively, the first, second, and third
     *         elements from each element triple of this Array.
     */
    def unzip3[A1, A2, A3](implicit asTriple: T => (A1, A2, A3), ct1: ClassTag[A1], ct2: ClassTag[A2],
                           ct3: ClassTag[A3]): (NArray[A1], NArray[A2], NArray[A3]) = {
      val a1 = NArray.ofSize[A1](a.length)
      val a2 = NArray.ofSize[A2](a.length)
      val a3 = NArray.ofSize[A3](a.length)
      var i = 0
      while (i < a.length) {
        val e = asTriple(a(i))
        a1(i) = e._1
        a2(i) = e._2
        a3(i) = e._3
        i += 1
      }
      (a1, a2, a3)
    }

    /** Transposes a two dimensional array.
     *
     * @tparam B Type of row elements.
     * @param asArray A function that converts elements of this array to rows - arrays of type `B`.
     * @return An array obtained by replacing elements of this arrays with rows the represent.
     */
    def transpose[B](using asArray: T => NArray[B]): NArray[NArray[B]] = {
      val aClass = a.getClass.getComponentType
      val bb = NArrayBuilder.builderFor[NArray[B]]()
      if (a.length == 0) bb.result
      else {
        def mkRowBuilder() = NArrayBuilder.builderFor[B]()

        val bs = asArray(a(0)).map((x: B) => mkRowBuilder())

        for (xs <- a) {
          var i = 0
          for (x <- asArray(xs)) {
            bs(i) += x
            i += 1
          }
        }
        for (b <- bs) bb += b.result
        bb.result
      }
    }

    /** Apply `f` to each element for side effects.
     * Note: [U] parameter needed to help scalac's type inference.
     */
    def foreach[U](f: T => U): Unit = {
      var i: Int = 0
      while (i < a.length) {
        f(a(i))
        i = i + 1
      }
    }
    /** Selects all the elements of this array ignoring the duplicates.
     *
     * @return a new array consisting of all the elements of this array without duplicates.
     */
    def distinct: NArray[T] = distinctBy(identity)

    /** Selects all the elements of this array ignoring the duplicates as determined by `==` after applying
     * the transforming function `f`.
     *
     * @param f The transforming function whose result is used to determine the uniqueness of each element
     * @tparam B the type of the elements after being transformed by `f`
     * @return a new array consisting of all the elements of this array without duplicates.
     */
    def distinctBy[B](f: T => B): NArray[T] =
      NArrayBuilder.builderFor[T]().addAll(iterator.distinctBy(f)).result

    /** A copy of this array with an element value appended until a given target length is reached.
     *
     * @param len  the target length
     * @param elem the padding value
     * @tparam B the element type of the returned array.
     * @return a new array consisting of
     *         all elements of this array followed by the minimal number of occurrences of `elem` so
     *         that the resulting collection has a length of at least `len`.
     *         that the resulting collection has a length of at least `len`.
     */
    def padTo[B >: T : ClassTag](len: Int, elem: B): NArray[B] = {
      var i = a.length
      val newlen = Math.max(i, len)
      val dest = NArray.copyAs[T, B](a, newlen)
      while (i < newlen) {
        dest(i) = elem
        i += 1
      }
      dest
    }

    /** Produces the range of all indices of this sequence.
     *
     * @return a `Range` value from `0` to one less than the length of this array.
     */
    inline def indices: Range = Range(0, a.length)

    /** Partitions this array into a map of arrays according to some discriminator function.
     *
     * @param f the discriminator function.
     * @tparam K the type of keys returned by the discriminator function.
     * @return A map from keys to arrays such that the following invariant holds:
     * {{{
     *                 (xs groupBy f)(k) = xs filter (x => f(x) == k)
     *                }}}
     *               That is, every key `k` is bound to an array of those elements `x`
     *               for which `f(x)` equals `k`.
     */
    def groupBy[K](f: T => K): immutable.Map[K, NArray[T]] = {
      val m = mutable.Map.empty[K, NArrayBuilder[T]]
      val len = a.length
      var i = 0
      while(i < len) {
        val elem = a(i)
        val key = f(elem)
        val bldr = m.getOrElseUpdate(key, NArrayBuilder.builderFor[T]())
        bldr += elem
        i += 1
      }
      m.view.mapValues(_.result).toMap
    }

    /**
     * Partitions this array into a map of arrays according to a discriminator function `key`.
     * Each element in a group is transformed into a value of type `B` using the `value` function.
     *
     * It is equivalent to `groupBy(key).mapValues(_.map(f))`, but more efficient.
     *
     * {{{
     *   case class User(name: String, age: Int)
     *
     *   def namesByAge(users: Array[User]): Map[Int, Array[String]] =
     *     users.groupMap(_.age)(_.name)
     * }}}
     *
     * @param key the discriminator function
     * @param f   the element transformation function
     * @tparam K the type of keys returned by the discriminator function
     * @tparam B the type of values returned by the transformation function
     */
    def groupMap[K, B: ClassTag](key: T => K)(f: T => B): immutable.Map[K, NArray[B]] = {
      val m = mutable.Map.empty[K, NArrayBuilder[B]]
      val len = a.length
      var i = 0
      while (i < len) {
        val elem = a(i)
        val k = key(elem)
        val bldr = m.getOrElseUpdate(k, NArrayBuilder[B]())
        bldr += f(elem)
        i += 1
      }
      m.view.mapValues(_.result).toMap
    }

    inline def toSeq: Seq[T] = toIndexedSeq

    def toIndexedSeq: IndexedSeq[T] = {
      //scala.collection.mutable.IndexedSeq.tabulate(a.length)((i:Int) => a(i)).toIndexedSeq
      a.asInstanceOf[scala.scalajs.js.Array[T]].toIndexedSeq
    }

    /** Copy elements of this array to another array.
     * Fills the given array `xs` starting at index 0.
     * Copying will stop once either all the elements of this array have been copied,
     * or the end of the array is reached.
     *
     * @param xs the array to fill.
     * @tparam B the type of the elements of the array.
     */
    def copyToNArray[B >: T](xs: NArray[B]): Int = copyToNArray(xs, 0)

    /** Copy elements of this array to another array.
     * Fills the given array `xs` starting at index `start`.
     * Copying will stop once either all the elements of this array have been copied,
     * or the end of the array is reached.
     *
     * @param xs    the array to fill.
     * @param start the starting index within the destination array.
     * @tparam B the type of the elements of the array.
     */
    def copyToNArray[B >: T](xs: NArray[B], start: Int): Int = copyToNArray(xs, start, Int.MaxValue)

    /** Copy elements of this array to another array.
     * Fills the given array `xs` starting at index `start` with at most `len` values.
     * Copying will stop once either all the elements of this array have been copied,
     * or the end of the array is reached, or `len` elements have been copied.
     *
     * @param xs    the array to fill.
     * @param start the starting index within the destination array.
     * @param len   the maximal number of elements to copy.
     * @tparam B the type of the elements of the array.
     */
    def copyToNArray[B >: T](xs: NArray[B], start: Int, len: Int): Int = {
      //val copied = scala.collection.IterableOnce.elemsToCopyToArray(a.length, xs.length, start, len)
      val copied = Math.max(Math.min(Math.min(len, a.length), xs.length - start), 0)
      if (copied > 0) {
        NArray.copy[B](a.asInstanceOf[NArray[B]], 0, xs, start, copied)
      }
      copied
    }

    /** Create a JVM/Native style copy of this array with the specified element type. */
    def toArray[B >: T : ClassTag]: Array[B] = {
      val r = new Array[B](a.length)
      var i = 0; while (i < a.length) {
        r(i) = a(i)
        i += 1
      }
      r
    }

    /** Create a copy of this array with the specified element type. */
    def toNArray[B >: T : ClassTag]: NArray[B] = {
      val destination = NArray.ofSize[B](a.length)
      @annotation.unused val copied = copyToNArray[B](destination, 0)
      //assert(copied == xs.length)
      destination
    }

    /** Counts the number of elements in this array which satisfy a predicate */
    def count(p: T => Boolean): Int = {
      var i, res = 0
      val len = a.length
      while (i < len) {
        if (p(a(i))) res += 1
        i += 1
      }
      res
    }


    /** Tests whether this array starts with the given array. */
    inline def startsWith[B >: T](that: NArray[B]): Boolean = startsWith(that, 0)

    /** Tests whether this array contains the given array at a given index.
     *
     * @param that   the array to test
     * @param offset the index where the array is searched.
     * @return `true` if the array `that` is contained in this array at
     *         index `offset`, otherwise `false`.
     */
    def startsWith[B >: T](that: NArray[B], offset: Int): Boolean = {
      val safeOffset = offset.max(0)
      val thatl = that.length
      if (thatl > a.length - safeOffset) thatl == 0
      else {
        var i = 0
        while (i < thatl) {
          if (a(i + safeOffset) != that(i)) return false
          i += 1
        }
        true
      }
    }


    /** Tests whether this array ends with the given array.
     *
     * @param that the array to test
     * @return `true` if this array has `that` as a suffix, `false` otherwise.
     */
    def endsWith[B >: T](that: NArray[B]): Boolean = {
      val thatl = that.length
      val off = a.length - thatl
      if (off < 0) false
      else {
        var i = 0
        while (i < thatl) {
          if (a(i + off) != that(i)) return false
          i += 1
        }
        true
      }
    }


    /** A copy of this array with one single replaced element.
     *
     * @param index the position of the replacement
     * @param elem  the replacing element
     * @return a new array which is a copy of this array with the element at position `index` replaced by `elem`.
     * @throws IndexOutOfBoundsException if `index` does not satisfy `0 <= index < length`.
     */
    def updated[B >: T : ClassTag](index: Int, elem: B): NArray[B] = {
      if (index < 0 || index >= a.length) throw new ArrayIndexOutOfBoundsException(s"$index is out of bounds (min 0, max ${a.length - 1})")
      val dest = toNArray[B]
      dest(index) = elem
      dest
    }

    inline def view: IndexedSeqView[T] = new NArrayView[T](a)

    private def occCounts[B](sq: Seq[B]): mutable.Map[B, Int] = {
      val occ = new mutable.HashMap[B, Int]()
      for (y <- sq) occ.updateWith(y) {
        case None => Some(1)
        case Some(n) => Some(n + 1)
      }
      occ
    }

    /** Computes the multiset difference between this array and another sequence.
     *
     * @param that the sequence of elements to remove
     * @return a new array which contains all elements of this array
     *         except some of occurrences of elements that also appear in `that`.
     *         If an element value `x` appears
     *         ''n'' times in `that`, then the first ''n'' occurrences of `x` will not form
     *         part of the result, but any following occurrences will.
     */
    def diff[B >: T](that: Seq[B]): NArray[T] = {
      if (isEmpty || that.isEmpty) a.copy
      else {
        val occ = occCounts(that)
        val b = NArrayBuilder.builderFor[T]()
        var i = 0
        while (i < a.length) {
          val x = a(i)
          occ.updateWith(x) {
            case None => {
              b.addOne(x)
              None
            }
            case Some(1) => None
            case Some(n) => Some(n - 1)
          }
          i = i + 1
        }
        b.result
      }
    }

    /** Computes the multiset intersection between this array and another sequence.
     *
     * @param that the sequence of elements to intersect with.
     * @return a new array which contains all elements of this array
     *         which also appear in `that`.
     *         If an element value `x` appears
     *         ''n'' times in `that`, then the first ''n'' occurrences of `x` will be retained
     *         in the result, but any following occurrences will be omitted.
     */
    def intersect[B >: T](that: Seq[B]): NArray[T] = {
      if (isEmpty || that.isEmpty) a.slice(0, 0)
      else {
        val occ = occCounts(that)
        val b = NArrayBuilder.builderFor[T]()
        var i = 0
        while (i < a.length) {
          val x = a(i)
          occ.updateWith(x) {
            case None => None
            case Some(n) => {
              b.addOne(x)
              if (n == 1) None else Some(n - 1)
            }
          }
          i = i + 1
        }
        b.result
      }
    }

    /** Groups elements in fixed width blocks by passing a "sliding window"
     * over them (as opposed to partitioning them, as is done in grouped.)
     *
     * @see [[scala.collection.Iterator]], method `sliding`
     * @param width the number of elements per group
     * @param step the distance between the first elements of successive groups
     * @return An iterator producing arrays of width `width`, except the
     *         last element (which may be the only element) will be truncated
     *         if there are fewer than `width` elements remaining to be grouped.
     */
    def sliding(width: Int, step: Int = 1): Iterator[NArray[T]] = new Iterator[NArray[T]] {
      var i = 0
      var c = 0

      override def hasNext: Boolean = i < a.length && c < a.length

      override def next(): NArray[T] = {
        if (hasNext) {
          c = i + width
          val r = a.slice(i, c)
          i = i + step
          r
        } else throw new NoSuchElementException("next on empty iterator")
      }
    }

  }
}