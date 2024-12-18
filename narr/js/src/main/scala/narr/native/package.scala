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
      narr.NArray.copy( original.asInstanceOf[NArray[B]], narr.NArray.ofSize[B](newLength), 0 )
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
  }


  inline def makeNativeArrayOfSize[A](n:Int)(using ClassTag[A]):NativeArray[A] = (new scala.scalajs.js.Array[Any](n)).asInstanceOf[NativeArray[A]]

}
