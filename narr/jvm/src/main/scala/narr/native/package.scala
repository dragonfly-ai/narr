package narr

import narr.native.NArr

import scala.collection.ArrayOps
import scala.reflect.ClassTag

package object native {

  type TypedArrayPrimitive = Byte | Short | Int | Float | Double

  type NArray[T] = Array[T]
  type TArray[T <: TypedArrayPrimitive] = Array[T]
  type NArrayOps[T] = ArrayOps[T]
  type TypedArrayOps[T] = ArrayOps[T]
  type NArr[T] = Array[T]

  type JsArrayOps[T] = ArrayOps[T]

  object NArrayOps {
//    inline def apply[T](xs:NArray[T]): NArrayOps[T] = ArrayOps[T](xs)


  }

  object NArray {
    export Array.*
  }

  object Extensions {
    inline def byteNArrayOps(xs:NArray[Byte]): TypedArrayOps[Byte] = new ArrayOps[Byte](xs)
    inline def shortNArrayOps(xs:NArray[Short]): TypedArrayOps[Short] = new ArrayOps[Short](xs)
    inline def intNArrayOps(xs:NArray[Int]): TypedArrayOps[Int] = new ArrayOps[Int](xs)
    inline def floatNArrayOps(xs:NArray[Float]): TypedArrayOps[Float] = new ArrayOps[Float](xs)
    inline def doubleNArrayOps(xs:NArray[Double]): TypedArrayOps[Double] = new ArrayOps[Double](xs)

    inline def refNArrayOps[T <: Any](xs:NArray[T]): NArrayOps[T] = new ArrayOps[T](xs)
  }
}
