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

import Util.*
import Util.NArrayType.*
import narr.*

import scala.reflect.ClassTag
import scala.util.Random

class MapInPlaceTest extends munit.FunSuite {

  val N:Int = 30

  private case class TestMapInPlace[T](a: NArray[T], f: T => T)(using ClassTag[T]) {
    def test(): Unit = {
      assertArray2NArrayEquality(a.toArray[T].mapInPlace(f), a.mapInPlace(f))
    }
  }

  test("TestMapInPlace[Byte]") {
    TestMapInPlace[Byte](
      NArray.tabulate[Byte](N)((i: Int) => i.toByte),
      (b: Byte) => (b + 1).toByte
    ).test()
  }

  test("TestMapInPlace[Short]") {
    TestMapInPlace[Short](
      NArray.tabulate[Short](N)((i: Int) => i.toShort),
      (s: Short) => (s + 1).toShort
    ).test()
  }

  test("TestMapInPlace[Int]") {
    TestMapInPlace[Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i: Int) => i + 1
    ).test()
  }

  test("TestMapInPlace[Float]") {
    TestMapInPlace[Float](
      NArray.tabulate[Float](N)((i: Int) => i.toFloat),
      (f: Float) => f + 1f
    ).test()
  }

  test("TestMapInPlace[Double]") {
    TestMapInPlace[Double](
      NArray.tabulate[Double](N)((i: Int) => i.toDouble),
      (d: Double) => d + 1.0
    ).test()
  }

  test("TestMapInPlace[String]") {
    val rs = new Random()
    TestMapInPlace[String](
      NArray.tabulate[String](N)((i: Int) => rs.nextString(i)),
      (s: String) => s + s
    ).test()
  }

}