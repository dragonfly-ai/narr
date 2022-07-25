package narr.native

import scala.scalajs.js
import scala.scalajs.js.annotation.JSBracketAccess
import scala.scalajs.js.typedarray.*
import scala.language.implicitConversions
import scala.reflect.ClassTag

// to imbue js.Array with scala.Array semantics


object Extensions {

  extension[T] (nArr:NArray[T]) {
    inline def length: Int = nArr.asInstanceOf[NArr[T]].length
    @JSBracketAccess inline def apply(i: Int): T = nArr.asInstanceOf[NArr[T]].apply(i)
    @JSBracketAccess inline def update(index: Int, value: T): Unit = nArr.asInstanceOf[NArr[T]].update(index, value)
  }

  @inline implicit def byteNArrayOps(xs: Int8Array): NArrayOps[Byte] = new TypedArrayOps[Byte](xs.asInstanceOf[NArr[Byte]])
  @inline implicit def shortNArrayOps(xs: Int16Array): NArrayOps[Short] = new TypedArrayOps[Short](xs.asInstanceOf[NArr[Short]])
  @inline implicit def intNArrayOps(xs: Int32Array): NArrayOps[Int] = new TypedArrayOps[Int](xs.asInstanceOf[NArr[Int]])
  @inline implicit def floatNArrayOps(xs: Float32Array): NArrayOps[Float] = new TypedArrayOps[Float](xs.asInstanceOf[NArr[Float]])
  @inline implicit def doubleNArrayOps(xs: Float64Array): NArrayOps[Double] = new TypedArrayOps[Double](xs.asInstanceOf[NArr[Double]])

  @inline implicit def booleanNArrayOps(xs: NArray[Boolean]): NArrayOps[Boolean] = new scala.scalajs.js.ArrayOps[Boolean](xs)
  @inline implicit def charNArrayOps(xs: NArray[Char]): NArrayOps[Char] = new scala.scalajs.js.ArrayOps[Char](xs)
  @inline implicit def longNArrayOps(xs: NArray[Long]): NArrayOps[Long] = new scala.scalajs.js.ArrayOps[Long](xs)

//  @inline def refNArrayOps[T](xs:NArray[T]): NArrayOps[T] = (xs match {
//    case xs0: Int8Array => new TypedArrayOps[Byte](xs0.asInstanceOf[NArr[Byte]])
//    case xs0: Int16Array => new TypedArrayOps[Short](xs0.asInstanceOf[NArr[Short]])
//    case xs0: Int32Array => new TypedArrayOps[Int](xs0.asInstanceOf[NArr[Int]])
//    case xs0: Float32Array => new TypedArrayOps[Float](xs0.asInstanceOf[NArr[Float]])
//    case xs0: Float64Array => new TypedArrayOps[Double](xs0.asInstanceOf[NArr[Double]])
//    case _ => new scalajs.js.ArrayOps[T](xs.asInstanceOf[scalajs.js.Array[T]])
//  }).asInstanceOf[NArrayOps[T]]

  @inline implicit def refNArrayOps[T <: Boolean | Char | AnyRef](xs: NArray[T]): NArrayOps[T] = new scala.scalajs.js.ArrayOps[T](xs.asInstanceOf[scala.scalajs.js.Array[T]]).asInstanceOf[NArrayOps[T]]

//  inline implicit def byteNArrayOps(xs: NArray[Byte]): NArrayOps[Byte] = new TypedArrayOps[Byte](xs.asInstanceOf[NArr[Byte]])
//  inline implicit def shortNArrayOps(xs: NArray[Short]): NArrayOps[Short] = new TypedArrayOps[Short](xs.asInstanceOf[NArr[Short]])
//  @inline implicit def intNArrayOps(xs: NArray[Int]): NArrayOps[Int] = new TypedArrayOps[Int](xs.asInstanceOf[NArr[Int]])
//  inline implicit def jsBigIntNArrayOps(xs: NArray[scala.scalajs.js.BigInt]): NArrayOps[scala.scalajs.js.BigInt] = new TypedArrayOps[scala.scalajs.js.BigInt](xs.asInstanceOf[NArr[scala.scalajs.js.BigInt]])
//  inline implicit def floatNArrayOps(xs: NArray[Float]): NArrayOps[Float] = new TypedArrayOps[Float](xs.asInstanceOf[NArr[Float]])
//  inline implicit def doubleNArrayOps(xs: NArray[Double]): NArrayOps[Double] = new TypedArrayOps[Double](xs.asInstanceOf[NArr[Double]])

//  inline implicit def booleanNArrayOps(xs: NArray[Boolean]): NArrayOps[Boolean] = new scala.scalajs.js.ArrayOps[Boolean](xs.asInstanceOf[scala.scalajs.js.Array[Boolean]])
//  inline implicit def charNArrayOps(xs: NArray[Char]): NArrayOps[Char] = new scala.scalajs.js.ArrayOps[Char](xs.asInstanceOf[scala.scalajs.js.Array[Char]])
//  inline implicit def longNArrayOps(xs: NArray[Long]): NArrayOps[Long] = new scala.scalajs.js.ArrayOps[Long](xs.asInstanceOf[scala.scalajs.js.Array[Long]])
//
//  inline implicit def stringNArrayOps(xs: NArray[String]): NArrayOps[String] = new scala.scalajs.js.ArrayOps[String](xs.asInstanceOf[scala.scalajs.js.Array[String]])


//  import scala.compiletime.*
//  inline implicit def genericNArrayOps[T](xs: NArray[T])(using ct:ClassTag[T]): NArrayOps[T] = (xs match {
//    case xs0: NArray[Byte] => new TypedArrayOps[Byte](xs0.asInstanceOf[NArr[Byte]])
//    case xs0: NArray[Short] => new TypedArrayOps[Short](xs0.asInstanceOf[NArr[Short]])
//    case xs0: NArray[Int] => new TypedArrayOps[Int](xs0.asInstanceOf[NArr[Int]])
//    case xs0: NArray[scala.scalajs.js.BigInt] => new TypedArrayOps[scala.scalajs.js.BigInt](xs0.asInstanceOf[NArr[scala.scalajs.js.BigInt]])
//    case xs0: NArray[Float] => new TypedArrayOps[Float](xs0.asInstanceOf[NArr[Float]])
//    case xs0: NArray[Double] => new TypedArrayOps[Double](xs0.asInstanceOf[NArr[Double]])
//    case xs0: NArray[Unit] => new scala.scalajs.js.ArrayOps[Unit](xs0)
//    case xs0: NArray[Boolean] => new scala.scalajs.js.ArrayOps[Boolean](xs0)
//    case xs0: NArray[Char] => new scala.scalajs.js.ArrayOps[Char](xs0)
//    case xs0: NArray[Long] => new scala.scalajs.js.ArrayOps[Long](xs0)
//    case xs0: NArray[String] => new scala.scalajs.js.ArrayOps[String](xs0)
//    case xs0: scala.scalajs.js.Array[NArray[t]] => new scala.scalajs.js.ArrayOps[NArray[t]](xs0)
//    //case xs0: NArray[AnyRef] => new scala.scalajs.js.ArrayOps[AnyRef](xs0)
//    case _ => new scala.scalajs.js.ArrayOps[T](xs.asInstanceOf[scala.scalajs.js.Array[T]])
//  }).asInstanceOf[NArrayOps[T]]


}