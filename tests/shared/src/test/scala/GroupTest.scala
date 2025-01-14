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

class GroupTest extends munit.FunSuite {

  val N:Int = 30

  private case class GroupByTest[T, K](a: NArray[T], f: Function1[T, K], nt: NArrayType)(using ClassTag[T]) {

    def test(): Unit = {
      val aG = a.groupBy[K](f)
      val arr = a.toArray[T]
      val arrG = arr.groupBy[K](f)
      assertEquals(aG.size, arrG.size)
      for (k <- aG.keySet) {
        assertArray2NArrayEquality[T](arrG(k), aG(k))
        assertNArrayType[T](aG(k), nt)
      }
    }
  }


  private case class GroupMapTest[T, K, B](a: NArray[T], fk: Function1[T, K], ft: Function1[T, B], nt: NArrayType)(
    using ClassTag[T], ClassTag[B]
  ) {

    def test(): Unit = {
      val aG = a.groupMap[K,B](fk)(ft)
      val arr = a.toArray[T]
      val arrG = arr.groupMap[K,B](fk)(ft)
      assertEquals(aG.size, arrG.size)
      for (k <- aG.keySet) {
        assertArray2NArrayEquality[B](arrG(k), aG(k))
        assertNArrayType[B](aG(k), nt)
      }
    }
  }

  // groupBy

  test("GroupByTest[Byte, Int]") {
    GroupByTest[Byte, Int](
      NArray.tabulate[Byte](N)((i: Int) => i.toByte),
      (b: Byte) => b % 3,
      BYTE_ARRAY
    ).test()
  }

  test("GroupByTest[Short, Int]") {
    GroupByTest[Short, Int](
      NArray.tabulate[Short](N)((i: Int) => i.toShort),
      (s: Short) => s % 3,
      SHORT_ARRAY
    ).test()
  }

  test("GroupByTest[Int, Int]") {
    GroupByTest[Int, Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i: Int) => i % 3,
      INT_ARRAY
    ).test()
  }

  test("GroupByTest[Float, Int]") {
    GroupByTest[Float, Int](
      NArray.tabulate[Float](N)((i: Int) => i.toFloat),
      (f: Float) => f.toInt % 3,
      FLOAT_ARRAY
    ).test()
  }

  test("GroupByTest[Double, Int]") {
    GroupByTest[Double, Int](
      NArray.tabulate[Double](N)((i: Int) => i.toDouble),
      (d: Double) => d.toInt % 3,
      DOUBLE_ARRAY
    ).test()
  }

  // groupMap

  test("GroupMapTest[Int, String, Byte]") {
    GroupMapTest[Int, String, Byte](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i: Int) => (i % 3).toString,
      (i: Int) => i.toByte,
      BYTE_ARRAY
    ).test()
  }

  test("GroupMapTest[Int, String, Short]") {
    GroupMapTest[Int, String, Short](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i: Int) => (i % 3).toString,
      (i: Int) => i.toShort,
      SHORT_ARRAY
    ).test()
  }

  test("GroupMapTest[Int, String, Int]") {
    GroupMapTest[Int, String, Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i: Int) => (i % 3).toString,
      (i: Int) => i*2,
      INT_ARRAY
    ).test()
  }

  test("GroupMapTest[Int, String, Float]") {
    GroupMapTest[Int, String, Float](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i: Int) => (i % 3).toString,
      (i: Int) => i.toFloat,
      FLOAT_ARRAY
    ).test()
  }

  test("GroupMapTest[Int, String, Double]") {
    GroupMapTest[Int, String, Double](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i: Int) => (i % 3).toString,
      (i: Int) => i.toDouble,
      DOUBLE_ARRAY
    ).test()
  }
}