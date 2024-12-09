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

import Util.assertNArrayEquality
import narr.*

import scala.reflect.ClassTag
import scala.util.Random

class BuilderTest extends munit.FunSuite {
  val r:Random = new Random()

  class AddOneByOneTest[T: ClassTag](nab: NArrayBuilder[T], r0: () => T) {

    def runTests(): Unit = {

      val n: Int = 8192
      val truth: NArray[T] = NArray.tabulate[T](n)((i: Int) => r0())

      var i: Int = 0; while (i < n) {
        nab.addOne(truth(i))
        i = i + 1
      }
      val result: NArray[T] = nab.result
      assertEquals(result.length, n)
      assertNArrayEquality[T](truth, result)

      // accessor test
      var accumulator = true
      i = 0; while (i < n) {
        if (truth(i) != nab(i)) println(s"truth($i) == nab($i): ${truth(i)} == ${nab(i)}")
        accumulator = accumulator && (truth(i) == nab(i))
        i = i + 1
      }
      assert(accumulator)
    }
  }

  class AddAllTest[T: ClassTag](nab: NArrayBuilder[T], r0: () => T) {

    def runTests(): Unit = {

      val n: Int = 8192
      val truth: NArray[T] = NArray.tabulate[T](n)((i: Int) => r0())
      var i: Int = 0; while (i < n) {
        val j:Int = Math.min(r.nextInt(128), n - i)
        val s:NArray[T] = truth.slice( i, i + j )
        nab.addAll(s)
        i = i + j
      }
      val result: NArray[T] = nab.result
      assertEquals(result.length, n)
      assertNArrayEquality[T](truth, result)
    }
  }

  test(" NArrayBuilder[Boolean] ") {
    AddOneByOneTest[Boolean](NArrayBuilder[Boolean](), () => r.nextBoolean()).runTests()
    AddAllTest[Boolean](NArrayBuilder[Boolean](), () => r.nextBoolean()).runTests()
  }

  test(" NArrayBuilder[Byte] ") {
    AddOneByOneTest[Byte](NArrayBuilder[Byte](), () => r.nextBytes(1)(0)).runTests()
    AddAllTest[Byte](NArrayBuilder[Byte](), () => r.nextBytes(1)(0)).runTests()
  }

  test(" NArrayBuilder[Short] ") {
    AddOneByOneTest[Short](NArrayBuilder[Short](), () => r.nextInt().toShort).runTests()
    AddAllTest[Short](NArrayBuilder[Short](), () => r.nextInt().toShort).runTests()
  }

  test(" NArrayBuilder[Int] ") {
    AddOneByOneTest[Int](NArrayBuilder[Int](), () => r.nextInt()).runTests()
    AddAllTest[Int](NArrayBuilder[Int](), () => r.nextInt()).runTests()
  }

  test(" NArrayBuilder[Long] ") {
    AddOneByOneTest[Long](NArrayBuilder[Long](), () => r.nextLong()).runTests()
    AddAllTest[Long](NArrayBuilder[Long](), () => r.nextLong()).runTests()

  }

  test(" NArrayBuilder[Float] ") {
    AddOneByOneTest[Float](NArrayBuilder[Float](), () => r.nextFloat()).runTests()
    AddAllTest[Float](NArrayBuilder[Float](), () => r.nextFloat()).runTests()
  }

  test(" NArrayBuilder[Double] ") {
    AddOneByOneTest[Double](NArrayBuilder[Double](), () => r.nextDouble()).runTests()
    AddAllTest[Double](NArrayBuilder[Double](), () => r.nextDouble()).runTests()
  }

  test(" NArrayBuilder[String] ") {
    AddOneByOneTest[String](NArrayBuilder[String](), () => r.nextString(1 + r.nextInt(9))).runTests()
    AddAllTest[String](NArrayBuilder[String](), () => r.nextString(1 + r.nextInt(9))).runTests()
  }

  test(" NArrayBuilder[Unit] ") {
    AddOneByOneTest[Unit](NArrayBuilder[Unit](), () => ()).runTests()
    AddAllTest[Unit](NArrayBuilder[Unit](), () => ()).runTests()
  }

  // add a stress test.
}
