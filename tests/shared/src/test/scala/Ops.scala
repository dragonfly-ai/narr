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

import Comparison.*
import scala.reflect.ClassTag
import scala.util.Random as r

class Ops extends munit.FunSuite {

  val N: Int = 11
  val lastIndex: Int = N - 1

  private case class NArrayOpsTest[T:ClassTag](a: NArray[T]) {

    def test(): Unit = {
      assertEquals(a.size, N)
      assertEquals(a.knownSize, N)
      assertEquals(a.isEmpty, N == 0)
      assertEquals(a.nonEmpty, N != 0)
      assertEquals(a.head, a(0))
      assertEquals(a.last, a(lastIndex))
      assertEquals(a.headOption, Some(a(0)))
      assertEquals(a.lastOption, Some(a(lastIndex)))

      assert(a.sizeCompare(lastIndex) > 0)
      assert(a.sizeCompare(N) == 0)
      assert(a.sizeCompare(N + 1) < 0)

      assert(a.lengthCompare(lastIndex) > 0)
      assert(a.lengthCompare(N) == 0)
      assert(a.lengthCompare(N + 1) < 0)

      assertEquals(a.sizeIs, N)
      assertEquals(a.lengthIs, N)

      // slice
      val middleIndex:Int = N / 2
      val start:Int = middleIndex - r.nextInt(middleIndex - 1)
      val end:Int = middleIndex + r.nextInt(middleIndex - 1)
      assertNArrayEquality[T](
        a.slice(start, end),
        NArray.tabulate[T](end - start)((i:Int) => a(start + i))
      )

      // tail
      assertNArrayEquality[T](
        a.tail,
        NArray.tabulate[T](lastIndex)((i: Int) => a(1 + i))
      )

      // init
      assertNArrayEquality[T](
        a.init,
        NArray.tabulate[T](lastIndex)((i: Int) => a(i))
      )

      var fulcrum:Int = 0; while (fulcrum < N) {

        val left:NArray[T] = NArray.tabulate[T](fulcrum)((i: Int) => a(i))
        val right:NArray[T] = NArray.tabulate[T](N - fulcrum)((i: Int) => a(fulcrum + i))

        // take
        assertNArrayEquality[T]( a.take(fulcrum), left )

        // drop
        assertNArrayEquality[T]( a.drop(fulcrum), right )

        // takeRight
        assertNArrayEquality[T](
          a.takeRight(fulcrum),
          NArray.tabulate[T](fulcrum)((i: Int) => a(N - fulcrum + i))
        )

        // dropRight
        assertNArrayEquality[T](
          a.dropRight(fulcrum),
          NArray.tabulate[T](N - fulcrum)((i: Int) => a(i))
        )

        // splitAt
        val (s1:NArray[T], s2:NArray[T]) = a.splitAt(fulcrum)
        assertNArrayEquality[T](s1, left)
        assertNArrayEquality[T](s2, right)

        fulcrum += 1
      }

      // takeWhile

      // dropWhile

      // iterator

      // indexWhere

      // foreach

      // indices
    }
  }

  ////////////////
  // Value Types:
  ////////////////

  test("NArrayOpsTest[Unit]") { NArrayOpsTest[Unit](NArray.tabulate[Unit](N)(_ => ())).test() }
  test("NArrayOpsTest[Boolean]") { NArrayOpsTest[Boolean](NArray.tabulate[Boolean](N)((i: Int) => i % 2 == 0)).test() }
  test("NArrayOpsTest[Byte]") { NArrayOpsTest[Byte](NArray.tabulate[Byte](N)((i:Int) => i.toByte)).test() }
  test("NArrayOpsTest[Short]") { NArrayOpsTest[Short](NArray.tabulate[Short](N)((i: Int) => i.toShort)).test() }
  test("NArrayOpsTest[Int]") { NArrayOpsTest[Int](NArray.tabulate[Int](N)((i: Int) => i)).test() }
  test("NArrayOpsTest[Long]") { NArrayOpsTest[Long](NArray.tabulate[Long](N)((i: Int) => i.toLong)).test() }
  test("NArrayOpsTest[Float]") { NArrayOpsTest[Float](NArray.tabulate[Float](N)((i: Int) => i.toFloat)).test() }
  test("NArrayOpsTest[Double]") { NArrayOpsTest[Double](NArray.tabulate[Double](N)((i: Int) => i.toDouble)).test() }
  test("NArrayOpsTest[Char]") { NArrayOpsTest[Char](NArray.tabulate[Char](N)((i: Int) => i.toChar)).test() }

  ////////////////////
  // Reference Types:
  ////////////////////

  test("NArrayOpsTest[String]") { NArrayOpsTest[String](NArray.tabulate[String](N)((i: Int) => i.toString)).test() }
  test("NArrayOpsTest[AnyRef]") { NArrayOpsTest[AnyRef](NArray.tabulate[AnyRef](N)(_ => new AnyRef())).test() }

}