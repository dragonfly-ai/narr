package narr.native

import narr.native.*

import scala.collection.SeqOps
import scala.reflect.ClassTag
import scala.scalajs.js.typedarray.TypedArray

import Extensions.*
import Extensions.given

import scala.language.implicitConversions


@inline
final class TypedArrayOps[A <: TypedArrayPrimitive](private val xs:NArr[A]) extends AnyVal {
  //@`inline` private[this] implicit def elemTag: ClassTag[A] = ClassTag(xs.getClass.getComponentType)

  /** The size of this array.
   *
   *  @return    the number of elements in this array.
   */
  @`inline` def size: Int = xs.length

  /** The size of this array.
   *
   *  @return    the number of elements in this array.
   */
  @`inline` def knownSize: Int = xs.length

  /** Tests whether the array is empty.
   *
   *  @return    `true` if the array contains no elements, `false` otherwise.
   */
  @`inline` def isEmpty: Boolean = xs.length == 0

  /** Tests whether the array is not empty.
   *
   *  @return    `true` if the array contains at least one element, `false` otherwise.
   */
  @`inline` def nonEmpty: Boolean = xs.length != 0

  /** Selects the first element of this array.
   *
   *  @return  the first element of this array.
   *  @throws NoSuchElementException if the array is empty.
   */
  def head: A = {
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
  def last: A = try xs(xs.length-1).asInstanceOf[A] catch { case _: ArrayIndexOutOfBoundsException => throw new NoSuchElementException("last of empty array") }

  /** Optionally selects the first element.
   *
   *  @return  the first element of this array if it is nonempty,
   *           `None` if it is empty.
   */
  def headOption: Option[A] = if(isEmpty) None else Some(head)

  /** Optionally selects the last element.
   *
   *  @return  the last element of this array$ if it is nonempty,
   *           `None` if it is empty.
   */
  def lastOption: Option[A] = if(isEmpty) None else Some(last)

  /** Compares the size of this array to a test value.
   *
   *   @param   otherSize the test value that gets compared with the size.
   *   @return  A value `x` where
   *   {{{
   *        x <  0       if this.size <  otherSize
   *        x == 0       if this.size == otherSize
   *        x >  0       if this.size >  otherSize
   *   }}}
   */
  def sizeCompare(otherSize: Int): Int = Integer.compare(xs.length, otherSize)

  /** Compares the length of this array to a test value.
   *
   *   @param   len   the test value that gets compared with the length.
   *   @return  A value `x` where
   *   {{{
   *        x <  0       if this.length <  len
   *        x == 0       if this.length == len
   *        x >  0       if this.length >  len
   *   }}}
   */
  def lengthCompare(len: Int): Int = Integer.compare(xs.length, len)

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
  def sizeIs: Int = xs.length

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
  def lengthIs: Int = xs.length

  /** Apply `f` to each element for its side effects.
   * Note: [U] parameter needed to help scalac's type inference.
   */
  def foreach[U](f: A => U): Unit = {
    val len = xs.length
    var i = 0
    while (i < len) {
      f(xs(i).asInstanceOf[A])
      i = i+1
    }
  }

  /** Produces the range of all indices of this sequence.
   *
   *  @return  a `Range` value from `0` to one less than the length of this array.
   */
  def indices: Range = Range(0, xs.length)
}