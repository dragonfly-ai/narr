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

class CollectTest extends munit.FunSuite {

  val N:Int = 30

  private case class Collect[T:ClassTag](a: NArray[T], pf: PartialFunction[T, T], nt: NArrayType) {
    def test(): Unit = {
      val arr = a.toArray
      val a0 = a.collect(pf)
      assertNArrayType(a0, nt)
      assertArray2NArrayEquality(arr.collect(pf), a0)
      assertEquals[Option[T], Option[T]](a.toArray.collectFirst(pf), a.collectFirst(pf))
    }
  }

  // collect
  test("Collect[Byte]") {
    Collect[Byte](
      NArray.tabulate[Byte](N)((i: Int) => i.toByte),
      (b: Byte) => b % 2 match {
        case 0 => b
      },
      BYTE_ARRAY
    ).test()
  }

  test("Collect[Short]") {
    Collect[Short](
      NArray.tabulate[Short](N)((i: Int) => i.toShort),
      (s: Short) => s % 2 match {
        case 0 => s
      },
      SHORT_ARRAY
    ).test()
  }

  test("Collect[Int]") {
    Collect[Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i: Int) => i % 2 match {
        case 0 => i
      },
      INT_ARRAY
    ).test()
  }

  val r = new scala.util.Random()
  test("Collect[Float]") {
    Collect[Float](
      NArray.tabulate[Float](N)((i: Int) => r.nextFloat()),
      (f: Float) => f match {
        case f0 if f0 > 0.5f => f0
      },
      FLOAT_ARRAY
    ).test()
  }

  test("Collect[Double]") {
    Collect[Double](
      NArray.tabulate[Double](N)((i: Int) => r.nextDouble()),
      (d: Double) => d match {
        case d0 if d0 > 0.5 => d0
      },
      DOUBLE_ARRAY
    ).test()
  }

  test("Collect[String]") {
    Collect[String](
      NArray.tabulate[String](N)((i: Int) => r.nextString(i)),
      (s: String) => s match {
        case s0 if s0.length > N/2 => s0
      },
      NATIVE_ARRAY
    ).test()
  }
}