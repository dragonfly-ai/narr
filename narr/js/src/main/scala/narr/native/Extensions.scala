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

import scala.compiletime.*
import scala.scalajs.js
import scala.scalajs.js.annotation.JSBracketAccess
import scala.scalajs.js.typedarray.*
import scala.language.implicitConversions
import scala.math.Ordering
import scala.reflect.ClassTag

// to imbue js.Array with scala.Array semantics


object Extensions {

  given orderingToCompareFunction[T]: Conversion[Ordering[T], js.Function2[T, T, Int]] with
    def apply(o: Ordering[T]): js.Function2[T, T, Int] = (a: T, b: T) => o.compare(a, b)


  extension (a: ByteArray) {
    def sort(): ByteArray = sortByteArray(a)
    def sort(ord: Ordering[Byte]): ByteArray = sortByteArray(a, ord)

    def sorted: ByteArray = sorted(Ordering.Byte)
    def sorted(ord: Ordering[Byte]): ByteArray = sortByteArray(copy[Byte](a), ord)

    def slice(from: Int, until: Int): ByteArray = a.asInstanceOf[NArr[Byte]].slice(from, until).asInstanceOf[ByteArray]
  }

  extension (a: ShortArray) {
    def sort(): ShortArray = sortShortArray(a)
    def sort(ord: Ordering[Short]): ShortArray = sortShortArray(a, ord)

    def sorted: ShortArray = sorted(Ordering.Short)
    def sorted(ord: Ordering[Short]): ShortArray = sortShortArray(copy[Short](a), ord)
    def slice(from: Int, until: Int): ShortArray = a.asInstanceOf[NArr[Short]].slice(from, until).asInstanceOf[ShortArray]
  }

  extension (a: IntArray) {
    def sort(): IntArray = sortIntArray(a)
    def sort(ord: Ordering[Int]): IntArray = sortIntArray(a, ord)

    def sorted:IntArray = sorted(Ordering.Int)
    def sorted(ord:Ordering[Int]): IntArray = sortIntArray(copy[Int](a), ord)

    def slice(from:Int, until:Int):IntArray = a.asInstanceOf[NArr[Int]].slice(from, until).asInstanceOf[IntArray]
  }

  extension (a: FloatArray) {
    def sort(): FloatArray = sortFloatArray(a)
    //sort(Ordering.Float.TotalOrdering)
    def sort(ord: Ordering[Float]): FloatArray = sortFloatArray(a, ord)
    def sorted: FloatArray = sorted(Ordering.Float.TotalOrdering)
    def sorted(ord: Ordering[Float]): FloatArray = sortFloatArray(copy[Float](a), ord)

    def slice(from: Int, until: Int): FloatArray = a.asInstanceOf[NArr[Float]].slice(from, until).asInstanceOf[FloatArray]
  }

  extension (a: DoubleArray) {
    def sort(): DoubleArray = sortDoubleArray(a)
    def sort(ord: Ordering[Double]): DoubleArray = sortDoubleArray(a, ord)

    def sorted: DoubleArray = sorted(Ordering.Double.TotalOrdering)
    def sorted(ord: Ordering[Double]): DoubleArray = sortDoubleArray(copy[Double](a), ord)
    def slice(from: Int, until: Int): DoubleArray = a.asInstanceOf[NArr[Double]].slice(from, until).asInstanceOf[DoubleArray]
  }

  extension[T] (a:NArray[T]) {
    inline def copy: NArray[T] = {
      var temp = a.asInstanceOf[NArr[T]]
      temp.slice(0, temp.length).asInstanceOf[NArray[T]]
    }

    inline def head: T = a(0)

    inline def last: T = try {
      a(a.length - 1)
    } catch {
      case _: ArrayIndexOutOfBoundsException => throw new NoSuchElementException("last of empty array")
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

    /** Finds index of the first element satisfying some predicate after or at some start index.
     *
     * @param p    the predicate used to test elements.
     * @param from the start index
     * @return the index `>= from` of the first element of this array that satisfies the predicate `p`,
     *         or `-1`, if none exists.
     */
    def indexWhere(f: T => Boolean, from: Int = 0): Int = {
      var i = from
      while (!f(a(i)) && i < a.length) i += 1
      -1
    }

    /** Apply `f` to each element for its side effects.
     * Note: [U] parameter needed to help scalac's type inference.
     */
    def foreach[U](f: T => U): Unit = {
      var i: Int = 0
      while (i < a.length) {
        f(a(i))
        i = i + 1
      }
    }

    /** Produces the range of all indices of this sequence.
     *
     * @return a `Range` value from `0` to one less than the length of this array.
     */
    inline def indices(): Range = Range(0, a.length)

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
     *           }}}
     */
    inline def sizeCompare(otherSize: Int): Int = Integer.compare(a.length, otherSize)

    /** Compares the length of this NArray to a test value.
     *
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


    /** Selects an interval of elements. The returned NArray is made up
     * of all elements `x` which satisfy the invariant:
     * {{{
     *   from <= indexOf(x) < until
     *
     * }}}
     *
     *
     * **  @param from   the lowest index to include from th
     * NArray.
     *
     * @p am until the lowest index to EXCLUDE from this NA ay.
     * @return an NArray containing the elements greater than or equal to
     *         index `from` extending up to (but not including) index `until`
     *         of this NArray.
     */
    inline def slice(from: Int, until: Int = a.length): NArray[T] = {
      a.asInstanceOf[NArr[T]].slice(from, until).asInstanceOf[NArray[T]]
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

  }
}