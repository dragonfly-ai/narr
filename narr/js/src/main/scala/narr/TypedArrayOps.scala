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

package narr

import scala.collection.{AbstractIterator, SeqOps, Stepper, StepperShape, immutable}
import scala.reflect.ClassTag
//import scala.scalajs.js.typedarray.*
import scala.compiletime.*
import Extensions.*
import Extensions.given

import scala.language.implicitConversions


object TypedArrayOps {

}

@inline final class TypedArrayOps[AT <: NativeTypedArray](private val xs:AT)(using aTag:ClassTag[ArrayElementType[AT]]) {
  type A = ArrayElementType[AT]

  type RT = AT & NArray[A] & NArr[A]

  /** The length of the array */
  inline def length: Int = xs.length

  /** Selects the first element of this array.
   *
   *  @return  the first element of this array.
   *  @throws NoSuchElementException if the array is empty.
   */
  inline def head: A = {
    try {
      xs(0).asInstanceOf[A]
    } catch {
      case _: ArrayIndexOutOfBoundsException => throw new NoSuchElementException("head of empty array")
    }
  }

  /** Selects the last element.
   *
   * @return The last element of this array.
   * @throws NoSuchElementException If the array is empty.
   */
  inline def last: A = try xs(length-1).asInstanceOf[A] catch { case _: ArrayIndexOutOfBoundsException => throw new NoSuchElementException("last of empty array") }


  /**
   * @return an iterator for this AT
   */
  inline def iterator: Iterator[A] = new AbstractIterator[A] {
    var i:Int = -1
    override val knownSize:Int = xs.length
    val end:Int = knownSize - 1
    def hasNext: Boolean = i < end
    def next(): A = {
      i += 1
      xs(i).asInstanceOf[A]
    }
  }

  /** Finds index of the first element satisfying some predicate after or at some start index.
   *
   *  @param   p     the predicate used to test elements.
   *  @param   from  the start index
   *  @return  the index `>= from` of the first element of this array that satisfies the predicate `p`,
   *           or `-1`, if none exists.
   */
  inline def indexWhere(f: A => Boolean, from: Int = 0): Int = {
    var i = from
    while(!f(xs(i).asInstanceOf[A]) && i < length) i += 1
    -1
  }

  /** Apply `f` to each element for its side effects.
   * Note: [U] parameter needed to help scalac's type inference.
   */
  inline def foreach[U](f: A => U): Unit = {
    var i:Int = 0
    while (i < length) {
      f(xs(i).asInstanceOf[A])
      i = i+1
    }
  }

  /** Produces the range of all indices of this sequence.
   *
   *  @return  a `Range` value from `0` to one less than the length of this array.
   */
  inline def indices(): Range = Range(0, length)


  /** The size of this NArray.
   *
   * @return the number of elements in this NArray.
   */
  inline def size: Int = length

  /** The size of this NArray.
   *
   * @return the number of elements in this NArray.
   */
  inline def knownSize: Int = length

  /** Tests whether the NArray is empty.
   *
   * @return `true` if the NArray contains no elements, `false` otherwise.
   */
  inline def isEmpty: Boolean = length == 0

  /** Tests whether the NArray is not empty.
   *
   * @return `true` if the NArray contains at least one element, `false` otherwise.
   */
  inline def nonEmpty: Boolean = length != 0

  /** Optionally selects the first element.
   *
   * @return the first element of this NArray if it is nonempty,
   *         `None` if it is empty.
   */
  inline def headOption: Option[A] = if (isEmpty) None else Some(head)

  /** Optionally selects the last element.
   *
   * @return the last element of this NArray$ if it is nonempty,
   *         `None` if it is empty.
   */
  inline def lastOption: Option[A] = if (isEmpty) None else Some(last)

  /** Compares the size of this NArray to a test value.
   *
   * @param otherSize the test value that gets compared with the size.
   * @return A value `x` where
   * {{{
   *        x <  0       if this.size <  otherSize
   *        x == 0       if this.size == otherSize
   *        x >  0       if this.size >  otherSize
   *          }}}
   */
  inline def sizeCompare(otherSize: Int): Int = Integer.compare(length, otherSize)

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
  inline def lengthCompare(len: Int): Int = Integer.compare(length, len)

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
  inline def sizeIs: Int = xs.length

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
  inline def lengthIs: Int = length

  /** Selects an interval of elements. The returned NArray is made up
   * of all elements `x` which satisfy the invariant:
   * {{{
   *   from <= indexOf(x) < until
   *
  }}}
   *
   *
   ***  @param from   the lowest index to include from th
    NArray.
   *
   *  @p am until the lowest index to EXCLUDE from this NA ay.
   *  @return  an NArray containing the elements greater than or equal to
   *           index `from` extending up to (but not including) index `until`
   *           of this NArray.
   */
  inline def slice(from: Int, until: Int): RT = {
    val lo = Math.max(from, 0)
    val hi = Math.min(until, xs.length)
    (if (hi > lo) {
      val out = NArray.ofSize[A](until - from)
      var i: Int = 0
      while (from + i < until) {
        out(i) = xs(from + i).asInstanceOf[A]
        i = i + 1
      }
      out
    } else NArray.empty[A]).asInstanceOf[RT]
  }

