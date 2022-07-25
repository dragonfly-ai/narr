package narr.native

import scala.scalajs.js
import scala.scalajs.js.annotation.JSBracketAccess

@js.native
trait NArr[T] extends js.Object {
  def length:Int = js.native
  @JSBracketAccess def apply(i:Int):T = js.native
  @JSBracketAccess def update(index: Int, value: T): Unit = js.native
}