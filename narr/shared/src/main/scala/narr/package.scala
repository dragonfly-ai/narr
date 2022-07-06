package object narr {

  type NArray[T] = narr.native.NArray[T]
  val NArray: narr.native.NArray.type = narr.native.NArray

  export narr.native.Extensions.*
}
