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

import Util.*
import Util.NArrayType.*

import scala.reflect.ClassTag
import scala.util.Random

class FlatMapTest extends munit.FunSuite {

  val N:Int = 30

  private case class TestFlatMap[T, B >: T](a: NArray[T], f: T => IterableOnce[B], nt: NArrayType)(using ClassTag[T], ClassTag[B]) {
    def test(): Unit = {
      val afltnd = a.flatMap(f)
      assertNArrayType(afltnd, nt)
      assertArray2NArrayEquality(a.toArray[T].flatMap(f), afltnd)
    }
  }

  test("TestFlatMap[Byte, Byte]") {
    TestFlatMap[Byte, Byte](
      NArray.tabulate[Byte](N)((i: Int) => i.toByte),
      (b: Byte) => NArray.tabulate[Byte](b.toInt)((i: Int) => i.toByte),
      BYTE_ARRAY
    ).test()
  }

  test("TestFlatMap[Short, Short]") {
    TestFlatMap[Short, Short](
      NArray.tabulate[Short](N)((i: Int) => i.toShort),
      (s: Short) => NArray.tabulate[Short](s.toInt)((i: Int) => i.toShort),
      SHORT_ARRAY
    ).test()
  }

  test("TestFlatMap[Int, Int]") {
    TestFlatMap[Int, Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i: Int) => NArray.tabulate[Int](N)((i: Int) => i),
      INT_ARRAY
    ).test()
  }

  test("TestFlatMap[Float, Float]") {
    TestFlatMap[Float, Float](
      NArray.tabulate[Float](N)((i: Int) => i.toFloat),
      (f: Float) => NArray.tabulate[Float](f.toInt)((i: Int) => i.toFloat),
      FLOAT_ARRAY
    ).test()
  }

  test("TestFlatMap[Double, Double]") {
    TestFlatMap[Double, Double](
      NArray.tabulate[Double](N)((i: Int) => i.toDouble),
      (d: Double) => NArray.tabulate[Double](d.toInt)((i: Int) => i.toDouble),
      DOUBLE_ARRAY
    ).test()
  }

  test("TestFlatMap[String, String]") {
    val rs = new Random()
    TestFlatMap[String, String](
      NArray.tabulate[String](N)((i: Int) => rs.nextString(i)),
      (s: String) => NArray.tabulate[String](s.length)((i: Int) => s"FooBar${s.charAt(i)}"),
      NATIVE_ARRAY
    ).test()
  }

}