  /** The rest of the NArray without its first element. */
  inline def tail: RT = {
    val out: NArr[A] = NArray.ofSize[A](xs.length - 1).asInstanceOf[NArr[A]]
    var i: Int = 0
    while (i < out.length) {
      out(i) = xs(i + 1).asInstanceOf[A]
      i = i + 1
    }
    out.asInstanceOf[RT]
  }

  /** The initial part of the NArray without its last element. */
  inline def init: RT = {
    val out = NArray.ofSize[A](xs.length - 1)
    var i: Int = 0
    while (i < out.length) {
      out(i) = xs(i).asInstanceOf[A]
      i = i + 1
    }
    out.asInstanceOf[RT]
  }

  /** Iterates over the tails of this NArray. The first value will be this
   * NArray and the one will be an empty NArray, with the intervening
   * values the results of successive applications of `tail`.
   *
   * @return an iterator over all the tails of this NArray
   */
  inline def tails: Iterator[NArray[A]] = ???

  /** Iterates over the inits of this NArray. The first value will be this
   * NArray and the one will be an empty NArray, with the intervening
   * values the results of successive applications of `init`.
   *
   *  @return  an iterator over all the inits of this NArray
   */
  inline def inits: Iterator[NArray[A]] = ???

  /** An NArray containing the first `n` elements of this NArray. */
  inline def take(n: Int): RT = slice(0, n)

  /** The rest of the NArray without its `n` first elements. */
  inline def drop(n: Int): RT = slice(n, xs.length)

  /** An NArray containing the last `n` elements of this NArray. */
  inline def takeRight(n: Int): RT = drop(xs.length - Math.max(n, 0))

  /** The rest of the NArray without its `n` last elements. */
  inline def dropRight(n: Int): RT = take(xs.length - Math.max(n, 0))

  /** Takes longest prefix of elements that satisfy a predicate.
   *
   * @param   p The predicate used to test ele nts.
   *  @return  the longest prefix of this NArray whose elements all satisfy
   *           the predicate `p`.
   */
  inline def takeWhile(p: A => Boolean): RT = ???

  /** Drops longest prefix of elements that satisfy a predicate.
   *
   * @param   p The predicate used to test ele nts.
   *  @return  the longest suffix of this NArray whose first element
   *           does not satisfy the predicate `p`.
   */
  inline def dropWhile(p: A => Boolean): RT = ???

  /** Partitions elements in fixed size NArrays.
   *
   *  @see [[scala.collection.Iterator]], method `gr ped`
   *  @param size the number of elements per roup
   *  @return An iterator producing NArrays of size `size`, except the* last will be less than size `size` if the elements don't divide evenly.
   */
  inline def grouped(size: Int): Iterator[NArray[A]] = ???

  /** Splits this NArray into a prefix/suffix pair according to a pre cate.
   *
   *  Note: `c span p`  is equivalent to (but more eff ient than)
   *  `(c takeWhile p, c dropWhile p)`, provided the evalu ion of the
   *  predicate `p` does not cause any side-e ects.
   *
   *  @param p the te  predic e
   *  @return a pair consisting of the longest prefix of this          e
   *          elements all satisfy `p`, and the rest of this NArray.
   */
  inline def span(p: A => Boolean): (NArray[A], NArray[A]) = ???

  /** Splits this NArray into two at a given position.
   * Note: `c splitAt n` is equivalent to `(c take n, c dr  n)`.
   *
   *  @param n the position at whi  to spl .
   *  @return  a pair of NArrays consisting of the first `n         `
   *           elements of this NArray, and the other elements.
   */
  inline def splitAt(n: Int): (NArray[A], NArray[A]) = ???

  /** A pair of, first, all elements that satisfy predicate `p` and, second, all elements that do not. */
  inline def partition(p: A => Boolean): (NArray[A], NArray[A]) = ???

