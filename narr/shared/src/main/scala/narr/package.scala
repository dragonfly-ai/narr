
package object narr {
  import scala.language.implicitConversions

  type TypedArrayPrimitive = narr.native.TypedArrayPrimitive

  type NArr[T] = narr.native.NArr[T]

  type NArray[T] = narr.native.NArray[T]
  type TArray[T <: TypedArrayPrimitive] = narr.native.TArray[T]

  val NArray: narr.native.NArray.type = narr.native.NArray
  type NArrayOps[T] = narr.native.NArrayOps[T]
  type TypedArrayOps[T <: TypedArrayPrimitive] = narr.native.TypedArrayOps[T]

  @inline implicit def nArray2NArr[T](nArr:NArray[T]): NArr[T] = nArr.asInstanceOf[NArr[T]]

  type JsArrayOps[T] = narr.native.JsArrayOps[T]

  val NArrayOps: narr.native.NArrayOps.type = narr.native.NArrayOps
  val Extensions: narr.native.Extensions.type = narr.native.Extensions
  export Extensions.*
}
