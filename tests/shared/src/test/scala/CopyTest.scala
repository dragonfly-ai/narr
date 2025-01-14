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

import scala.language.implicitConversions
import Util.*
import Util.NArrayType.*

import scala.reflect.ClassTag

class CopyTest extends munit.FunSuite {

  var N: Int = 11

  // TODO: test NArray.{copyOf, copyAs}

  private case class CopyOpsTest[T](a: NArray[T], nt: NArrayType)(using ClassTag[T]) {
    def test(): Unit = {
      assertNArrayEquality[T](a, a.copy, nt)
      assertNArrayEquality[T](a, narr.NArray.copy(a), nt)

      val arrU = new Array[T](N)
      a.copyToArray(arrU)
      assertArrayEquality(arrU, a.toArray)

      val aU = NArray.ofSize[T](N)
      a.copyToNArray[T](aU)
      assertArray2NArrayEquality[T](arrU, aU)
      assertNArrayEquality[T](a, a.toNArray[T], nt)
    }
  }

  ////////////////
  // Value Types:
  ////////////////

  test(" Copy NArray[Unit] ") {
    CopyOpsTest[Unit](NArray.tabulate[Unit](N)(_ => ()), NATIVE_ARRAY).test()
  }

  test(" Copy NArray[Boolean] ") {
    CopyOpsTest[Boolean](NArray.tabulate[Boolean](N)((i: Int) => i % 2 == 0), NATIVE_ARRAY).test()
  }

  test(" Copy NArray[Byte] ") {
    CopyOpsTest[Byte](NArray.tabulate[Byte](N)((i: Int) => i.toByte), BYTE_ARRAY).test()
  }

  test(" Copy NArray[Short] ") {
    CopyOpsTest[Short](NArray.tabulate[Short](N)((i: Int) => i.toShort), SHORT_ARRAY).test()
  }

  test(" Copy NArray[Int] ") {
    CopyOpsTest[Int](NArray.tabulate[Int](N)((i: Int) => i), INT_ARRAY).test()
  }

  test(" Copy NArray[Long] ") {
    CopyOpsTest[Long](NArray.tabulate[Long](N)((i: Int) => i.toLong), NATIVE_ARRAY).test()
  }

  test(" Copy NArray[Float] ") {
    CopyOpsTest[Float](NArray.tabulate[Float](N)((i: Int) => i.toFloat), FLOAT_ARRAY).test()
  }

  test(" Copy NArray[Double] ") {
    CopyOpsTest[Double](NArray.tabulate[Double](N)((i: Int) => i.toDouble), DOUBLE_ARRAY).test()
  }

  test(" Copy NArray[Char] ") {
    CopyOpsTest[Char](NArray.tabulate[Char](N)((i: Int) => i.toChar), NATIVE_ARRAY).test()
  }

    ////////////////////
    // Reference Types:
    ////////////////////

  test(" Copy NArray[String] ") {
    CopyOpsTest[String](NArray.tabulate[String](N)((i: Int) => i.toString), NATIVE_ARRAY).test()
  }

  test(" Copy NArray[AnyRef] ") {
    CopyOpsTest[AnyRef](NArray.tabulate[AnyRef](N)(_ => new AnyRef()), NATIVE_ARRAY).test()
  }

}