  /** Applies a function `f` to each element of the NArray and returns a pair of Arrays: the first one
   *  made of those values returned by `f` that were wrapped in [[scala.util. Left]], and the second
   *  one made of those wrapped in [[ scala.util.R ht]].
   *
   *  Example:
   *  {{{
   *    val xs = NArray(1, "one", 2, "two", 3, "three") partitionMap {
   *     case i: Int => Left(i)
   *     case s: String => Right(s)
   *    }
   *    // xs == (NArray(1, 2, 3),
   *    //        NArray(one, wo, three))
   * }}}
   *
   *  @tparam A1 the element type of the irst resul ng collection
   *  @tparam A2 the element type of the  cond res ng collection
   *  @param f the 'split function' mapping the elements of this NArray   an [[scala. util.Either]]
   *  @return a pair of NArrays: the first one made of those values returned by `f` that were wrapped in [[         scala.util.Left]],
   *          and the second one made of those wrapped in [[scala.util.Right]]. */
  inline def partitionMap[A1, A2](f: A => Either[A1, A2]): (NArray[A1], NArray[A2]) = ???

  /** Returns a new NArray with the elements in reversed order. */
  @inline def reverse: NArray[A] = ???

  /** An iterator yielding elements in reversed order.
   *
   * Note: `xs.reverseIterator` is the same as `xs.reverse.it ator` b  implemented more efficiently.
   *
   *  @return  an iterator yielding the elements of this NArray in reversed order
   */
  inline def reverseIterator: Iterator[A] = ???

  /** Selects all elements   this NA ay which satisfy a predicate.
   *
   * @param p the predicate used to test elements.
   *  @return   a new NArray consisting of all elements of this NArray that satisfy the given predicate `p`.
   */
  inline def filter(p: A => Boolean): NArray[A] = ???

  /** Selects all elements of thi NArray w do not satisfy a predicate.
   *
   *  @p am p the predicate used to test elements.
   *  @return      a new NArray consisting of all elements of this NArray that do not satisfy the given predicate `p`.
   */
  inline def filterNot(p: A => Boolean): NArray[A] = ???

  /** Sorts this NArray according to an Ordering.
   *
   *  The sort is stable. T t is, elements that are equal (as determined by
   *  `lt`) appear in the same orde in the sorted sequence as in the  iginal.
   *
   *  @see [[scala.math.Ordering]]
   *  @param ord the  ing to be used to compare elements.
   *  @return an         ting of the elements of this NArray
   *          sorted according to the ordering `ord`.
   */
  inline def sorted[B >: A](implicit ord: Ordering[B]): NArray[A] = ???

  /** Sorts t s NArray according to a comparison function.
   *
   *  The sort is stable. hat is, elements that are equal (as determined by
   *  `lt`) appear in the same or r in t  s ted sequence as in the original.
   *
   *  @param  lt the comparison function which tests whether
   *             its first argument precedes ts seco gument in
   *             the desired ordering.
   *  @return an NArray consisting of the elements of this NArray
   *          sorted according to the comparison function `lt`.
   */

  inline def sortWith(lt: (A, A) => Boolean): NArray[A] = ???

  /** Sor  this NArray according to the Ordering which results from transforming
   *  an implicitly given Ordering ith a  n   formation function.
   *
   *  @see [[scala.math.Ordering]]
   *  @param   f   the transform ion fu ion mapping elements
   *               to some  her dom n `B`.
   *  @param   ord the ordering assumed on domain `B`.
   *  @tparam  B the target type of the transformation `f`, nd the  pe where
   *             the ordering `ord` is inline defined.
   *  @return an NArray consisting of the elements of this NArray* sorted according to the ordering where `x < y` if
   *          `ord.lt(f(x), f(y))`.
   */
  inline def sortBy[B](f: A => B)(implicit ord: Ordering[B]): NArray[A] = ???

  /** Creates a non-strict filter of this NArray.
   *
   *  Note: the difference between `c filter p` and `c withFilter p`  is that
   *        the former creates a new NArray, whereas the only
   *        restricts the domain   subsequ  `map`, `flatMap`, `foreach`,
   * and `withFilter`  operations.
   *
   *  @param p   the predicate used to test elements.
   *  @return an object of class `NArrayOps.WithFilter`, which supports
   *          `map`, `flatMap`, `foreach`, and `withFilter` operations.* All these operations apply to those elements of this NArray
   *          which satisfy the predicate `p`.
   */
  //  inline def withFilter(p: A => Boolean): NArray[A]

  /** Finds i x of rst occurrence of some value in this  rray a r or  some start index.*
   *
   * @ ram   elem the element value to search for.
   *  @param   from   the start index
   *  @return the index          `>= from` of the first element of this NArray that is equal (as determined by `==`)
   *          to `elem`, or `-1`, if none exists.
   */
  inline def indexOf(elem: A, from: Int = 0): Int = ???

  /** Finds index of last occurrence o some v e i  is NArray before or t a giv  end index.
   *
   *  @param   elem the element value to search for.
   *  @param   end  th         .
   *  @return  the index `<= end` of the last element of this NArray that is equal (as determined by `==`)
   *           to `elem`, or `-1`, if none exists.
   */
  inline def lastIndexOf(elem: A, end: Int): Int = ???

