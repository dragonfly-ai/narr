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

class FoldAndScanTest extends munit.FunSuite {

  val N:Int = 30

  private case class TestFold[T, B >: T](a: NArray[T], c: B, f: (B, B) => B)(using ClassTag[B]) {
    def test(): Unit = assertEquals[B, B](a.toArray.fold[B](c)(f), a.fold[B](c)(f))
  }

  private case class TestFoldLeft[T, B](a: NArray[T], c: B, f: (B, T) => B)(using ClassTag[T]) {
    def test(): Unit = assertEquals[B, B](a.toArray.foldLeft[B](c)(f), a.foldLeft[B](c)(f))
  }

  private case class TestFoldRight[T, B](a: NArray[T], c: B, f: (T, B) => B)(using ClassTag[T]) {
    def test(): Unit = assertEquals(a.toArray.foldRight(c)(f), a.foldRight(c)(f))
  }

  private case class TestScan[T, B >: T](a: NArray[B], c: B, f: (B, B) => B, nt: NArrayType)(using ClassTag[B]) {
    def test(): Unit = {
      val scnd = a.scan(c)(f)
      assertNArrayType(scnd, nt)
      assertArray2NArrayEquality[B](a.toArray.scan(c)(f), scnd)
    }
  }

  private case class TestScanLeft[T, B](a: NArray[T], c: B, f: (B, T) => B, nt: NArrayType)(using ClassTag[T], ClassTag[B]) {
    def test(): Unit = {
      val scnd = a.scanLeft[B](c)(f)
      assertNArrayType(scnd, nt)
      assertArray2NArrayEquality(a.toArray.scanLeft[B](c)(f), scnd)
    }
  }

  private case class TestScanRight[T, B](a: NArray[T], c: B, f: (T, B) => B, nt: NArrayType)(using ClassTag[T], ClassTag[B]) {
    def test(): Unit = {
      val scnd = a.scanRight(c)(f)
      assertNArrayType(scnd, nt)
      assertArray2NArrayEquality(a.toArray.scanRight(c)(f), scnd)
    }
  }

  // fold
  test("TestFold[Int, Int]") {
    TestFold[Int, Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      0,
      (i0: Int, i1: Int) => i0 + i1
    ).test()
  }

  test("TestFold[Double, Double]") {
    TestFold[Double, Double](
      NArray.tabulate[Double](N)((i: Int) => i.toDouble),
      0.0,
      (d0: Double, d1: Double) => d0 + d1
    ).test()
  }

  test("TestFold[String, String]") {
    TestFold[String, String](
      NArray.tabulate[String](N)((i: Int) => i.toString),
      "",
      (s0: String, s1: String) => s0 + s1
    ).test()
  }

  // scan
  test("TestScan[Int, Int]") {
    TestScan[Int, Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      0,
      (i0: Int, i1: Int) => i0 + i1,
      INT_ARRAY
    ).test()
  }

  test("TestScan[Double, Double]") {
    TestScan[Double, Double](
      NArray.tabulate[Double](N)((i: Int) => i.toDouble),
      0.0,
      (d0: Double, d1: Double) => d0 + d1,
      DOUBLE_ARRAY
    ).test()
  }

  test("TestScan[String, String]") {
    TestScan[String, String](
      NArray.tabulate[String](N)((i: Int) => i.toString),
      "",
      (s0: String, s1: String) => s0 + s1,
      NATIVE_ARRAY
    ).test()
  }

  // foldLeft
  test("TestFoldLeft[Byte, Int]") {
    TestFoldLeft[Byte, Int](
      NArray.tabulate[Byte](N)((i: Int) => i.toByte),
      0,
      (i: Int, b: Byte) => i + b
    ).test()
  }

  test("TestFoldLeft[Short, Int]") {
    TestFoldLeft[Short, Int](
      NArray.tabulate[Short](N)((i: Int) => i.toShort),
      0,
      (i: Int, s: Short) => i + s
    ).test()
  }

  test("TestFoldLeft[Int, Int]") {
    TestFoldLeft[Int, Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      0,
      (i0: Int, i1: Int) => i0 + i1
    ).test()
  }

  test("TestFoldLeft[Float, Double]") {
    TestFoldLeft[Float, Double](
      NArray.tabulate[Float](N)((i: Int) => i.toFloat),
      0.0,
      (d: Double, f: Float) => f.toDouble + d
    ).test()
  }

  test("TestFoldLeft[Double, Double]") {
    TestFoldLeft[Double, Double](
      NArray.tabulate[Double](N)((i: Int) => i.toDouble),
      0.0,
      (d0: Double, d1: Double) => d0 + d1
    ).test()
  }

