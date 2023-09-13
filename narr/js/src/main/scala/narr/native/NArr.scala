/*
 * Copyright 2023 dragonfly.ai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package narr.native

import scala.scalajs.js
import scala.scalajs.js.annotation.JSBracketAccess

@js.native
trait NArr[T] extends js.Object {
  def length:Int = js.native
  @JSBracketAccess def apply(i:Int):T = js.native
  @JSBracketAccess def update(index: Int, value: T): Unit = js.native

  def slice(start:Int, end:Int): NArr[T] = js.native

}