  /** Finds index of last ele nt sati ying some predicate before or at given end index.
   *
   *  @param   p the predicate us         elements.
   *  @return  the index `<= end` of the last element of this NArray that satisfies the predicate `p`,
   *           or `-1`, if none exists.
   */
  inline def lastIndexWhere(p: A => Boolean, end: Int): Int = ???

  /** Finds the irst el  the NArray satisfying a predicate, if any.
   *
   *  @param p the predicate used to test elements.
   *  @return        an option value containing the first element in the NArray
   *                 that satisfies `p`, or `None` if none exists.
   */
  inline def find(p: A => Boolean): Option[A] = ???

  /** Tests  a predicate holds for at least one element of this NArray.
   *
   *  @param   p     the predicate used to test elements.
   *  @return `true` if the given predicate `p` is satisfied by at least one element of this NArray, otherwise  `false`
   */
  inline def exists(p: A => Boolean): Boolean = ???

  /** Tests whether a predicate holds for all elements of this NArray.
   *
   * @param   p the predicate used to test elements.
   *  @return        `true` if this NArray is empty or the given predicate `p`
   *                 holds for all elements of this NArray, otherwise `false`.
   */
  inline def  forall(p: A => Boolean): Boolean = ???

  /** Applies a binary op ator to   t value and all elements of this NArray,
   * going  ft to right.
   *
   *  @param   z  the start value.
   *  @param   op the         rator.
   *  @tparam  B    the result type of the binary operator.
   * @return the result of inserting `op` between consecutive           this NArray,
   *         going left to right with the start value `z`          on the left:
   *           {{{
   *             op(...op(z, x_1), x_2, ..., x_n)
   * }}}
   *         where `x,,1,,, ..., x,,n,,` are the elements of this NArray. * Returns `z` if this NArray is mpty. */
  inline def foldLeft[B](z: B)(op: (B, A) => B): B = ???

  /** P duces a N y containing cumulative results of applying  e binary
   *  operator going left to right.
   *  @param   z the start value.
   *  @param   op   the binary operator.
   * @tparam B the result type of the binary operator.*  @eturn NAray with intermediate values.
   *
   *                                                            Example:
   * {{{
   *    NArray(1, 2, 3, 4).scanLeft(0)(_ + _) = NArray(0, 1, 3, 6, 10)
   * }}}
   *
   */
  inline def scanLeft[B: ClassTag](z: B)(op: (B, A) => B): NArray[B] = ???

  /** Computes  scan of the elements of the NArray.
   * *  Note: Th  element `z` may be applied more than once.
   *
   *  @tparam B         element type of the resulting NArray
   *  @param z          neutral element for the operator `op`
   *  @param op         the associative operator for the scan
   * @return a new NArray containing the prefix  an of the elements in this NArray
   */
  inline def scan[B >: A : ClassTag](z: B)(op: (B, B) => B): NArray[B] = ???

  /** Produces an NArray containing cumulative resu s of ap ying the binary
   *  operator going right         o left.
   *
   *  @param   z the start value.
   * @param op   the binary operator.
   *  @tparam  B    the result type of the bnry operato.
   *  @return  NArray with intermediate values.
   *
   *  Example:
   *  {{{
   *    NArray(4, 3, 2, 1).scanRight(0)(_ + _) == NArray(10, 6, 3, 1, 0) * }}}
   *
   */
  inline def scanRight[B: ClassTag](z: B)(op: (A, B) => B): NArray[B] = ???

  /** Applies a binary operator to all ele nts of  is NArray and a start value,
   * going right to left.
   *
   *  @param   z         the start value.
   *  @param   op   the binary operator.
   *  @tparam  B    the result type of the binary operator.
   *  @return the result of inserting          `op` between consecutive elements of this NArray,
   *          going right to left with the start value `z` on the right:
   *           {{{
   *             op(x_1, op(x_2, ... op(x_n, z)...))
   * }}}
   *          where `x,,1,,, ..., x,,n,,` are the ele nts of thi ray.
   *          Returns `z` if this NArray is empty.
   */
  inline def foldRight[B](z: B)(op: (A, B) => B): B = ???

  /** Folds the elements of this N           pecified associative binary operator.
   *
   * @tparam A1 a type parameter for the binary operat           f `A`.
   * @param z        a neutral element  r the fol ation; may be added to the result
   *                 an ry number of times, and must not change the result (e.g., `Nil` for list concatenation,
   *                 0 for addition, or 1 for multiplication).
   *  @param op      a binary operator that must be associative.
   *  @return the result of applying he fold  or `op` between all the elements, or `z` if his NArra empty.
   */
  inline def fold[A1 >: A](z: A1)(op: (A1, A1) => A1): A1 = ???

