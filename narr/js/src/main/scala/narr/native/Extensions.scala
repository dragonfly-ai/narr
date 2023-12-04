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

import scala.scalajs.js
import scala.language.implicitConversions
import scala.math.Ordering
import scala.reflect.ClassTag

// to imbue js.Array with scala.Array semantics


object Extensions {

  given orderingToCompareFunction[T]: Conversion[Ordering[T], js.Function2[T, T, Int]] with
    def apply(o: Ordering[T]): js.Function2[T, T, Int] = (a: T, b: T) => o.compare(a, b)

  extension (a: ByteArray) {
    inline def head: Byte = a(0)
    def sort(): ByteArray = sortByteArray(a)
    def sort(ord: Ordering[Byte]): ByteArray = sortByteArray(a, ord)

    def sorted: ByteArray = sorted(Ordering.Byte)
    def sorted(ord: Ordering[Byte]): ByteArray = sortByteArray(copy[Byte](a), ord)

//    inline def concat(suffix: IterableOnce[Byte]): ByteArray = {
//      concat(a, scala.scalajs.js.typedarray.Int8Array.from(suffix.asInstanceOf[Iterable[Byte]]))
//    }

  }

  extension (a: ShortArray) {
    inline def head: Short = a(0)
    def sort(): ShortArray = sortShortArray(a)
    def sort(ord: Ordering[Short]): ShortArray = sortShortArray(a, ord)

    def sorted: ShortArray = sorted(Ordering.Short)
    def sorted(ord: Ordering[Short]): ShortArray = sortShortArray(copy[Short](a), ord)

//    inline def concat(suffix: IterableOnce[Short]): ShortArray = {
//      concat(a, scala.scalajs.js.typedarray.Int16Array.from(suffix.asInstanceOf[Iterable[Short]]))
//    }
  }

  extension (a: IntArray) {
    def sort(): IntArray = sortIntArray(a)
    def sort(ord: Ordering[Int]): IntArray = sortIntArray(a, ord)

    def sorted:IntArray = sorted(Ordering.Int)
    def sorted(ord:Ordering[Int]): IntArray = sortIntArray(copy[Int](a), ord)

//    inline def concat(suffix: IterableOnce[Int]): IntArray = {
//      concat(a, scala.scalajs.js.typedarray.Int32Array.from(suffix.asInstanceOf[Iterable[Int]]))
//    }
  }

  extension (a: FloatArray) {
    def sort(): FloatArray = sortFloatArray(a)
    //sort(Ordering.Float.TotalOrdering)
    def sort(ord: Ordering[Float]): FloatArray = sortFloatArray(a, ord)
    def sorted: FloatArray = sorted(Ordering.Float.TotalOrdering)
    def sorted(ord: Ordering[Float]): FloatArray = sortFloatArray(copy[Float](a), ord)

//    inline def concat(suffix: IterableOnce[Float]): FloatArray = {
//      concat(a, scala.scalajs.js.typedarray.Float32Array.from(suffix.asInstanceOf[Iterable[Float]]))
//    }
  }

  extension (a: DoubleArray) {
    def sort(): DoubleArray = sortDoubleArray(a)
    def sort(ord: Ordering[Double]): DoubleArray = sortDoubleArray(a, ord)
    def sorted: DoubleArray = sorted(Ordering.Double.TotalOrdering)
    def sorted(ord: Ordering[Double]): DoubleArray = sortDoubleArray(copy[Double](a), ord)

//    inline def concat(suffix: IterableOnce[Double]): DoubleArray = {
//      concat(a, scala.scalajs.js.typedarray.Float64Array.from(suffix.asInstanceOf[Iterable[Double]]))
//    }
  }

  extension[T] (a:NArray[T]) {
    inline def copy: NArray[T] = {
      var temp = a.asInstanceOf[NArr[T]]
      temp.slice(0, temp.length).asInstanceOf[NArray[T]]
    }


