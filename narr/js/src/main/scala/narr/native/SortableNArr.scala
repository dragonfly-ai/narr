package narr.native

import scala.scalajs.js


import scala.scalajs.js
import scala.scalajs.js.annotation.JSBracketAccess

@js.native
trait SortableNArr[T] extends js.Object {
  def sort(): NArr[T] = js.native
  def sort(compareFunction: js.Function2[T, T, Int]): NArr[T] = js.native

  
}