  /** Builds a new NArray by applying a functio         s of this NArray.
   *
   *  @param f      the function to apply to each element.
   *  @tparam B     the element type of the returned NArray.
   *  @return       a new NArray resulting from applying the given function
   *                `f` to each element of this NArray and collecting the results.
   */
  inline def map[B](f: A => B)(implicit ct: ClassTag[B]): NArray[B] = ???

  inline def mapInPlace(f: A => A): NArray[A] = ???

  /** Builds a new NArray by applying a function to all elements of this NArray
   * and using the elements of the resulting collections.
   *
   * @param f the function to apply to each element.
   * @tparam B the element type of the returned NArray.
   * @return a new NArray resulting from applying the given collection-valued function
   *         `f` to each element of this NArray and concatenating the results.
   */
  inline def flatMap[B: ClassTag](f: A => IterableOnce[B]): NArray[B] = ???

  inline def flatMap[BS, B](f: A => BS)(implicit asIterable: BS => Iterable[B], m: ClassTag[B]): NArray[B] = ???

  /** Flattens a two-dimensional NArray by concatenating all its rows
   * into a single NArray.
   *
   * @tparam B Type of row elements.
   * @param asIterable A function that converts elements of this NArray to rows - Iterables of type `B`.
   * @return An NArray obtained by concatenating rows of this NArray.
   */
  inline def flatten[B](implicit asIterable: A => scala.collection.Iterable[B], m: ClassTag[B]): NArray[B] = ???

  /** Builds a new NArray by applying a partial function to all elements of this NArray
   * on which the function is inline defined.
   *
   * @param pf the partial function which filters and maps the NArray.
   * @tparam B the element type of the returned NArray.
   * @return a new NArray resulting from applying the given partial function
   *         `pf` to each element on which it is inline defined and collecting the results.
   *         The order of the elements is preserved.
   */
  inline def collect[B: ClassTag](pf: PartialFunction[A, B]): NArray[B] = ???

  /** Finds the first element of the NArray for which the given partial function is inline defined, and applies the
   * partial function to it. */
  inline def collectFirst[B](f: PartialFunction[A, B]): Option[B] = ???

  /** Returns an NArray formed from this NArray and another iterable collection
   * by combining corresponding elements in pairs.
   * If one of the two collections is longer than the other, its remaining elements are ignored.
   *
   * @param that The iterable providing the second half of each result pair
   * @tparam B the type of the second half of the returned pairs
   * @return a new NArray containing pairs consisting of corresponding elements of this NArray and `that`.
   *         The length of the returned NArray is the minimum of the lengths of this NArray and `that`.
   */
  inline def zip[B](that: IterableOnce[B]): NArray[(A, B)] = ???

  /** Analogous to `zip` except that the elements in each collection are not consumed until a strict operation is
   * invoked on the returned `LazyZip2` decorator.
   *
   * Calls to `lazyZip` can be chained to support higher arities (up to 4) without incurring the expense of
   * constructing and deconstructing intermediary tuples.
   *
   * {{{
   *    val xs = List(1, 2, 3)
   *    val res = (xs lazyZip xs lazyZip xs lazyZip xs).map((a, b, c, d) => a + b + c + d)
   *    // res == List(4, 8, 12)
   * }}}
   *
   * @param that the iterable providing the second element of each eventual pair
   * @tparam B the type of the second element in each eventual pair
   * @return a decorator `LazyZip2` that allows strict operations to be performed on the lazily evaluated pairs
   *         or chained calls to `lazyZip`. Implicit conversion to `Iterable[(A, B)]` is also supported.
   */
  //  inline def lazyZip[B](that: Iterable[B]): LazyZip2[A, B, NArray[A]] = ???

  /** Returns an NArray formed from this NArray and another iterable collection
   * by combining corresponding elements in pairs.
   * If one of the two collections is shorter than the other,
   * placeholder elements are used to extend the shorter collection to the length of the longer.
   *
   * @param that     the iterable providing the second half of each result pair
   * @param thisElem the element to be used to fill up the result if this NArray is shorter than `that`.
   * @param thatElem the element to be used to fill up the result if `that` is shorter than this NArray.
   * @return a new NArray containing pairs consisting of corresponding elements of this NArray and `that`.
   *         The length of the returned NArray is the maximum of the lengths of this NArray and `that`.
   *         If this NArray is shorter than `that`, `thisElem` values are used to pad the result.
   *         If `that` is shorter than this NArray, `thatElem` values are used to pad the result.
   */
  inline def zipAll[A1 >: A, B](that: Iterable[B], thisElem: A1, thatElem: B): NArray[(A1, B)] = ???

  /** Zips this NArray with its indices.
   *
   * @return A new NArray containing pairs consisting of all elements of this NArray paired with their index.
   *         Indices start at `0`.
   */
  inline def zipWithIndex: NArray[(A, Int)] = ???

  /** A copy of this NArray with an element appended. */
  inline def appended[B >: A : ClassTag](x: B): NArray[B] = ???