    /** The size of this NArray.
     *
     * @return the number of elements in this NArray.
     */
    inline def size: Int = a.length


    /** The size of this NArray.
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

    /** Compares the size of this NArray to a test value.
     *
     * @param otherSize the test value that gets compared with the size.
     * @return A value `x` where
     * {{{
     *        x <  0       if this.size <  otherSize
     *        x == 0       if this.size == otherSize
     *        x >  0       if this.size >  otherSize
     *            }}}
     */
    inline def sizeCompare(otherSize: Int): Int = Integer.compare(a.length, otherSize)

    /** Compares the length of this NArray to a test value. *
     *   @p m   len the test value that gets compared with the le h.
     *   @ret n A value `x`  re
     *   {{{
     *        x <  0       if this.length <  len
     *        x == 0       if this.length == len
     *        x >  0       if this.length >  len
     * }}}
     */
    inline def lengthCompare(len: Int): Int = Integer.compare(a.length, len)

    /** Method mirroring [[SeqOps.sizeIs]] for consistency, except it returns an `Int`
     * because `size` is known and comparison is constant-time.
     *
     * These operations are equivalent to [[sizeCompare(Int) `sizeCompare(Int)`]], and
     * allow the following more readable usages:
     *
     * {{{
     * this.sizeIs < size     // this.sizeCompare(size) < 0
     * this.sizeIs <= size    // this.sizeCompare(size) <= 0
     * this.sizeIs == size    // this.sizeCompare(size) == 0
     * this.sizeIs != size    // this.sizeCompare(size) != 0
     * this.sizeIs >= size    // this.sizeCompare(size) >= 0
     * this.sizeIs > size     // this.sizeCompare(size) > 0
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
      a.asInstanceOf[NArr[T]].slice(from, until).asInstanceOf[NArray[T]]
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
    inline def iterator: Iterator[T] = new scala.collection.AbstractIterator[T] {
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
    inline def reverseIterator: Iterator[T] = new scala.collection.AbstractIterator[T] {
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

//    /** A copy of this array with all elements of a collection appended. */
//    def appendedAll[B >: A : ClassTag](suffix: IterableOnce[B]): NArray[B] = {
//      val b = ArrayBuilder.make[B]
//      val k = suffix.knownSize
//      if (k >= 0) b.sizeHint(k + xs.length)
//      b.addAll(xs)
//      b.addAll(suffix)
//      b.result()
//    }

    /** Zips this array with its indices.
     *
     * @return A new array containing pairs consisting of all elements of this array paired with their index.
     *         Indices start at `0`.
     */
    def zipWithIndex: NArray[(T, Int)] = NArray.tabulate[(T, Int)](a.length)(
      (i:Int) => ((a(i), i))
    )

    /** A copy of this array with all elements of an array appended. */
    inline def appendedAll[B >: T : ClassTag](suffix: NArray[B]): NArray[B] = {
      a.asInstanceOf[NArr[T]].concat(suffix).asInstanceOf[NArray[B]]
    }

    inline def :++ [B >: T : ClassTag](suffix: NArray[B]): NArray[B] = appendedAll(suffix)

    inline def concat[B >: T : ClassTag](suffix: NArray[B]): NArray[B] = appendedAll(suffix)

    inline def ++[B >: T : ClassTag](xs: NArray[B]): NArray[B] = appendedAll(xs)

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
      return false
    }

    /** Apply `f` to each element for
       side effects.
     * Note: [U] parameter needed to help scalac'
       pe
      ference.
     */
    def foreach[U](f: T => U): Unit = {
      var i: Int = 0
      while (i < a.length) {
        f(a(i))
        i = i + 1
      }
    }

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

    /** Produces the range of all indices of this sequence.
     *
     * @return a `Range` value from `0` to one less than the length of this array.
     */
    inline def indices: Range = Range(0, a.length)

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

  }
}