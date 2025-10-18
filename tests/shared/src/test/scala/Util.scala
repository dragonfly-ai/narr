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

  enum NArrayType:
    case BYTE_ARRAY, SHORT_ARRAY, INT_ARRAY, FLOAT_ARRAY, DOUBLE_ARRAY, NATIVE_ARRAY
  import NArrayType.*

  def getNArrayType[T](narr: NArray[T]): NArrayType = {
    narr match {
      case _: ByteArray => BYTE_ARRAY
      case _: ShortArray => SHORT_ARRAY
      case _: IntArray => INT_ARRAY
      case _: FloatArray => FLOAT_ARRAY
      case _: DoubleArray => DOUBLE_ARRAY
      case _: NativeArray[?] => NATIVE_ARRAY
    }
  }

  def assertNArrayType[T](narr: NArray[T], expectedNArrType: NArrayType)(using loc: Location): Unit = {
    assertEquals[NArrayType, NArrayType](
      expectedNArrType,
      getNArrayType(narr)
    )
  }

  def assertNArrayEquality[T](nArr1: NArray[T], nArr2: NArray[T], nt:NArrayType)(using loc: Location): Unit = {
    assertEquals(nArr1.length, nArr2.length)
    assertEquals[NArrayType, NArrayType](getNArrayType[T](nArr1), nt)
    assertEquals[NArrayType, NArrayType](getNArrayType[T](nArr2), nt)
    var i: Int = 0
    while (i < nArr1.length) {
      assertEquals(nArr1(i), nArr2(i))
      i += 1
    }
  }

  def assertArrayEquality[T](nArr1: Array[T], nArr2: Array[T])(using loc: Location): Unit = {
    assertEquals(nArr1.length, nArr2.length)
    var i: Int = 0
    while (i < nArr1.length) {
      assertEquals(nArr1(i), nArr2(i))
      i += 1
    }
  }

  def assertArray2NArrayEquality[T](arr: Array[T], nArr: NArray[T])(using loc: Location): Unit = {
    assertEquals(arr.length, nArr.length)
    var i: Int = 0
    while (i < arr.length) {
      val comparison:Boolean = arr(i) == nArr(i)
      assertEquals(comparison, true)
      i += 1
    }
  }

  def assertThrows[T <: Throwable]( f: () => Any ): Unit = assert({
    try {
      f()
      false
    } catch {
      case t: Throwable => true
      case _ => false
    }
  })

}