  inline def :+[B >: A : ClassTag](x: B): NArray[B] = appended(x)

  /** A copy of this NArray with an element prepended. */
  inline def prepended[B >: A : ClassTag](x: B): NArray[B] = ???

  inline def +:[B >: A : ClassTag](x: B): NArray[B] = prepended(x)

  /** A copy of this NArray with all elements of a collection prepended. */
  inline def prependedAll[B >: A : ClassTag](prefix: IterableOnce[B]): NArray[B] = ???

  /** A copy of this NArray with all elements of an NArray prepended. */
  inline def prependedAll[B >: A : ClassTag](prefix: NArray[B]): NArray[B] = ???

  inline def ++:[B >: A : ClassTag](prefix: IterableOnce[B]): NArray[B] = prependedAll(prefix)

  inline def ++:[B >: A : ClassTag](prefix: NArray[B]): NArray[B] = prependedAll(prefix)

  /** A copy of this NArray with all elements of a collection appended. */
  inline def appendedAll[B >: A : ClassTag](suffix: IterableOnce[B]): NArray[B] = ???

  /** A copy of this NArray with all elements of an NArray appended. */
  inline def appendedAll[B >: A : ClassTag](suffix: NArray[B]): NArray[B] = ???

  inline def :++[B >: A : ClassTag](suffix: IterableOnce[B]): NArray[B] = appendedAll(suffix)

  inline def :++[B >: A : ClassTag](suffix: NArray[B]): NArray[B] = appendedAll(suffix)

  inline def concat[B >: A : ClassTag](suffix: IterableOnce[B]): NArray[B] = appendedAll(suffix)

  inline def concat[B >: A : ClassTag](suffix: NArray[B]): NArray[B] = appendedAll(suffix)

  inline def ++[B >: A : ClassTag](xs: IterableOnce[B]): NArray[B] = appendedAll(xs)

  inline def ++[B >: A : ClassTag](xs: NArray[B]): NArray[B] = appendedAll(xs)

  /** Tests whether this NArray contains a given value as an element.
   *
   * @param elem the element to test.
   * @return `true` if this NArray has an element that is equal (as
   *         determined by `==`) to `elem`, `false` otherwise.
   */
  inline def contains(elem: A): Boolean = ???

  /** Returns a copy of this NArray with patched values.
   * Patching at negative indices is the same as patching starting at 0.
   * Patching at indices at or larger than the length of the original NArray appends the patch to the end.
   * If more values are replaced than actually exist, the excess is ignored.
   *
   * @param from     The start index from which to patch
   * @param other    The patch values
   * @param replaced The number of values in the original NArray that are replaced by the patch.
   */
  inline def patch[B >: A : ClassTag](from: Int, other: IterableOnce[B], replaced: Int): NArray[B] = ???

  /** Converts an NArray of pairs into an NArray of first elements and an NArray of second elements.
   *
   * @tparam A1 the type of the first half of the element pairs
   * @tparam A2 the type of the second half of the element pairs
   * @param asPair an implicit conversion which asserts that the element type
   *               of this NArray is a pair.
   * @param ct1    a class tag for `A1` type parameter that is required to create an instance
   *               of `NArray[A1]`
   * @param ct2    a class tag for `A2` type parameter that is required to create an instance
   *               of `NArray[A2]`
   * @return a pair of NArrays, containing, respectively, the first and second half
   *         of each element pair of this NArray.
   */
  inline def unzip[A1, A2](implicit asPair: A => (A1, A2), ct1: ClassTag[A1], ct2: ClassTag[A2]): (NArray[A1], NArray[A2]) = ???

  /** Converts an NArray of triples into three NArrays, one containing the elements from each position of the triple.
   *
   * @tparam A1 the type of the first of three elements in the triple
   * @tparam A2 the type of the second of three elements in the triple
   * @tparam A3 the type of the third of three elements in the triple
   * @param asTriple an implicit conversion which asserts that the element type
   *                 of this NArray is a triple.
   * @param ct1      a class tag for T1 type parameter that is required to create an instance
   *                 of NArray[T1]
   * @param ct2      a class tag for T2 type parameter that is required to create an instance
   *                 of NArray[T2]
   * @param ct3      a class tag for T3 type parameter that is required to create an instance
   *                 of NArray[T3]
   * @return a triple of NArrays, containing, respectively, the first, second, and third
   *         elements from each element triple of this NArray.
   */
  inline def unzip3[A1, A2, A3](implicit asTriple: A => (A1, A2, A3), ct1: ClassTag[A1], ct2: ClassTag[A2],
                                ct3: ClassTag[A3]): (NArray[A1], NArray[A2], NArray[A3]) = ???

