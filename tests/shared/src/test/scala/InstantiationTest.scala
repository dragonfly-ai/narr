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

class InstantiationTest extends munit.FunSuite {

  var N: Int = 11

  def tabulateTypeTest[T](f: (Int) => T, nt:NArrayType)(using ClassTag[T]): Unit = {
    assertNArrayType(NArray.tabulate[T](N)(f), nt)
  }

  ////////////////
  // Value Types:
  ////////////////

  test(" NArray[Unit] constructors and factories ") {

    // Unit
    val f = (_:Int) => ()
    val na: NArray[Unit] = new NArray[Unit](N)
    val aos: NArray[Unit] = NArray.ofSize[Unit](N)
    val af: NArray[Unit] = NArray.fill[Unit](N)(())
    val at: NArray[Unit] = NArray.tabulate[Unit](N)(f)

    assertNArrayType[Unit](na, NATIVE_ARRAY)
    assertNArrayType[Unit](aos, NATIVE_ARRAY)
    assertNArrayType[Unit](af, NATIVE_ARRAY)
    assertNArrayType[Unit](at, NATIVE_ARRAY)
    tabulateTypeTest[Unit](f, NATIVE_ARRAY)

    assertEquals(na.length, aos.length)
    assertEquals(aos.length, af.length)
    assertEquals(af.length, at.length)
  }

  test(" NArray[Boolean] constructors and factories ") {
    val f = (i: Int) => i % 2 == 0
    // Boolean
    val an: NArray[Boolean] = new NArray[Boolean](N)
    val aos: NArray[Boolean] = NArray.ofSize[Boolean](N)
    val af: NArray[Boolean] = NArray.fill[Boolean](N)(false)
    val at: NArray[Boolean] = NArray.tabulate[Boolean](N)(f)

    assertNArrayType[Boolean](an, NATIVE_ARRAY)
    assertNArrayType[Boolean](aos, NATIVE_ARRAY)
    assertNArrayType[Boolean](af, NATIVE_ARRAY)
    assertNArrayType[Boolean](at, NATIVE_ARRAY)
    tabulateTypeTest[Boolean](f, NATIVE_ARRAY)

    assertEquals(an.length, aos.length)
    assertEquals(aos.length, af.length)
    assertEquals(af.length, at.length)
  }

  test(" NArray[Byte] constructors and factories ") {
    val f = (i: Int) => i.toByte
    // Byte
    val an: NArray[Byte] = new NArray[Byte](N)
    val aos: NArray[Byte] = NArray.ofSize[Byte](N)
    val af: NArray[Byte] = NArray.fill[Byte](N)(1)
    val at: NArray[Byte] = NArray.tabulate[Byte](N)(f)

    assertNArrayType[Byte](an, BYTE_ARRAY)
    assertNArrayType[Byte](aos, BYTE_ARRAY)
    assertNArrayType[Byte](af, BYTE_ARRAY)
    assertNArrayType[Byte](at, BYTE_ARRAY)
    tabulateTypeTest[Byte](f, BYTE_ARRAY)

    assertEquals(an.length, aos.length)
    assertEquals(aos.length, af.length)
    assertEquals(af.length, at.length)
  }

  test(" NArray[Short] constructors and factories ") {
    val f = (i: Int) => i.toShort
    // Short
    val an: NArray[Short] = new NArray[Short](N)
    val aos: NArray[Short] = NArray.ofSize[Short](N)
    val af: NArray[Short] = NArray.fill[Short](N)(1)
    val at: NArray[Short] = NArray.tabulate[Short](N)(f)

    assertNArrayType[Short](an, SHORT_ARRAY)
    assertNArrayType[Short](aos, SHORT_ARRAY)
    assertNArrayType[Short](af, SHORT_ARRAY)
    assertNArrayType[Short](at, SHORT_ARRAY)
    tabulateTypeTest[Short](f, SHORT_ARRAY)

    assertEquals(an.length, aos.length)
    assertEquals(aos.length, af.length)
    assertEquals(af.length, at.length)
  }

  test(" NArray[Int] constructors and factories ") {
    val f = (i: Int) => i
    // Int
    val an: NArray[Int] = new NArray[Int](N)
    val aos: NArray[Int] = NArray.ofSize[Int](N)
    val af: NArray[Int] = NArray.fill[Int](N)(1)
    val at: NArray[Int] = NArray.tabulate[Int](N)(f)

    assertNArrayType[Int](an, INT_ARRAY)
    assertNArrayType[Int](aos, INT_ARRAY)
    assertNArrayType[Int](af, INT_ARRAY)
    assertNArrayType[Int](at, INT_ARRAY)
    tabulateTypeTest[Int](f, INT_ARRAY)

    assertEquals(an.length, aos.length)
    assertEquals(aos.length, af.length)
    assertEquals(af.length, at.length)
  }

