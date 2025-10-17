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

import munit.{Compare, Location}
import narr.*
import Util.*
import Util.NArrayType.*

import scala.annotation.nowarn
import scala.reflect.ClassTag
import scala.util.Random

class BuilderTest extends munit.FunSuite {
  val r:Random = new Random()
  val N: Int = 8192

  class AddOneByOneTest[T: ClassTag](nab: NArrayBuilder[T], values: NArray[T], nt: NArrayType) {

    def runTests(): Unit = {

      var i: Int = 0; while (i < N) {
        nab.addOne(values(i))
        i = i + 1
      }
      val result: NArray[T] = nab.result
      assertEquals(result.length, N)
      assertNArrayEquality[T](values, result, nt)

      // accessor test
      var accumulator = true
      i = 0; while (i < N) {
        if (values(i) != nab(i)) println(s"values($i) == nab($i): ${values(i)} == ${nab(i)}")
        accumulator = accumulator && (values(i) == nab(i))
        i = i + 1
      }
      assert(accumulator)
    }
  }

  class AddAllTest[T: ClassTag](nab: NArrayBuilder[T], values: NArray[T], nt: NArrayType) {

    def runTests(): Unit = {
      var i: Int = 0; while (i < N) {
        val j:Int = Math.min(r.nextInt(128), N - i)
        if (Math.random() > 0.5) {
          val s: NArray[T] = values.slice(i, i + j)
          nab.addAll(s)
          i = i + j
        } else {
          nab.addAll(values, i, j)
          i = i + j
        }
      }
      val result: NArray[T] = nab.result
      assertEquals(result.length, N)
      assertNArrayEquality[T](values, result, nt)
    }
  }

  private class BuilderResultTypeTest[T: ClassTag](expectedNArrType: NArrayType) {

    def runTests(): Unit = {
      // Indirect
      assertNArrayType[T](NArrayBuilder[T]().result, expectedNArrType)
    }
  }

  // builderFor
  test(" NArray.builderFor[]()") {
    // Direct
    assertNArrayType[Byte](NArrayBuilder.builderFor[Byte]().result, BYTE_ARRAY)
    assertNArrayType[Short](NArrayBuilder.builderFor[Short]().result, SHORT_ARRAY)
    assertNArrayType[Int](NArrayBuilder.builderFor[Int]().result, INT_ARRAY)
    assertNArrayType[Float](NArrayBuilder.builderFor[Float]().result, FLOAT_ARRAY)
    assertNArrayType[Double](NArrayBuilder.builderFor[Double]().result, DOUBLE_ARRAY)
    assertNArrayType[Unit](NArrayBuilder.builderFor[Unit]().result, NATIVE_ARRAY)
    assertNArrayType[Boolean](NArrayBuilder.builderFor[Boolean]().result, NATIVE_ARRAY)
    assertNArrayType[Long](NArrayBuilder.builderFor[Long]().result, NATIVE_ARRAY)
    assertNArrayType[String](NArrayBuilder.builderFor[String]().result, NATIVE_ARRAY)
    assertNArrayType[Any](NArrayBuilder.builderFor[Any]().result, NATIVE_ARRAY)
  }

  test(" NArrayBuilder[Boolean] ") {
    val f = () => r.nextBoolean()
    val values: NArray[Boolean] = NArray.tabulate[Boolean](N)((i: Int) => f())
    AddOneByOneTest[Boolean](NArrayBuilder[Boolean](), values, NATIVE_ARRAY).runTests()
    AddAllTest[Boolean](NArrayBuilder[Boolean](), values, NATIVE_ARRAY).runTests()
    BuilderResultTypeTest[Boolean](NATIVE_ARRAY).runTests()
  }

  test(" NArrayBuilder[Byte] ") {
    val f = () => r.nextBytes(1)(0)
    val values: NArray[Byte] = NArray.tabulate[Byte](N)((i: Int) => f())
    AddOneByOneTest[Byte](NArrayBuilder[Byte](), values, BYTE_ARRAY).runTests()
    AddAllTest[Byte](NArrayBuilder[Byte](), values, BYTE_ARRAY).runTests()
    //assertBuilderResultType[Byte](values.builder[Byte]().result, BYTE_ARRAY)
    BuilderResultTypeTest[Byte](BYTE_ARRAY).runTests()
  }

