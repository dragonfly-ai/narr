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

import scala.collection.ArrayOps
import scala.reflect.ClassTag
import scala.language.implicitConversions
import scala.util.Sorting.*
import java.util

package object native {

  type ByteArray = Array[Byte]
  type ShortArray = Array[Short]
  type IntArray = Array[Int]
  type FloatArray = Array[Float]
  type DoubleArray = Array[Double]
  type NativeArray[T] = Array[T]

  type NArray[T] = Array[T]

  type NArr[T] = Array[T]
  type SortableNArr[T] = Array[T]

  inline def makeNativeArrayOfSize[A](n:Int)(using ClassTag[A]):NativeArray[A] = new Array[A](n)

  object NArray {
    export Array.*

    /** Copy one array to another.
     *
     * Note that the passed-in `dest` array will be modified by this call.
     *
     * @param src     the source array.
     * @param dest    destination array.
     * @param destPos starting position in the destination array.
     * @see `java.lang.System#arraycopy`
     */
    inline def copyByteArray(src: ByteArray, dest: ByteArray, destPos: Int): Unit = Array.copy(src, 0, dest, destPos, src.length)
    inline def copyShortArray(src: ShortArray, dest: ShortArray, destPos: Int): Unit = Array.copy(src, 0, dest, destPos, src.length)
    inline def copyIntArray(src: IntArray, dest: IntArray, destPos: Int): Unit = Array.copy(src, 0, dest, destPos, src.length)
    inline def copyFloatArray(src: FloatArray, dest: FloatArray, destPos: Int): Unit = Array.copy(src, 0, dest, destPos, src.length)
    inline def copyDoubleArray(src: DoubleArray, dest: DoubleArray, destPos: Int): Unit = Array.copy(src, 0, dest, destPos, src.length)
    inline def copyNativeArray[T](src: NArray[T], dest: NArray[T], destPos: Int): Unit = Array.copy(src, 0, dest, destPos, src.length)

    /** Copy one array to another.
     * Equivalent to Java's
     * `System.arraycopy(src, srcPos, dest, destPos, length)`,
     * except that this also works for polymorphic and boxed arrays.
     *
     * Note that the passed-in `dest` array will be modified by this call.
     *
     * @param src     the source array.
     * @param srcPos  starting position in the source array.
     * @param dest    destination array.
     * @param destPos starting position in the destination array.
     * @param length  the number of array elements to be copied.
     * @see `java.lang.System#arraycopy`
     */
    inline def copyByteArray(src: ByteArray, srcPos: Int, dest: ByteArray, destPos: Int, length: Int): Unit = java.lang.System.arraycopy(src, srcPos, dest, destPos, length)
    inline def copyShortArray(src: ShortArray, srcPos: Int, dest: ShortArray, destPos: Int, length: Int): Unit = java.lang.System.arraycopy(src, srcPos, dest, destPos, length)
    inline def copyIntArray(src: IntArray, srcPos: Int, dest: IntArray, destPos: Int, length: Int): Unit = java.lang.System.arraycopy(src, srcPos, dest, destPos, length)
    inline def copyFloatArray(src: FloatArray, srcPos: Int, dest: FloatArray, destPos: Int, length: Int): Unit = java.lang.System.arraycopy(src, srcPos, dest, destPos, length)
    inline def copyDoubleArray(src: DoubleArray, srcPos: Int, dest: DoubleArray, destPos: Int, length: Int): Unit = java.lang.System.arraycopy(src, srcPos, dest, destPos, length)
    inline def copyNativeArray[T](src: NativeArray[T], srcPos: Int, dest: NativeArray[T], destPos: Int, length: Int): Unit = Array.copy(src, srcPos, dest, destPos, length)

    /** Copy one array to another, truncating or padding with default values (if
     * necessary) so the copy has the specified length. The new array can have
     * a different type than the original one as long as the values are
     * assignment-compatible. When copying between primitive and object arrays,
     * boxing and unboxing are supported.
     *
     * Equivalent to Java's
     * `java.util.Arrays.copyOf(original, newLength, newType)`,
     * except that this works for all combinations of primitive and object arrays
     * in a single method.
     *
     * @see `java.util.Arrays#copyOf`
     */
    inline def copyAs[T, B >: T](original: NArray[T], newLength: Int)(using ClassTag[B]): NArray[B] = {
      Array.copyAs[B](original, newLength)
    }

  }

  object Extensions {

    export collection.ArrayOps

    extension (ua: NArray[Unit]) {
      inline def sort(): NArray[Unit] = ua
      inline def sort(ot: Ordering[Unit]): NArray[Unit] = {
        quickSort[Unit](ua)(ot)
        ua
      }
    }

    extension (ba: NArray[Boolean]) {
      inline def sort(): NArray[Boolean] = sort(Ordering.Boolean)
      inline def sort(ot: Ordering[Boolean]): NArray[Boolean] = {
        quickSort[Boolean](ba)(ot); ba
      }
    }

    extension (ba: ByteArray) {
      inline def sort(): ByteArray = { util.Arrays.sort(ba); ba}
      inline def sort(ot: Ordering[Byte]): ByteArray = { quickSort[Byte](ba)(ot); ba}
    }
    extension (sa: ShortArray) {
      inline def sort(): ShortArray = { util.Arrays.sort(sa); sa }
      inline def sort(ot: Ordering[Short]): ShortArray = { quickSort[Short](sa)(ot); sa}
    }

    extension (ia: IntArray) {
      inline def sort(): IntArray = { util.Arrays.sort(ia); ia }
      inline def sort(ot: Ordering[Int]): IntArray = { quickSort[Int](ia)(ot); ia }
    }

    extension (la: NArray[Long]) {
      inline def sort(): NArray[Long] = { util.Arrays.sort(la); la }
      inline def sort(ot: Ordering[Long]): NArray[Long] = { quickSort[Long](la)(ot); la}
    }

    extension (fa: FloatArray) {
      inline def sort(): FloatArray = { util.Arrays.sort(fa); fa }
      inline def sort(ot: Ordering[Float]): FloatArray = { quickSort[Float](fa)(ot); fa }
    }

    extension (da: DoubleArray) {
      inline def sort(): DoubleArray = { util.Arrays.sort(da); da }
      inline def sort(ot: Ordering[Double]): DoubleArray = { quickSort[Double](da)(ot); da }
    }

    extension (ca: NArray[Char]) {
      inline def sort(): NArray[Char] = { util.Arrays.sort(ca); ca }
      inline def sort(ot: Ordering[Char]): NArray[Char] = { quickSort[Char](ca)(ot); ca }
    }

    extension (a: NArray[String]) {
      inline def sort(): NArray[String] = { quickSort[String](a)(Ordering.String); a }
      inline def sort(ot: Ordering[String]): NArray[String] = { quickSort[String](a)(ot); a }
    }

    extension[T <: AnyRef] (a:NArray[T]) {
      inline def sort(ot:Ordering[T]): NArray[T] = { quickSort[T](a)(ot); a }
    }

    extension[T](a:NArray[T]) {
      def copy: NArray[T] = (a match {
        case nArr: Array[Boolean] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Byte] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Short] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Int] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Long] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Float] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Double] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Char] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[String] => util.Arrays.copyOf(nArr, nArr.length)
        case _ => util.Arrays.copyOf[T](a.asInstanceOf[Array[AnyRef & T]], a.length)
      }).asInstanceOf[NArr[T] & NArray[T]]

      /** Copy elements of this array to another array.
       * Fills the given array `xs` starting at index 0.
       * Copying will stop once either all the elements of this array have been copied,
       * or the end of the array is reached.
       *
       * @param xs the array to fill.
       * @tparam B the type of the elements of the array.
       */
      inline def copyToNArray[B >: T](xs: NArray[B]): Int = a.copyToArray(xs)

      /** Copy elements of this array to another array.
       * Fills the given array `xs` starting at index `start`.
       * Copying will stop once either all the elements of this array have been copied,
       * or the end of the array is reached.
       *
       * @param xs    the array to fill.
       * @param start the starting index within the destination array.
       * @tparam B the type of the elements of the array.
       */
      inline def copyToNArray[B >: T](xs: NArray[B], start: Int): Int = a.copyToArray(xs, start, Int.MaxValue)

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
      inline def copyToNArray[B >: T](xs: NArray[B], start: Int, len: Int): Int = a.copyToArray(xs, start, len)

      /** Create a copy of this array with the specified element type. */
      def toNArray[B >: T : ClassTag]: NArray[B] = {
        val destination = narr.NArray.ofSize[B](a.length)
        @annotation.unused val copied = copyToNArray[B](destination, 0)
        //assert(copied == xs.length)
        destination
      }

      def startsWithIterable[B >: T](that: IterableOnce[B], offset: Int = 0): Boolean = a.startsWith(that, offset)

      def endsWithIterable[B >: T](that: scala.collection.Iterable[B]): Boolean = a.endsWith(that)
    }
  }
}