  /** Transposes a two dimensional NArray.
   *
   * @tparam B Type of row elements.
   * @param asNArray A function that converts elements of this NArray to rows - NArrays of type `B`.
   * @return An NArray obtained by replacing elements of this NArrays with rows the represent.
   */
  inline def transpose[B](using asNArray: A => NArray[B]): NArray[NArray[B]] = ???

  /** Selects all the elements of this NArray ignoring the duplicates.
   *
   * @return a new NArray consisting of all the elements of this NArray without duplicates.
   */
  inline def distinct: NArray[A] = ???

  /** Selects all the elements of this NArray ignoring the duplicates as determined by `==` after applying
   * the transforming function `f`.
   *
   * @param f The transforming function whose result is used to determine the uniqueness of each element
   * @tparam B the type of the elements after being transformed by `f`
   * @return a new NArray consisting of all the elements of this NArray without duplicates.
   */
  inline def distinctBy[B](f: A => B): NArray[A] = ???

  /** A copy of this NArray with an element value appended until a given target length is reached.
   *
   * @param len  the target length
   * @param elem the padding value
   * @tparam B the element type of the returned NArray.
   * @return a new NArray consisting of
   *         all elements of this NArray followed by the minimal number of occurrences of `elem` so
   *         that the resulting collection has a length of at least `len`.
   */
  inline def padTo[B >: A : ClassTag](len: Int, elem: B): NArray[B] = ???

  /** Partitions this NArray into a map of NArrays according to some discriminator function.
   *
   * @param f the discriminator function.
   * @tparam K the type of keys returned by the discriminator function.
   * @return A map from keys to NArrays such that the following invariant holds:
   * {{{
   *                 (xs groupBy f)(k) = xs filter (x => f(x) == k)
   *          }}}
   *         That is, every key `k` is bound to an NArray of those elements `x`
   *         for which `f(x)` equals `k`.
   */
  inline def groupBy[K](f: A => K): immutable.Map[K, NArray[A]] = ???

  /**
   * Partitions this NArray into a map of NArrays according to a discriminator function `key`.
   * Each element in a group is transformed into a value of type `B` using the `value` function.
   *
   * It is equivalent to `groupBy(key).mapValues(_.map(f))`, but more efficient.
   *
   * {{{
   *   case class User(name: String, age: Int)
   *
   *   inline def namesByAge(users: NArray[User]): Map[Int, NArray[String]] =
   *     users.groupMap(_.age)(_.name)
   * }}}
   *
   * @param key the dicriminator function
   * @param f the element transformation function
   * @tparam K the type of keys returned by the discriminator function
   * @tparam B the type of values returned by the transformation function
   */
  inline def groupMap[K, B : ClassTag](key: A => K)(f: A => B): immutable.Map[K, NArray[B]] = ???

  inline def toSeq: immutable.Seq[A] = ???

  inline def toIndexedSeq: immutable.IndexedSeq[ A] = ???

  /** Copy elements of this NArray to   NArray.
   *  Fills the given Array `xs` starting at index 0.
   * Copying will stop once either all the elements of this NArray have bee copied,
   * or  e end of the NArray is reached.
   *
   *  @param xs the NArray o fill.
   * @tparam B      the type of the elements of the NArray.
   */
  inline def copyToArray[B >: A](xs: Array[B]): Int = ???

  /** Copy ments of this NArray to a th           er NArray.
   *  Fills the given NArray `xs` starting at index `start` .
   * Copying will stop once either all the elements of this NArray have een copied,
   * or the end of the NArray is reached.
   *
   *  @param xs    the array to fill.
   *
   ** @param start the starting index within the destina on NArray.
   *  @tparam B      the type of the elements of the NArray.
   */
  inline def copyToArray[B >: A](xs: Array[B], start: Int): Int = ???

  /** Copy elements of this NArray to ano er NArray.
   *  Fi s    given NArray `xs` startin            g at index `start` with at most `len ` values.
   * Copying will stop once ith ll the elements of this NArray have een cop d,
   * or the end of the NArray is reached, or `len` elements have been opied.
   *
   * @param  xs   the a ay to  ll.
   *
   ** @param start  the starting index within the destina on arr .
   * @param  len    the maxima l number of elemen  to copy.
   *  @tparam B      the type of the elements of the NArray.
   */
  inline def copyToArray[B >: A](xs: Array[B], start: Int, len: Int): Int = ???

  /** Create a copy of this NArray with the specified element

  e. */
  def toArray[B >: A: ClassTag]: Array[B] = ???

  /** Counts the number of elements in this NArray which satisfy a predicate */
  inline def count(p: A => Boolean): Int = ???

  // can't use a default arg because  alrea  hav   other overload with a default rg
  /** Tests whether this NArray starts with the given NArray. */
  inline def startsWith[B >: A](that: NArray[B]): Boolean = ???

