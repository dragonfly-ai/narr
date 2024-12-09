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
import scala.util.Random as r

object SortTest {

  import munit.*
  import munit.Assertions.*

  def assertNArraySortedAscending[T](nArr: NArray[T])(using loc: Location, o: Ordering[T]): Unit = {
    var i: Int = 0
    while (i < nArr.length - 1) {
      assert(o.lteq(nArr(i), nArr(i + 1)))
      i += 1
    }
  }

  def assertNArraySortedDescending[T](nArr: NArray[T])(using loc: Location, o: Ordering[T]): Unit = {
    var i: Int = 0
    while (i < nArr.length - 1) {
      assert(o.gteq(nArr(i), nArr(i + 1)))
      i += 1
    }
  }
}

class SortTest extends munit.FunSuite {

  import SortTest.*

  var N: Int = 11

  test("SortTest NArray[AnyRef]") {
    given anyRefOrdering: Ordering[AnyRef] = Ordering.by(_.hashCode())

    val sara: NArray[AnyRef] = NArray.tabulate[AnyRef](N)(_ => new AnyRef)
    assertNArraySortedAscending[AnyRef](sara.sort(anyRefOrdering))
    assertNArraySortedDescending[AnyRef](sara.sort(anyRefOrdering.reverse))
    assertNArraySortedAscending[AnyRef](sara.sorted(anyRefOrdering))
    //assertNArraySortedAscending[AnyRef](sara.sortWith((e0:AnyRef)))
  }

  test("SortTest NArray[Unit]") {
    val sba: NArray[Unit] = NArray.tabulate[Unit](N)((_: Int) => ())
    assertNArraySortedAscending[Unit](sba.sort())
    assertNArraySortedAscending[Unit](sba.sorted)
    assertNArraySortedDescending[Unit](sba.sort(Ordering.Unit.reverse))
    assertNArraySortedAscending[Unit](sba.sorted(Ordering.Unit))
  }

  test("SortTest NArray[Boolean]") {
    val sba: NArray[Boolean] = NArray.tabulate[Boolean](N)((_: Int) => r.nextBoolean())
    assertNArraySortedAscending[Boolean](sba.sort())
    assertNArraySortedAscending[Boolean](sba.sorted)
    assertNArraySortedDescending[Boolean](sba.sort(Ordering.Boolean.reverse))
    assertNArraySortedAscending[Boolean](sba.sorted(Ordering.Boolean))
  }

  test("SortTest NArray[Byte]") {
    val sba: NArray[Byte] = NArray.tabulate[Byte](N)((_: Int) => r.nextBytes(1)(0))
    assertNArraySortedAscending[Byte](sba.sort())
    assertNArraySortedAscending[Byte](sba.sorted)
    assertNArraySortedDescending[Byte](sba.sort(Ordering.Byte.reverse))
    assertNArraySortedAscending[Byte](sba.sorted(Ordering.Byte))
  }

  test("SortTest NArray[Short]") {
    val ssa: NArray[Short] = NArray.tabulate[Short](N)((_: Int) => r.nextInt(Short.MaxValue).toShort)
    assertNArraySortedAscending[Short](ssa.sort())
    assertNArraySortedAscending[Short](ssa.sorted)
    assertNArraySortedDescending[Short](ssa.sort(Ordering.Short.reverse))
    assertNArraySortedAscending[Short](ssa.sorted(Ordering.Short))
  }

  test("SortTest NArray[Int]") {
    val sia: NArray[Int] = NArray.tabulate[Int](N)((_: Int) => r.nextInt())
    assertNArraySortedAscending[Int](sia.sort())
    assertNArraySortedAscending[Int](sia.sorted)
    assertNArraySortedDescending[Int](sia.sort(Ordering.Int.reverse))
    assertNArraySortedAscending[Int](sia.sorted(Ordering.Int))
  }

  test("SortTest NArray[Long]") {
    val sla: NArray[Long] = NArray.tabulate[Long](N)((_: Int) => r.nextLong(9999L))
    assertNArraySortedAscending[Long](sla.sort(Ordering.Long))
    assertNArraySortedDescending[Long](sla.sort(Ordering.Long.reverse))
    assertNArraySortedAscending[Long](sla.sorted(Ordering.Long))
  }

  test("SortTest NArray[Float]") {
    val sfa: NArray[Float] = NArray.tabulate[Float](N)((_: Int) => r.nextFloat())
    assertNArraySortedAscending[Float](sfa.sort())
    assertNArraySortedAscending[Float](sfa.sorted)
    assertNArraySortedDescending[Float](sfa.sort(Ordering.Float.TotalOrdering.reverse))
    assertNArraySortedAscending[Float](sfa.sorted(Ordering.Float.TotalOrdering))
  }

  test("SortTest NArray[Double]") {
    val sda: NArray[Double] = NArray.tabulate[Double](N)((_: Int) => r.nextDouble())
    assertNArraySortedAscending[Double](sda.sort())
    assertNArraySortedAscending[Double](sda.sorted)
    assertNArraySortedDescending[Double](sda.sort(Ordering.Double.TotalOrdering.reverse))
    assertNArraySortedAscending[Double](sda.sorted(Ordering.Double.TotalOrdering))
  }

  test("SortTest NArray[Char]") {
    val sca: NArray[Char] = NArray.tabulate[Char](N)((_: Int) => r.nextPrintableChar())
    assertNArraySortedAscending[Char](sca.sort())
    assertNArraySortedDescending[Char](sca.sort(Ordering.Char.reverse))
    assertNArraySortedAscending[Char](sca.sorted(Ordering.Char))
  }

  test("SortTest NArray[String]") {
    val sstra: NArray[String] = NArray.tabulate[String](N)((_: Int) => r.nextString(11))
    assertNArraySortedAscending[String](sstra.sort())
    assertNArraySortedDescending[String](sstra.sort(Ordering.String.reverse))
    assertNArraySortedAscending[String](sstra.sorted(Ordering.String))
  }
}