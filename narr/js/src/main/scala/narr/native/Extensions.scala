package narr.native

import narr.{TypedArrayOps, NativeTypedArray}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSBracketAccess
import scala.scalajs.js.typedarray.*
import scala.language.implicitConversions
import scala.reflect.ClassTag

// to imbue js.Array with scala.Array semantics


object Extensions {

  extension[T] (nArray:NArray[T]) {
    inline def length: Int = nArray.asInstanceOf[NArr[T]].length
    @JSBracketAccess inline def apply(i: Int): T = nArray.asInstanceOf[NArr[T]].apply(i)
    @JSBracketAccess inline def update(index: Int, value: T): Unit = nArray.asInstanceOf[NArr[T]].update(index, value)

  }

}