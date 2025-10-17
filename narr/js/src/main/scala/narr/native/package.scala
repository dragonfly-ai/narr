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

import scala.annotation.nowarn
import scala.scalajs.js.typedarray.*
//import narr.native.Extensions.*
import narr.native.Extensions.given

import scala.language.implicitConversions
import scala.reflect.ClassTag

package object native {

  type ByteArray = Int8Array
  type ShortArray = Int16Array
  type IntArray = Int32Array
  type FloatArray = Float32Array
  type DoubleArray = Float64Array
  type NativeArray[T] = scala.scalajs.js.Array[T]

  type NArray[T] = T match
    case Byte => ByteArray
    case Short => ShortArray
    case Int => IntArray
    case Float => FloatArray
    case Double => DoubleArray
    case _ => NativeArray[T]

  def sortByteArray(a: ByteArray, ord: Ordering[Byte] = Ordering.Byte): ByteArray = {
    a.asInstanceOf[SortableNArr[Byte]].sort(ord).asInstanceOf[ByteArray]
  }

  def sortShortArray(a: ShortArray, ord: Ordering[Short] = Ordering.Short): ShortArray = {
    a.asInstanceOf[SortableNArr[Short]].sort(ord).asInstanceOf[ShortArray]
  }

  def sortIntArray(a: IntArray, ord: Ordering[Int] = Ordering.Int): IntArray = {
    a.asInstanceOf[SortableNArr[Int]].sort(ord).asInstanceOf[IntArray]
  }

  def sortFloatArray(a: FloatArray, ord: Ordering[Float] = Ordering.Float.TotalOrdering): FloatArray = {
    a.asInstanceOf[SortableNArr[Float]].sort(ord).asInstanceOf[FloatArray]
  }

  def sortDoubleArray(a: DoubleArray, ord: Ordering[Double] = Ordering.Double.TotalOrdering): DoubleArray = {
    a.asInstanceOf[SortableNArr[Double]].sort(ord).asInstanceOf[DoubleArray]
  }

  object NArray {
    /** Copy one array to another.
     *
     * Note that the passed-in `dest` array will be modified by this call.
     *
     * @param src     the source array.
     * @param dest    destination array.
     * @param destPos starting position in the destination array.
     * @see `java.lang.System#arraycopy`
     */

    inline def copyByteArray(src: ByteArray, dest: ByteArray, destPos: Int): Unit = dest.set(src, destPos)
    inline def copyShortArray(src: ShortArray, dest: ShortArray, destPos: Int): Unit = dest.set(src, destPos)
    inline def copyIntArray(src: IntArray, dest: IntArray, destPos: Int): Unit = dest.set(src, destPos)
    inline def copyFloatArray(src: FloatArray, dest: FloatArray, destPos: Int): Unit = dest.set(src, destPos)
    inline def copyDoubleArray(src: DoubleArray, dest: DoubleArray, destPos: Int): Unit = dest.set(src, destPos)
    inline def copyNativeArray[T](src: NativeArray[T], dest: NativeArray[T], destPos: Int): Unit = {
      //val availableSpace = dest.length - destPos
      //if (src.length > availableSpace) throw Exception(s"Can't copy source array into destination.  The source is too long.")
      var i:Int = 0; while (i < src.length) {
        dest(destPos + i) = src(i)
        i = i + 1
      }
    }
    /** Copy one array to another.
     * Shim for Java's `System.arraycopy(src, srcPos, dest, destPos, length)`,
     * except that this slices the sub array, temporarily duplicating data.
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

    inline def copyByteArray(src: ByteArray, srcPos: Int, dest: ByteArray, destPos: Int, length:Int): Unit = {
      if (srcPos != 0 || length != src.length) {
        val subArray:ByteArray = src.asInstanceOf[narr.NArr[Byte]].slice(srcPos, srcPos + length).asInstanceOf[ByteArray]
        dest.set(subArray, destPos)
      } else dest.set(src, destPos)
    }
    inline def copyShortArray(src: ShortArray, srcPos: Int, dest: ShortArray, destPos: Int, length:Int): Unit = {
      if (srcPos != 0 || length != src.length) {
        val subArray:ShortArray = src.asInstanceOf[narr.NArr[Short]].slice(srcPos, srcPos + length).asInstanceOf[ShortArray]
        dest.set(subArray, destPos)
      } else dest.set(src, destPos)
    }
    inline def copyIntArray(src: IntArray, srcPos: Int, dest: IntArray, destPos: Int, length:Int): Unit = {
      if (srcPos != 0 || length != src.length) {
        val subArray:IntArray = src.asInstanceOf[narr.NArr[Int]].slice(srcPos, srcPos + length).asInstanceOf[IntArray]
        dest.set(subArray, destPos)
      } else dest.set(src, destPos)
    }
    inline def copyFloatArray(src: FloatArray, srcPos: Int, dest: FloatArray, destPos: Int, length:Int): Unit = {
      if (srcPos != 0 || length != src.length) {
        val subArray:FloatArray = src.asInstanceOf[narr.NArr[Float]].slice(srcPos, srcPos + length).asInstanceOf[FloatArray]
        dest.set(subArray, destPos)
      }
      else dest.set(src, destPos)
    }
    inline def copyDoubleArray(src: DoubleArray, srcPos: Int, dest: DoubleArray, destPos: Int, length:Int): Unit = {
      if (srcPos != 0 || length != src.length) {
        val subArray:DoubleArray = src.asInstanceOf[narr.NArr[Double]].slice(srcPos, srcPos + length).asInstanceOf[DoubleArray]
        dest.set(subArray, destPos)
      }
      else dest.set(src, destPos)
    }
    inline def copyNativeArray[T](src: NativeArray[T], srcPos: Int, dest: NativeArray[T], destPos: Int, length:Int): Unit = {
      //val availableSpace = dest.length - destPos
      //if (length > availableSpace) throw Exception(s"Can't copy source array into destination.  The source is too long.")
      var i: Int = 0; while (i < length) {
        dest(destPos + i) = src(srcPos + i)
        i = i + 1
      }
    }

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
    def copyAs[T, B >: T](original: NArray[T], newLength: Int)(using ClassTag[B]): NArray[B] = {
      val out = narr.NArray.ofSize[B](newLength)
      narr.NArray.copy(original.asInstanceOf[NArray[B]], out, 0)
      out
    }

    /** Copy one array to another, truncating or padding with default values (if
     * necessary) so the copy has the specified length.
     *
     * Equivalent to Java's
     * `java.util.Arrays.copyOf(original, newLength)`,
     * except that this works for primitive and object arrays in a single method.
     *
     * @see `java.util.Arrays#copyOf`
     */
    def copyOf[T: ClassTag](original: NArray[T], newLength: Int): NArray[T] = {
      val cp = narr.NArray.ofSize[T](newLength)
      println(s"cp = $cp, newLength = $newLength")
      narr.NArray.copy[T]( original, cp, 0 )
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
    def unzip[T, A1, A2](a: NArray[T])(using asPair: T => (A1, A2), ct1: ClassTag[A1], ct2: ClassTag[A2]): (NArray[A1], NArray[A2]) = {
      val a1 = narr.NArray.ofSize[A1](a.length)
      val a2 = narr.NArray.ofSize[A2](a.length)
      var i = 0
      while (i < a.length) {
        val e = asPair(a(i))
        a1(i) = e._1
        a2(i) = e._2
        i += 1
      }
      (a1, a2)
    }


    /** Partitions this array into a map of arrays according to some discriminator function.
     *
     * @param f the discriminator function.
     * @tparam K the type of keys returned by the discriminator function.
     * @return A map from keys to arrays such that the following invariant holds:
     * {{{
     *                 (xs groupBy f)(k) = xs filter (x => f(x) == k)
     *                  }}}         *               That is, every key `k` is bound to an array         nts `x`
     *               for which `f(x)` equals `k`.
     */
    def groupBy[T, K](a: NArray[T], f: T => K)(using ClassTag[T]): scala.collection.immutable.Map[K, NArray[T]] = {
      val m = scala.collection.mutable.Map.empty[K, NArrayBuilder[T]]
      val len = a.length
      var i = 0
      while(i < len) {
        val elem = a(i)
        val key = f(elem)
        val bldr = m.getOrElseUpdate(key, NArrayBuilder[T]())
        bldr += elem
        i += 1
      }
      m.view.mapValues(_.result).toMap
    }
  }

  @nowarn("msg=unused implicit parameter")
  inline def makeNativeArrayOfSize[A](n:Int)(using ClassTag[A]):NativeArray[A] = (new scala.scalajs.js.Array[Any](n)).asInstanceOf[NativeArray[A]]

}