  test(" NArrayBuilder[Short] ") {
    val f = () => r.nextInt().toShort
    val values: NArray[Short] = NArray.tabulate[Short](N)((i: Int) => f())
    AddOneByOneTest[Short](NArrayBuilder[Short](), values, SHORT_ARRAY).runTests()
    AddAllTest[Short](NArrayBuilder[Short](), values, SHORT_ARRAY).runTests()
    //assertBuilderResultType[Short](values.builder[Short]().result, SHORT_ARRAY)
    BuilderResultTypeTest[Short](SHORT_ARRAY).runTests()
  }

  test(" NArrayBuilder[Int] ") {
    val f = () => r.nextInt()
    val values: NArray[Int] = NArray.tabulate[Int](N)((i: Int) => f())
    AddOneByOneTest[Int](NArrayBuilder[Int](), values, INT_ARRAY).runTests()
    AddAllTest[Int](NArrayBuilder[Int](), values, INT_ARRAY).runTests()
    //assertBuilderResultType[Int](values.builder[Int]().result, INT_ARRAY)
    BuilderResultTypeTest[Int](INT_ARRAY).runTests()
  }

  test(" NArrayBuilder[Long] ") {
    val f = () => r.nextLong()
    val values: NArray[Long] = NArray.tabulate[Long](N)((i: Int) => f())
    AddOneByOneTest[Long](NArrayBuilder[Long](), values, NATIVE_ARRAY).runTests()
    AddAllTest[Long](NArrayBuilder[Long](), values, NATIVE_ARRAY).runTests()
    //assertBuilderResultType[Long](values.builder[Long]().result, NATIVE_ARRAY)
    BuilderResultTypeTest[Long](NATIVE_ARRAY).runTests()
  }

  test(" NArrayBuilder[Float] ") {
    val f = () => r.nextFloat()
    val values: NArray[Float] = NArray.tabulate[Float](N)((i: Int) => f())
    AddOneByOneTest[Float](NArrayBuilder[Float](), values, FLOAT_ARRAY).runTests()
    AddAllTest[Float](NArrayBuilder[Float](), values, FLOAT_ARRAY).runTests()
    //assertBuilderResultType[Float](values.builder[Float]().result, FLOAT_ARRAY)
    BuilderResultTypeTest[Float](FLOAT_ARRAY).runTests()
  }

  test(" NArrayBuilder[Double] ") {
    val f = () => r.nextDouble()
    val values: NArray[Double] = NArray.tabulate[Double](N)((i: Int) => f())
    AddOneByOneTest[Double](NArrayBuilder[Double](), values, DOUBLE_ARRAY).runTests()
    AddAllTest[Double](NArrayBuilder[Double](), values, DOUBLE_ARRAY).runTests()
    //assertBuilderResultType[Double](values.builder[Double]().result, DOUBLE_ARRAY)
    BuilderResultTypeTest[Double](DOUBLE_ARRAY).runTests()
  }

  test(" NArrayBuilder[String] ") {
    val f = () => r.nextString(1 + r.nextInt(9))
    val values: NArray[String] = NArray.tabulate[String](N)((i: Int) => f())
    AddOneByOneTest[String](NArrayBuilder[String](), values, NATIVE_ARRAY).runTests()
    AddAllTest[String](NArrayBuilder[String](), values, NATIVE_ARRAY).runTests()
    //assertBuilderResultType[String](values.builder[String]().result, NATIVE_ARRAY)
    BuilderResultTypeTest[String](NATIVE_ARRAY).runTests()
  }

  test(" NArrayBuilder[Unit] ") {
    val f = () => ()
    val values: NArray[Unit] = NArray.tabulate[Unit](N)((i: Int) => f())
    AddOneByOneTest[Unit](NArrayBuilder[Unit](), values, NATIVE_ARRAY).runTests()
    AddAllTest[Unit](NArrayBuilder[Unit](), values, NATIVE_ARRAY).runTests()
    //assertBuilderResultType[Unit](values.builder[Unit]().result, NATIVE_ARRAY)
    BuilderResultTypeTest[Unit](NATIVE_ARRAY).runTests()
  }

  // TODO: add a stress test.
}