  test(" NArray[Long] constructors and factories ") {
    val f = (i: Int) => i.toLong
    // Long
    val an: NArray[Long] = new NArray[Long](N)
    val aos: NArray[Long] = NArray.ofSize[Long](N)
    val af: NArray[Long] = NArray.fill[Long](N)(1L)
    val at: NArray[Long] = NArray.tabulate[Long](N)(f)

    assertNArrayType[Long](an, NATIVE_ARRAY)
    assertNArrayType[Long](aos, NATIVE_ARRAY)
    assertNArrayType[Long](af, NATIVE_ARRAY)
    assertNArrayType[Long](at, NATIVE_ARRAY)
    tabulateTypeTest[Long](f, NATIVE_ARRAY)

    assertEquals(an.length, aos.length)
    assertEquals(aos.length, af.length)
    assertEquals(af.length, at.length)
  }

  test(" NArray[Float] constructors and factories ") {
    val f = (i: Int) => i.toFloat
    // Float
    val an: NArray[Float] = new NArray[Float](N)
    val aos: NArray[Float] = NArray.ofSize[Float](N)
    val af: NArray[Float] = NArray.fill[Float](N)(1L)
    val at: NArray[Float] = NArray.tabulate[Float](N)(f)

    assertNArrayType[Float](an, FLOAT_ARRAY)
    assertNArrayType[Float](aos, FLOAT_ARRAY)
    assertNArrayType[Float](af, FLOAT_ARRAY)
    assertNArrayType[Float](at, FLOAT_ARRAY)
    tabulateTypeTest[Float](f, FLOAT_ARRAY)

    assertEquals(an.length, aos.length)
    assertEquals(aos.length, af.length)
    assertEquals(af.length, at.length)
  }

  test(" NArray[Double] constructors and factories ") {
    val f = (i: Int) => i.toDouble
    // Double
    val an: NArray[Double] = new NArray[Double](N)
    val aos: NArray[Double] = NArray.ofSize[Double](N)
    val af: NArray[Double] = NArray.fill[Double](N)(1L)
    val at: NArray[Double] = NArray.tabulate[Double](N)(f)

    assertNArrayType[Double](an, DOUBLE_ARRAY)
    assertNArrayType[Double](aos, DOUBLE_ARRAY)
    assertNArrayType[Double](af, DOUBLE_ARRAY)
    assertNArrayType[Double](at, DOUBLE_ARRAY)
    tabulateTypeTest[Double](f, DOUBLE_ARRAY)

    assertEquals(an.length, aos.length)
    assertEquals(aos.length, af.length)
    assertEquals(af.length, at.length)
  }

  test(" NArray[Char] constructors and factories ") {
    val f = (i: Int) => i.toChar
    // Char
    val an: NArray[Char] = new NArray[Char](N)
    val aos: NArray[Char] = NArray.ofSize[Char](N)
    val af: NArray[Char] = NArray.fill[Char](N)('a')
    val at: NArray[Char] = NArray.tabulate[Char](N)(f)

    assertNArrayType[Char](an, NATIVE_ARRAY)
    assertNArrayType[Char](aos, NATIVE_ARRAY)
    assertNArrayType[Char](af, NATIVE_ARRAY)
    assertNArrayType[Char](at, NATIVE_ARRAY)
    tabulateTypeTest[Char](f, NATIVE_ARRAY)

    assertEquals(an.length, aos.length)
    assertEquals(aos.length, af.length)
    assertEquals(af.length, at.length)
  }

  ////////////////////
  // Reference Types:
  ////////////////////

  test(" NArray[String] constructors and factories ") {
    val f = (i: Int) => i.toString
    // String
    val an: NArray[String] = new NArray[String](N)
    val aos: NArray[String] = NArray.ofSize[String](N)
    val af: NArray[String] = NArray.fill[String](N)("Asdf.")
    val at: NArray[String] = NArray.tabulate[String](N)(f)

    assertNArrayType[String](an, NATIVE_ARRAY)
    assertNArrayType[String](aos, NATIVE_ARRAY)
    assertNArrayType[String](af, NATIVE_ARRAY)
    assertNArrayType[String](at, NATIVE_ARRAY)
    tabulateTypeTest[String](f, NATIVE_ARRAY)

    assertEquals(an.length, aos.length)
    assertEquals(aos.length, af.length)
    assertEquals(af.length, at.length)
  }

  test(" NArray[AnyRef] constructors and factories ") {
    val f = (_:Int) => new AnyRef()
    // AnyRef
    val an: NArray[AnyRef] = new NArray[AnyRef](N)
    val aos: NArray[AnyRef] = NArray.ofSize[AnyRef](N)
    val af: NArray[AnyRef] = NArray.fill[AnyRef](N)(new AnyRef())
    val at: NArray[AnyRef] = NArray.tabulate[AnyRef](N)(f)

    assertNArrayType[AnyRef](an, NATIVE_ARRAY)
    assertNArrayType[AnyRef](aos, NATIVE_ARRAY)
    assertNArrayType[AnyRef](af, NATIVE_ARRAY)
    assertNArrayType[AnyRef](at, NATIVE_ARRAY)
    tabulateTypeTest[AnyRef](f, NATIVE_ARRAY)

    assertEquals(an.length, aos.length)
    assertEquals(aos.length, af.length)
    assertEquals(af.length, at.length)
  }
}
