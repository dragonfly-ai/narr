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

class PartitionTest extends munit.FunSuite {

  val N:Int = 30

  private case class TestPartition[T:ClassTag](a: NArray[T], f: T => Boolean, nt: NArrayType) {
    def test(): Unit = {
      val (aL: NArray[T], aR: NArray[T]) = a.partition(f)
      assertNArrayType(aL, nt)
      assertNArrayType(aR, nt)
      assertEquals(aL.length + aR.length, a.length)
      val (arrL: Array[T], arrR: Array[T]) = a.toArray.partition(f)
      assertArray2NArrayEquality(arrL, aL)
      assertArray2NArrayEquality(arrR, aR)
    }
  }


  private case class TestPartitionMap[T:ClassTag, A1:ClassTag, A2:ClassTag](a: NArray[T], f: T => Either[A1, A2], nt: NArrayType) {
    def test(): Unit = {
      val (aL: NArray[A1], aR: NArray[A2]) = a.partitionMap(f)
      assertNArrayType(aL, nt)
      assertNArrayType(aR, nt)
      assertEquals(aL.length + aR.length, a.length)
      val (arrL: Array[A1], arrR: Array[A2]) = a.toArray.partitionMap(f)
      assertArray2NArrayEquality(arrL, aL)
      assertArray2NArrayEquality(arrR, aR)
    }
  }

  // partition
  test("TestPartition[Boolean]") {
    TestPartition[Boolean](
      NArray.tabulate[Boolean](N)((i: Int) => i % 2 == 0),
      (b: Boolean) => b,
      NATIVE_ARRAY
    ).test()
  }

  test("TestPartition[Byte]") {
    TestPartition[Byte](
      NArray.tabulate[Byte](N)((i: Int) => i.toByte),
      (b: Byte) => b % 2 == 0,
      BYTE_ARRAY
    ).test()
  }

  test("TestPartition[Short]") {
    TestPartition[Short](
      NArray.tabulate[Short](N)((i: Int) => i.toShort),
      (s: Short) => s % 2 == 0,
      SHORT_ARRAY
    ).test()
  }

  test("TestPartition[Int]") {
    TestPartition[Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i: Int) => i % 2 == 0,
      INT_ARRAY
    ).test()
  }

  val rs = new Random()

  test("TestPartition[Float]") {
    TestPartition[Float](
      NArray.tabulate[Float](N)((_: Int) => rs.nextFloat()),
      (f: Float) => f > 0.5f,
      FLOAT_ARRAY
    ).test()
  }

  test("TestPartition[Double]") {
    TestPartition[Double](
      NArray.tabulate[Double](N)((_: Int) => rs.nextDouble()),
      (d: Double) => d > 0.5,
      DOUBLE_ARRAY
    ).test()
  }

  // partitionMap
  test("TestPartitionMap[Boolean, Boolean, Boolean]") {
    TestPartitionMap[Boolean, Boolean, Boolean](
      NArray.tabulate[Boolean](N)((i: Int) => i % 2 == 0),
      (b: Boolean) => if (b) Left(b) else Right(b),
      NATIVE_ARRAY
    ).test()
  }

  test("TestPartitionMap[Byte, Byte, Byte]") {
    TestPartitionMap[Byte, Byte, Byte](
      NArray.tabulate[Byte](N)((i: Int) => i.toByte),
      (b: Byte) => if (b % 2 == 0) Left(b) else Right(b),
      BYTE_ARRAY
    ).test()
  }

  test("TestPartitionMap[Short, Short, Short]") {
    TestPartitionMap[Short, Short, Short](
      NArray.tabulate[Short](N)((i: Int) => i.toShort),
      (s: Short) => if (s % 2 == 0) Left(s) else Right(s),
      SHORT_ARRAY
    ).test()
  }

  test("TestPartitionMap[Int, Int, Int]") {
    TestPartitionMap[Int, Int, Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i: Int) => if (i % 2 == 0) Left(i) else Right(i),
      INT_ARRAY
    ).test()
  }

  test("TestPartitionMap[Float, Float, Float]") {
    TestPartitionMap[Float, Float, Float](
      NArray.tabulate[Float](N)((_: Int) => rs.nextFloat()),
      (f: Float) => if (f > 0.5f) Left(f) else Right(f),
      FLOAT_ARRAY
    ).test()
  }

  test("TestPartitionMap[Double, Double, Double]") {
    TestPartitionMap[Double, Double, Double](
      NArray.tabulate[Double](N)((_: Int) => rs.nextDouble()),
      (d: Double) => if (d > 0.5) Left(d) else Right(d),
      DOUBLE_ARRAY
    ).test()
  }
}