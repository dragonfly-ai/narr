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

import narr.*

object Util {

  import munit.*
  import munit.Assertions.*

  def assertNArrayEquality[T](nArr1: NArray[T], nArr2: NArray[T])(using loc: Location, compare: Compare[T, T]): Unit = {
    assertEquals(nArr1.length, nArr2.length)
    var i: Int = 0
    while (i < nArr1.length) {
      assertEquals(nArr1(i), nArr2(i))
      i += 1
    }
  }

  def assertThrows[T <: Throwable]( f: () => Any ): Unit = assert({
    try {
      f()
      false
    } catch {
      case t: T => true
      case _ => false
    }
  })

}
