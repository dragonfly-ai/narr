package narr

import scala.scalajs.js.typedarray.*
import scala.compiletime.*
import narr.native.Extensions.*
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

  inline def makeNativeArrayOfSize[A:ClassTag](n:Int):NativeArray[A] = new scala.scalajs.js.Array[A](n)

}
