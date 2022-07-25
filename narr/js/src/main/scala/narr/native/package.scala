package narr

import scala.scalajs.js.typedarray.*
import scala.compiletime.*
import narr.native.Extensions.*
import narr.native.Extensions.given

import scala.language.implicitConversions
import scala.reflect.ClassTag


package object native {

  export scala.scalajs.js.ArrayOps

  type JsArrayOps[T] = scala.scalajs.js.ArrayOps[T]

  type TypedArrayPrimitive = Byte | Short | Int | Float | Double // | scala.scalajs.js.BigInt

  type NativeTypedArray = Int8Array | Int16Array | Int32Array | Float32Array | Float64Array // | BigInt64Array | TypedArray[_, _]

  type TArray[T <: TypedArrayPrimitive] = T match
    case Byte => Int8Array
    case Short => Int16Array
    case Int => Int32Array
    case Float => Float32Array
    case Double => Float64Array

  type TArrayOps[T <: TypedArrayPrimitive] = T match
    case Byte => TypedArrayOps[Byte]
    case Short => TypedArrayOps[Short]
    case Int => TypedArrayOps[Int]
    case Float => TypedArrayOps[Float]
    case Double => TypedArrayOps[Double]

  object TArray {

    def apply[T <: TypedArrayPrimitive](elem: T*): TArray[T] = NArray.apply[T](elem:_*).asInstanceOf[TArray[T]]
    inline def ofSize[T <: TypedArrayPrimitive](length: Int): TArray[T] = NArray.ofSize[T](length).asInstanceOf[TArray[T]]
    inline def fill[T <: TypedArrayPrimitive](length: Int)(t: T): TArray[T] = NArray.fill[T](length)(t).asInstanceOf[TArray[T]]
    inline def tabulate[T <: TypedArrayPrimitive](length: Int)(f: Int => T): TArray[T] = NArray.tabulate[T](length)(f).asInstanceOf[TArray[T]]
    inline def from[T <: TypedArrayPrimitive](arr:Array[T]): TArray[T] = NArray.from[T](arr).asInstanceOf[TArray[T]]

    /** Copy one TArray to another.
     *  Based on private method `scala.Array.slowCopy`
     *  @param src the source array.
     *  @param srcPos  starting position in the source array.
     *  @param dest destination array.
     *  @param destPos starting position in the destination array.
     *  @param length the number of array elements to be copied.
     */
    inline def copy[T <: TypedArrayPrimitive, AT <: TypedArray[T, AT]](src : AT, srcPos : Int, dest : AT, destPos : Int, length : Int): Unit = {
      var i = srcPos
      var j = destPos
      val srcUntil = srcPos + length
      while (i < srcUntil) {
        dest(j) = src(i)
        i += 1
        j += 1
      }
    }
  }

  type NArray[T] = T match
    case TypedArrayPrimitive => TArray[T]
//    case NArray[t] => scala.scalajs.js.Array[NArray[t]]
    case _ => scala.scalajs.js.Array[T]

  object NArray {

    def apply[T](elem: T*): NArray[T] = tabulate[T](elem.size)((i: Int) => elem(i))

    inline def ofSize[T](length: Int): NArray[T] = (inline erasedValue[T] match {
      case _: Byte => new scala.scalajs.js.typedarray.Int8Array(length)
      case _: Short => new scala.scalajs.js.typedarray.Int16Array(length)
      case _: Int => new scala.scalajs.js.typedarray.Int32Array(length)
      case _: Float => new scala.scalajs.js.typedarray.Float32Array(length)
      case _: Double => new scala.scalajs.js.typedarray.Float64Array(length)
      case _ => new scala.scalajs.js.Array[T](length)
    }).asInstanceOf[NArray[T]]

    inline def fill[T](length: Int)(t: T): NArray[T] = {
      val out: NArray[T] = ofSize[T](length)
      var i: Int = 0
      while (i < length) {
        out(i) = t
        i += 1
      }

      out
    }

    inline def tabulate[T](length: Int)(f: Int => T): NArray[T] = {
      val out: NArray[T] = ofSize[T](length)
      var i: Int = 0
      while (i < length) {
        out(i) = f(i)
        i += 1
      }
      out
    }

    inline def from[T](arr:Array[T]):NArray[T] = {
      val out:NArray[T] = ofSize[T](arr.length)
      var i:Int = 0
      while (i < arr.length) {
        out(i) = arr(0)
        i += 1
      }
      out
    }


    /** Copy one NArray to another.
     *  Based on private method `scala.Array.slowCopy`
     *  @param src the source array.
     *  @param srcPos  starting position in the source array.
     *  @param dest destination array.
     *  @param destPos starting position in the destination array.
     *  @param length the number of array elements to be copied.
     */
    inline def copy[T](src : scala.scalajs.js.Array[T], srcPos : Int, dest : scala.scalajs.js.Array[T], destPos : Int, length : Int): Unit = {
      var i = srcPos
      var j = destPos
      val srcUntil = srcPos + length
      while (i < srcUntil) {
        dest(j) = src(i)
        i += 1
        j += 1
      }
    }
  }

//  @inline final class What[T](i:T) { inline def what(a: NArray[T]): Unit = println(a.size) }
//  new What[Long](1).what(NArray[Long](1L, 2L, 3L, 4L))

  object NArrayOps {
//    implicit inline def byteNArrayOps(xs:NArray[Byte]): TypedArrayOps[Byte] = new TypedArrayOps[Byte](xs.asInstanceOf[NArr[Byte]])
//    implicit inline def shortNArrayOps(xs:NArray[Short]): TypedArrayOps[Short] = new TypedArrayOps[Short](xs.asInstanceOf[NArr[Short]])
//    implicit inline def intNArrayOps(xs:NArray[Int]): TypedArrayOps[Int] = new TypedArrayOps[Int](xs.asInstanceOf[NArr[Int]])
//    implicit inline def floatNArrayOps(xs:NArray[Float]): TypedArrayOps[Float] = new TypedArrayOps[Float](xs.asInstanceOf[NArr[Float]])
//    implicit inline def doubleNArrayOps(xs:NArray[Double]): TypedArrayOps[Double] = new TypedArrayOps[Double](xs.asInstanceOf[NArr[Double]])
//
//    implicit inline def anyNArrayOps[T](xs:scalajs.js.Array[T]): scalajs.js.ArrayOps[T] = new scalajs.js.ArrayOps[T](xs.asInstanceOf[scalajs.js.Array[T]])

//    inline def nArray2Ops[T](xs:NArray[T]): NArrayOps[T] = (xs match {
//      case xs0: Byte => new TypedArrayOps[Byte](xs0.asInstanceOf[NArr[Byte]])
//      case xs0: Short => new TypedArrayOps[Short](xs0.asInstanceOf[NArr[Short]])
//      case xs0: Int => new TypedArrayOps[Int](xs0.asInstanceOf[NArr[Int]])
//      case xs0: Float => new TypedArrayOps[Float](xs0.asInstanceOf[NArr[Float]])
//      case xs0: Double => new TypedArrayOps[Double](xs0.asInstanceOf[NArr[Double]])
//      case _ => new scalajs.js.ArrayOps[T](xs.asInstanceOf[scalajs.js.Array[T]])

//      case xs0: Int8Array => new TypedArrayOps[Byte](xs0.asInstanceOf[NArr[Byte]])
//      case xs0: Int16Array => new TypedArrayOps[Short](xs0.asInstanceOf[NArr[Short]])
//      case xs0: Int32Array => new TypedArrayOps[Int](xs0.asInstanceOf[NArr[Int]])
//      case xs0: Float32Array => new TypedArrayOps[Float](xs0.asInstanceOf[NArr[Float]])
//      case xs0: Float64Array => new TypedArrayOps[Double](xs0.asInstanceOf[NArr[Double]])
//      case _ => new scalajs.js.ArrayOps[T](xs.asInstanceOf[scalajs.js.Array[T]])
//    }).asInstanceOf[NArrayOps[T]]

  }



  type NArrayOps[T] = T match
    case Byte => TypedArrayOps[Byte]
    case Short => TypedArrayOps[Short]
    case Int => TypedArrayOps[Int]
    case Float => TypedArrayOps[Float]
    case Double => TypedArrayOps[Double]
    case _ => scala.scalajs.js.ArrayOps[T]

//  @inline
//  final class Int8ArrayOps(private val xs0: Int8Array) {
//    val tArrOps:TArrayOps[Byte] = new TArrayOps[Byte](xs0)
//    export tArrOps.*
//  }
//  @inline
//  final class Int16ArrayOps(private val xs0: Int16Array) {
//    val tArrOps:TArrayOps[Short] = new TArrayOps[Short](xs0)
//    export tArrOps.*
//  }
//  @inline
//  final class Int32ArrayOps(private val xs0: Int32Array) {
//    val tArrOps:TArrayOps[Int] = new TArrayOps[Int](xs0)
//    export tArrOps.*
//  }
//  @inline
//  final class BigInt64ArrayOps(private val xs0: BigInt64Array) {
//    val tArrOps:TArrayOps[scala.scalajs.js.BigInt] = new TArrayOps[scala.scalajs.js.BigInt](xs0)
//    export tArrOps.*
//  }
//  @inline
//  final class Float32ArrayOps(private val xs0: Float32Array) {
//    val tArrOps:TArrayOps[Float] = new TArrayOps[Float](xs0)
//    export tArrOps.*
//  }
//  @inline
//  final class Float64ArrayOps(private val xs0: Float64Array) {
//    val tArrOps:TArrayOps[Double] = new TArrayOps[Double](xs0)
//    export tArrOps.*
//  }
}