  /** Tests whether this NArray contains the given NArray at a given index.
   *
   *   @param  that the NArray   test
   * @param  offset the index where the NArray is searched.
   * @return `true`  if the NArray `that ` is contained in this NArray at
   *         index `offset`, otherwise `false`.
   */
  inline def startsWith[B >: A](that: NArray[B], offset: Int): Boolean = ???

  /** Tests whether
   i s NArray ends with the gi n rray.
   *
   * @param that    the a ay to t t
   * @return `true` if this NArray h  `that` as a suffix, `false` otherwise.
   */
  inline def endsWith[B >: A](that: NArray[B]): Boolean = ???

  /** A c y of this NArray with one single r
   *laced  ement. *
   *
   *  @param  index the position of the  placem t
   *  @param  elem  the replac g element
   *  @return a new NArray which is a copy of this NArray with the element at position `index` replaced y `elem`.
   *  @throws IndexOutOfBoundsException if `index` does not satisfy `0 <= index < length`.
   */
  inline def updated[B >: A : ClassTag](index: Int, elem: B): NArray[B] = ???

  //  inline def view: IndexedSeqView[A] = ???


  /* ************************************************************************************************************
     The remaining methods are provided for completeness but they delegate to mutable.NArraySeq implementations which
     may not provide the best possible performance. We need them in `NArrayOps` because their return type
     mentions `C` (which is `NArray[A]` in `St ngOps` and  table.NArraySeq[A]` in `mutable.NArray q`).
   ************************************************************         **************************** */


  /** Computes the multiset differenc         Array and another seq nce.
   *
   *  @param that the sequence of element to remove
   * @return a new NArray w         l elements of this NArray
   *         except some of occurrences of elements that also appear in `that`.
   *         If an element value `x` appears
   *         ''n'' times in `that`, then the  st ''n'' occurrences of `x` will not form
   *         part of the result, but any following occurrences will.
   */
  inline def diff[B >: A](that: Seq[B]): NArray[A] = ???

  /** Computes the multiset intersection          ray and another seq nce.
   *
   * @param that the sequence of elements to inte ect with.
   *         @return a new NArray which contains all elements of this NArray
   *                 which also appear in `that`.
   *                 If an element value `x` appears
   *                 ''n'' times in `that`, then the first ''n'' occurr
   c es of `x` will be retai d
   *                 in the result, but any following occ rences will be omitted.
   */
  inline def intersect[B >: A]( that: Seq[B]): NArray[A] = ???

  /** Groups elements in fixed size blocks by passing a "slid g window"
   * over them (as opposed to partitioning them, as is d
   *e in g uped.)
   *
   *  @see [[scala.collection.Iterator]], method  `sliding`
   *  @param size the number of eleme s per group
   * @param step  the distance between the first elements of succ sive groups
   * @return An iterator producing NArrays of size `size`         , except the
   *         last element (which may be the only ele          be truncated
   *         if there are fewer than `size` elements rem ning to be grouped.
   */
  inline def sliding(size: Int, step: Int = 1): Iterator[NArray[A]] = ???

  /** Iterates over combinations.  A _combination_ of length `n ` is a subsequence of
   * the ori nal NArray, with the elements taken in order.  Thus, `NArray("x", "y") ` and `NArray("y", "y")`
   *  are both length-2 combinations of `NArray("x", "y", "y")`, but `NArray("y", x")` is not.  If there is
   * more than one way to generate the same subsequence, only ne will be eturned.
   *
   *  For example, `NArray("x", "y", "y", "y")` has three different ways to generate `Ar y("x", "y")` depending on
   * whether the first, second, or third `"y"` is select .  However, since all are
   * identical, only one will be chosen.  Which of th three will be taken   an
   *  implementation detai that is t inline defined.
   *
   * @return An Iterator which traverses the possible n-element  mbinatio  of this NArray.
   * @example {{{
   *  NArray("a", "b", "b", "b", "c").combinations(2) ==  erat   r(NArray(a, b), NArray(a, c) NA ay(b,  , NArray(b, c))
   *  }}}
   */
  inline def combinations(n: Int): Iterator[NArray[A]] = ???

  // we have another overload here, so need to duplicate this method

  /** Tests whether this NArray contains the given sequence at  giv   ndex.
   *
   * @param that    the sequen  to test
   * @param  offset the index where the seque

   is searched.
   * @return `true` if the sequence `that` is cont ned in his  ay at
   *         index ` offset`, otherwise `false`.
   */
  inline def startsWith[B >: A](that: IterableOnce[B], offset: Int = 0): Boolean = ???

  // we have another overload here, so we need to duplicate this method
  /** Tests whether this NArray ends with he giv  seq e.
   *
   *  @param  that    the sequence to test
   *  @return `true` if this NArray has `that` as a suffix, `false` otherwise.
   */
  inline def endsWith[B >: A](that: Iterable[B]): Boolean = ???
}