  test("TestFoldLeft[String, String]") {
    TestFoldLeft[String, String](
      NArray.tabulate[String](N)((i: Int) => i.toString),
      "",
      (s0: String, s1: String) => s0 + s1
    ).test()
  }

  // scanLeft
  test("TestScanLeft[Byte, Int]") {
    TestScanLeft[Byte, Int](
      NArray.tabulate[Byte](N)((i: Int) => i.toByte),
      0,
      (i: Int, b: Byte) => i + b,
      INT_ARRAY
    ).test()
  }

  test("TestScanLeft[Short, Int]") {
    TestScanLeft[Short, Int](
      NArray.tabulate[Short](N)((i: Int) => i.toShort),
      0,
      (i: Int, s: Short) => i + s,
      INT_ARRAY
    ).test()
  }

  test("TestScanLeft[Int, Int]") {
    TestScanLeft[Int, Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      0,
      (i0: Int, i1: Int) => i0 + i1,
      INT_ARRAY
    ).test()
  }

  test("TestScanLeft[Float, Double]") {
    TestScanLeft[Float, Double](
      NArray.tabulate[Float](N)((i: Int) => i.toFloat),
      0.0,
      (d: Double, f: Float) => f.toDouble + d,
      DOUBLE_ARRAY
    ).test()
  }

  test("TestScanLeft[Double, Double]") {
    TestScanLeft[Double, Double](
      NArray.tabulate[Double](N)((i: Int) => i.toDouble),
      0.0,
      (d0: Double, d1: Double) => d0 + d1,
      DOUBLE_ARRAY
    ).test()
  }

  test("TestScanLeft[String, String]") {
    TestScanLeft[String, String](
      NArray.tabulate[String](N)((i: Int) => i.toString),
      "",
      (s0: String, s1: String) => s0 + s1,
      NATIVE_ARRAY
    ).test()
  }

  // foldRight
  test("TestFoldRight[Byte, Int]") {
    TestFoldRight[Byte, Int](
      NArray.tabulate[Byte](N)((i: Int) => i.toByte),
      0,
      (b: Byte, i: Int) => i + b
    ).test()
  }

  test("TestFoldRight[Short, Int]") {
    TestFoldRight[Short, Int](
      NArray.tabulate[Short](N)((i: Int) => i.toShort),
      0,
      (s: Short, i: Int) => i + s
    ).test()
  }

  test("TestFoldRight[Int, Int]") {
    TestFoldRight[Int, Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      0,
      (i0: Int, i1: Int) => i0 + i1
    ).test()
  }

  test("TestFoldRight[Float, Double]") {
    TestFoldRight[Float, Double](
      NArray.tabulate[Float](N)((i: Int) => i.toFloat),
      0.0,
      (f: Float, d: Double) => f.toDouble + d
    ).test()
  }

  test("TestFoldRight[Double, Double]") {
    TestFoldRight[Double, Double](
      NArray.tabulate[Double](N)((i: Int) => i.toDouble),
      0.0,
      (d0: Double, d1: Double) => d0 + d1
    ).test()
  }

  test("TestFoldRight[String, String]") {
    TestFoldRight[String, String](
      NArray.tabulate[String](N)((i: Int) => i.toString),
      "",
      (s0: String, s1: String) => s0 + s1
    ).test()
  }


  // scanRight
  test("TestScanRight[Byte, Int]") {
    TestScanRight[Byte, Int](
      NArray.tabulate[Byte](N)((i: Int) => i.toByte),
      0,
      (b: Byte, i: Int) => i + b,
      INT_ARRAY
    ).test()
  }

  test("TestScanRight[Short, Int]") {
    TestScanRight[Short, Int](
      NArray.tabulate[Short](N)((i: Int) => i.toShort),
      0,
      (s: Short, i: Int) => i + s,
      INT_ARRAY
    ).test()
  }

  test("TestScanRight[Int, Int]") {
    TestScanRight[Int, Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      0,
      (i0: Int, i1: Int) => i0 + i1,
      INT_ARRAY
    ).test()
  }

  test("TestScanRight[Float, Double]") {
    TestScanRight[Float, Double](
      NArray.tabulate[Float](N)((i: Int) => i.toFloat),
      0.0,
      (f: Float, d: Double) => f.toDouble + d,
      DOUBLE_ARRAY
    ).test()
  }

  test("TestScanRight[Double, Double]") {
    TestScanRight[Double, Double](
      NArray.tabulate[Double](N)((i: Int) => i.toDouble),
      0.0,
      (d0: Double, d1: Double) => d0 + d1,
      DOUBLE_ARRAY
    ).test()
  }

  test("TestScanRight[String, String]") {
    TestScanRight[String, String](
      NArray.tabulate[String](N)((i: Int) => i.toString),
      "",
      (s0: String, s1: String) => s0 + s1,
      NATIVE_ARRAY
    ).test()
  }

}