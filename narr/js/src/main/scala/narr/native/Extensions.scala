package narr.native

import scala.scalajs.js
import scala.scalajs.js.annotation.JSBracketAccess
import scala.scalajs.js.typedarray.*

// to imbue js.Array with scala.Array semantics

object Extensions {

  trait NArr[T, AT <: NArray[T]] {
    def length(arr:AT):Int
    @JSBracketAccess def apply(arr:AT)(i:Int):T
    @JSBracketAccess def update(arr:AT)(index: Int, value: T): Unit
    extension (nArr:AT)
      //inline def length:Int = length(nArr)
      inline def indices:Range = 0 until length(nArr)
      def forEach(f: js.Function1[T, Any]): Unit = {
        var i = 0
        val len = length(nArr)
        while (i < len) {
          f(apply(nArr)(i))
          i += 1
        }
      }
  }

  trait NativeTypedArray[T, AT <: TypedArray[T, AT]] extends NArr[T, NArray[T]] {
    override inline def length(arr: NArray[T]): Int = arr.asInstanceOf[TypedArray[T, AT]].length
    @JSBracketAccess override inline def apply(arr: NArray[T])(index: Int): T = arr.asInstanceOf[TypedArray[T, AT]](index)
    @JSBracketAccess override inline def update(arr: NArray[T])(index: Int, value: T): Unit = arr.asInstanceOf[TypedArray[T, AT]](index) = value
  }

  given NativeTypedArray[Byte, Int8Array] with NArr[Byte, Int8Array]
  given NativeTypedArray[Short, Int16Array] with NArr[Short, Int16Array]
  given NativeTypedArray[Int, Int32Array] with NArr[Int, Int32Array]
  given NativeTypedArray[Float, Float32Array] with NArr[Float, Float32Array]
  given NativeTypedArray[Double, Float64Array] with NArr[Double, Float64Array]

  extension[T] (nArr:NArray[T]) {
    inline def length: Int = (nArr.asInstanceOf[js.Array[T]]).length // lengthOf[T](nArr)
    @JSBracketAccess inline def apply(i: Int): T = (nArr.asInstanceOf[js.Array[T]]).apply(i)
    @JSBracketAccess inline def update(index: Int, value: T): Unit = (nArr.asInstanceOf[js.Array[T]]).update(index, value)
    inline def indices: Range = 0 until (nArr.asInstanceOf[js.Array[T]]).length
    def forEach(f: js.Function1[T, Any]): Unit = {
      val arr = (nArr.asInstanceOf[js.Array[T]])
      var i = 0
      val len = arr.length
      while (i < len) {
        f(arr(i))
        i += 1
      }
    }
  }


}