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

class FlattenTest extends munit.FunSuite {

  val N:Int = 30

  private case class TestFlatten[B](a: NArray[NArray[B]], nt: NArrayType)(using ClassTag[B], ClassTag[NArray[B]]) {
    given f: Function1[NArray[B], IterableOnce[B]] = (ab:NArray[B]) => new IterableOnce[B] {
      override def iterator: Iterator[B] = new Iterator[B] {
        var i = 0
        override def hasNext: Boolean = i < ab.length
        override def next(): B = {
          if (hasNext) {
            val r = ab(i)
            i = i + 1
            r
          } else throw new NoSuchElementException("next on empty iterator")
        }
      }
    }

    def test(): Unit = {
      val afltnd: NArray[B] = a.flatten
      assertNArrayType(afltnd, nt)
      assertArray2NArrayEquality(a.toArray.flatten, afltnd)
    }
  }

  test("TestFlatten[NArray[Byte]]") {
    TestFlatten[Byte](
      NArray.tabulate[NArray[Byte]](N)((i: Int) => NArray.tabulate[Byte](i)((i0: Int) => i0.toByte)),
      BYTE_ARRAY
    ).test()
  }

  test("TestFlatten[NArray[Short]]") {
    TestFlatten[Short](
      NArray.tabulate[NArray[Short]](N)((i: Int) => NArray.tabulate[Short](i)((i0: Int) => i0.toShort)),
      SHORT_ARRAY
    ).test()
  }

  test("TestFlatten[NArray[Int]]") {
    TestFlatten[Int](
      NArray.tabulate[NArray[Int]](N)((i: Int) => NArray.tabulate[Int](i)((i0: Int) => i0)),
      INT_ARRAY
    ).test()
  }

  test("TestFlatten[NArray[Float]]") {
    TestFlatten[Float](
      NArray.tabulate[NArray[Float]](N)((i: Int) => NArray.tabulate[Float](i)((i0: Int) => i0.toFloat)),
      FLOAT_ARRAY
    ).test()
  }

  test("TestFlatten[NArray[Double], Double]") {
    TestFlatten[Double](
      NArray.tabulate[NArray[Double]](N)((i: Int) => NArray.tabulate[Double](i)((i0: Int) => i0.toDouble)),
      DOUBLE_ARRAY
    ).test()
  }

  test("TestFlatten[String, String]") {
    val rs = new Random()
    TestFlatten[String](
      NArray.tabulate[NArray[String]](N)((i: Int) => NArray.tabulate[String](i)((i0: Int) => rs.nextString(i))),
      NATIVE_ARRAY
    ).test()
  }

}