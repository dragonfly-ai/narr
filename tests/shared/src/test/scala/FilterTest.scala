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

class FilterTest extends munit.FunSuite {

  val N: Int = 30

  private case class TestFilterAndFilterNot[T: ClassTag](a: NArray[T], f: T => Boolean, nt: NArrayType) {
    def test(): Unit = {
      val (aL: NArray[T], aR: NArray[T]) = (a.filter(f), a.filterNot(f))
      assertNArrayType(aL, nt)
      assertNArrayType(aR, nt)
      assertEquals(aL.length + aR.length, a.length)
      val (arrL: Array[T], arrR: Array[T]) = (a.toArray.filter(f), a.toArray.filterNot(f))
      assertArray2NArrayEquality(arrL, aL)
      assertArray2NArrayEquality(arrR, aR)
    }
  }

  test("TestFilterAndFilterNot[Boolean]") {
    TestFilterAndFilterNot[Boolean](
      NArray.tabulate[Boolean](N)((i: Int) => i % 2 == 0),
      (b: Boolean) => b,
      NATIVE_ARRAY
    ).test()
  }

  test("TestFilterAndFilterNot[Byte]") {
    TestFilterAndFilterNot[Byte](
      NArray.tabulate[Byte](N)((i: Int) => i.toByte),
      (b: Byte) => b % 2 == 0,
      BYTE_ARRAY
    ).test()
  }

  test("TestFilterAndFilterNot[Short]") {
    TestFilterAndFilterNot[Short](
      NArray.tabulate[Short](N)((i: Int) => i.toShort),
      (s: Short) => s % 2 == 0,
      SHORT_ARRAY
    ).test()
  }

  test("TestFilterAndFilterNot[Int]") {
    TestFilterAndFilterNot[Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i: Int) => i % 2 == 0,
      INT_ARRAY
    ).test()
  }

  val rs = new Random()

  test("TestFilterAndFilterNot[Float]") {
    TestFilterAndFilterNot[Float](
      NArray.tabulate[Float](N)((_: Int) => rs.nextFloat()),
      (f: Float) => f > 0.5f,
      FLOAT_ARRAY
    ).test()
  }

  test("TestFilterAndFilterNot[Double]") {
    TestFilterAndFilterNot[Double](
      NArray.tabulate[Double](N)((_: Int) => rs.nextDouble()),
      (d: Double) => d > 0.5,
      DOUBLE_ARRAY
    ).test()
  }

}