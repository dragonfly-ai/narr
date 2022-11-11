
import scala.compiletime.*
import narr.native.Extensions.*
import narr.native.Extensions.given

import scala.language.implicitConversions
import scala.reflect.ClassTag

package object narr {
  import scala.language.implicitConversions

  type ByteArray = narr.native.ByteArray
  type ShortArray = narr.native.ShortArray
  type IntArray = narr.native.IntArray
  type FloatArray = narr.native.FloatArray
  type DoubleArray = narr.native.DoubleArray
  type NativeArray[T] = narr.native.NativeArray[T]

  private inline def makeNativeArrayOfSize[A](n:Int)(using ClassTag[A]):NativeArray[A] = narr.native.makeNativeArrayOfSize[A](n:Int)

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
        out(i) = arr(0)
        i += 1
      }
      out
    }
  }

  type NArr[T] = narr.native.NArr[T]
  @inline implicit def nArray2NArr[T](nArr:NArray[T]): NArr[T] & NArray[T] = nArr.asInstanceOf[NArr[T] & NArray[T]]

  val Extensions: narr.native.Extensions.type = narr.native.Extensions
  export Extensions.*

}
