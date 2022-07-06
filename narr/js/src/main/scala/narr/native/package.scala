package narr

import scala.scalajs.js
import scala.scalajs.js.typedarray.*
import scala.compiletime.*

package object native {

  type NArray[T] = T match
    case Byte => Int8Array
    case Short => Int16Array
    case Int => Int32Array
    case Float => Float32Array
    case Double => Float64Array
    case NArray[t] => js.Array[NArray[t]]
    case _ => js.Array[T]

  object NArray {

    def apply[T](elem: T*): NArray[T] = tabulate[T](elem.size)((i: Int) => elem(i))

    inline def fill[T](length: Int)(t: T): NArray[T] = {
      val out: NArray[T] = ofSize[T](length)
      var i: Int = 0
      while (i < length) {
        out(i) = t
        i += 1
      }

      out
    }

    inline def ofSize[T](length: Int): NArray[T] = (inline erasedValue[T] match {
      case _: Byte => new Int8Array(length)
      case _: Short => new Int16Array(length)
      case _: Int => new Int32Array(length)
      case _: Float => new Float32Array(length)
      case _: Double => new Float64Array(length)
      case _ => new js.Array[T](length)
    }).asInstanceOf[NArray[T]]

    inline def tabulate[T](length: Int)(f: Int => T): NArray[T] = {
      val out: NArray[T] = ofSize[T](length)
      var i: Int = 0
      while (i < length) {
        out(i) = f(i)
        i += 1
      }
      out
    }

  }

}
