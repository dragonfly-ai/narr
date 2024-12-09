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

import scala.compiletime.*
import narr.native.Extensions.*

import scala.language.implicitConversions
import scala.reflect.ClassTag

package object narr {

  type ByteArray = narr.native.ByteArray
  type ShortArray = narr.native.ShortArray
  type IntArray = narr.native.IntArray
  type FloatArray = narr.native.FloatArray
  type DoubleArray = narr.native.DoubleArray
  type NativeArray[T] = narr.native.NativeArray[T]

  private inline def makeNativeArrayOfSize[A:ClassTag](n:Int):NativeArray[A] = narr.native.makeNativeArrayOfSize[A](n:Int)

  type NativeTypedArray = ByteArray | ShortArray | IntArray | FloatArray | DoubleArray

  type TypedArrayPrimitive = Byte | Short | Int | Float | Double

  type ArrayElementType[T <: NativeTypedArray | NativeArray[T]] = T match
    case ByteArray => Byte
    case ShortArray => Short
    case IntArray => Int
    case FloatArray => Float
    case DoubleArray => Double
    case NativeArray[T] => T

  type NArray[T] = narr.native.NArray[T]

  //val NArray: narr.native.NArray.type = narr.native.NArray

  object NArray {

    def apply[A](elem: A*)(using ClassTag[A]): NArray[A] = tabulate[A](elem.size)((i: Int) => elem(i))

    inline def empty[A](using ClassTag[A]): NArray[A] = ofSize[A](0)

    transparent inline def ofSize[A](length: Int)(using ClassTag[A]): NArr[A] & NArray[A] = (inline erasedValue[A] match {
      case _: Byte => new ByteArray(length)
      case _: Short => new ShortArray(length)
      case _: Int => new IntArray(length)
      case _: Float => new FloatArray(length)
      case _: Double => new DoubleArray(length)
      case _ => makeNativeArrayOfSize[A](length)
    }).asInstanceOf[NArr[A] & NArray[A]]

    def copy[T](nArr: NArray[T]): NArray[T] = nArr.slice(0, nArr.length)

    /** Copy one array to another.
     *
     * Note that the passed-in `dest` array will be modified by this call.
     *
     * @param src     the source array.
     * @param dest    destination array.
     * @param destPos starting position in the destination array.
     * @see `java.lang.System#arraycopy`
     */
    inline def copyByteArray(src: ByteArray, dest: ByteArray, destPos: Int): Unit = native.NArray.copyByteArray(src, 0, dest, destPos, src.length)
    inline def copyShortArray(src: ShortArray, dest: ShortArray, destPos: Int): Unit = native.NArray.copyShortArray(src, 0, dest, destPos, src.length)
    inline def copyIntArray(src: IntArray, dest: IntArray, destPos: Int): Unit = native.NArray.copyIntArray(src, 0, dest, destPos, src.length)
    inline def copyFloatArray(src: FloatArray, dest: FloatArray, destPos: Int): Unit = native.NArray.copyFloatArray(src, 0, dest, destPos, src.length)
    inline def copyDoubleArray(src: DoubleArray, dest: DoubleArray, destPos: Int): Unit = native.NArray.copyDoubleArray(src, 0, dest, destPos, src.length)
    inline def copyNArray[T](src: NArray[T], dest: NArray[T], destPos: Int): Unit = native.NArray.copyNArray(src, 0, dest, destPos, src.length)

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
    inline def copyByteArray(src: ByteArray, srcPos: Int, dest: ByteArray, destPos: Int, length: Int): Unit = native.NArray.copyByteArray(src, srcPos, dest, destPos, length)
    inline def copyShortArray(src: ShortArray, srcPos: Int, dest: ShortArray, destPos: Int, length: Int): Unit = native.NArray.copyShortArray(src, srcPos, dest, destPos, length)
    inline def copyIntArray(src: IntArray, srcPos: Int, dest: IntArray, destPos: Int, length: Int): Unit = native.NArray.copyIntArray(src, srcPos, dest, destPos, length)
    inline def copyFloatArray(src: FloatArray, srcPos: Int, dest: FloatArray, destPos: Int, length: Int): Unit = native.NArray.copyFloatArray(src, srcPos, dest, destPos, length)
    inline def copyDoubleArray(src: DoubleArray, srcPos: Int, dest: DoubleArray, destPos: Int, length: Int): Unit = native.NArray.copyDoubleArray(src, srcPos, dest, destPos, length)
    inline def copyNArray[T](src: NArray[T], srcPos: Int, dest: NArray[T], destPos: Int, length: Int): Unit = native.NArray.copyNArray(src, srcPos, dest, destPos, length)

    inline def fill[A](length: Int)(t: A)(using ClassTag[A]): NArray[A] = {
      val out: NArray[A] = ofSize[A](length)
      var i: Int = 0
      while (i < length) {
        out(i) = t
        i += 1
      }

      out
    }

    inline def tabulate[A](length: Int)(f: Int => A)(using ClassTag[A]): NArray[A] = {
      val out: NArray[A] = ofSize[A](length)
      var i: Int = 0
      while (i < length) {
        out(i) = f(i)
        i += 1
      }
      out
    }

    transparent inline def from[A](arr: Array[A])(using ClassTag[A]): NArray[A] = {
      val out: NArray[A] = ofSize[A](arr.length)
      var i: Int = 0
      while (i < arr.length) {
        out(i) = arr(i)
        i += 1
      }
      out
    }
  }

  type NArr[T] = narr.native.NArr[T]

  type SortableNArr[T] = narr.native.SortableNArr[T]

  @inline implicit def nArray2NArr[T](nArr:NArray[T]): NArr[T] & NArray[T] = nArr.asInstanceOf[NArr[T] & NArray[T]]

  val Extensions: narr.native.Extensions.type = narr.native.Extensions
  export Extensions.*
  export Extensions.given

}
