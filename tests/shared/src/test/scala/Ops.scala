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

import scala.collection.immutable
import scala.reflect.ClassTag
import scala.util.Random as r

class Ops extends munit.FunSuite {

  val N: Int = 11
  val lastIndex: Int = N - 1

  private trait HasNArray[T:ClassTag] {
    val a: NArray[T]
  }

  private trait UniversalNArrayOpsTest[T:ClassTag] extends HasNArray[T] {
    def universalTest(): Unit = {
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

      // reverse
      val rev:NArray[T] = a.reverse
      var i:Int = 0; while (i < N) {
        assertEquals(rev(i), a(lastIndex - i))
        i += 1
      }

      // reverseIterator
      val ritr: Iterator[T] = a.reverseIterator
      i = 0; while (i < N) {
        assertEquals(true, ritr.hasNext)
        assertEquals(a(lastIndex - i), ritr.next())
        i += 1
      }
      assertEquals(false, ritr.hasNext)

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

        // appendedAll
        assertNArrayEquality[T](left.appendedAll(right), a)

        // concat
        assertNArrayEquality[T]( left.concat(right), a )

        // find
        a.find((t:T) => t == a(fulcrum)) match {
          case Some(t:T) => assertEquals( t, a(fulcrum) )
          case _ => assertEquals(false, true)
        }

        // exists
        assertEquals( a.exists((t: T) => t == a(fulcrum)), true)

        // contains
        assertEquals(true, a.contains(a(fulcrum)))

        // startsWith
        assertEquals(a.startsWith(left), true)
        assertEquals(a.startsWith(right, fulcrum), true)

        // endsWith
        assertEquals(a.endsWith(right), true)

        fulcrum += 1
      }

      // forall
      assertEquals(
        true,
        a.forall((t: T) => a.contains(t))
      )

      // map
      val ampd:NArray[Int] = a.map[Int]( (t:T) => t.hashCode() )
      assertEquals(ampd.length, a.length)
      i = 0; while (i < N) {
        assertEquals(a(i).hashCode(), ampd(i))
        i += 1
      }

      // zipWithIndex
      val zippedWithIndex:NArray[(T, Int)] = a.zipWithIndex
      i = 0; while (i < N) {
        assertEquals(i, zippedWithIndex(i)._2)
        assertEquals[T, T](a(i), zippedWithIndex(i)._1)
        i += 1
      }

      // iterator
      val itr:Iterator[T] = a.iterator
      i = 0; while (i < N) {
        assertEquals(true, itr.hasNext)
        assertEquals(a(i), itr.next())
        i += 1
      }
      assertEquals(false, itr.hasNext)

      // grouped
      val groupSize:Int = 3
      val gi:Iterator[NArray[T]] = a.grouped(groupSize)
      i = 0
      while (gi.hasNext) {
        val group: NArray[T] = gi.next()
        val t = a.slice(i, i + groupSize)
        assertEquals(t.length, group.length)
        assertNArrayEquality[T](group, t)
        i += groupSize
      }


      // foreach
      i = 0; a.foreach((t:T) => i += 1)
      assertEquals(i, a.length)

      // indices
      assertEquals(N, a.indices.size)
    }
  }

  // only for arrays with unique elements.
  private trait NArrayOpsSearchTest[T:ClassTag] extends HasNArray[T] {

    def searchTest(): Unit = {

      var fulcrum: Int = 0; while (fulcrum < N) {

        val left: NArray[T] = NArray.tabulate[T](fulcrum)((i: Int) => a(i))
        val right: NArray[T] = NArray.tabulate[T](N - fulcrum)((i: Int) => a(fulcrum + i))

        assertEquals( a.indexOf( a( fulcrum ) ), fulcrum )

        // indexWhere
        assertEquals(
          a.indexWhere((t: T) => t == a(fulcrum)),
          fulcrum
        )

        // lastIndexOf
        assertEquals( a.lastIndexOf( a( fulcrum ) ), fulcrum )

        // takeWhile
        assertNArrayEquality[T](
          a.takeWhile((t: T) => t != a(fulcrum)),
          left
        )

        // dropWhile
        assertNArrayEquality[T](
          a.dropWhile((t: T) => t != a(fulcrum)),
          right
        )

        val (spanLeft, spanRight) = a.span((t: T) => t != a(fulcrum))
        // span
        assertNArrayEquality[T](spanLeft, left)
        assertNArrayEquality[T](spanRight, right)

        // lastIndexWhere
        assertEquals(
          a.lastIndexWhere((t: T) => t == a(fulcrum)),
          fulcrum
        )

        // count
        assertEquals(
          fulcrum, a.count( (t:T) => t match {
            case t0:Byte => t0 < a(fulcrum).asInstanceOf[Byte]
            case t0:Short => t0 < a(fulcrum).asInstanceOf[Short]
            case t0:Int => t0 < a(fulcrum).asInstanceOf[Int]
            case t0:Long => t0 < a(fulcrum).asInstanceOf[Long]
            case t0:Float => t0 < a(fulcrum).asInstanceOf[Float]
            case t0:Double => t0 < a(fulcrum).asInstanceOf[Double]
            case t0:Char => t0 < a(fulcrum).asInstanceOf[Char]
            case t0:String => Integer.parseInt(t0) < Integer.parseInt(a(fulcrum).asInstanceOf[String])
          } )
        )

        fulcrum += 1

      }

    }

  }

  private case class NArrayWithDuplicateElementsOpsTest[T:ClassTag](override val a:NArray[T]) extends UniversalNArrayOpsTest[T] {
    def test():Unit = universalTest()
  }

  private case class NArrayOfUniquelyValuedElementsOpsTest[T:ClassTag](override val a:NArray[T]) extends UniversalNArrayOpsTest[T] with NArrayOpsSearchTest[T] {
    def test(): Unit = {
      universalTest()
      searchTest()
    }

  }

  private case class NArraySelfMapOpsTest[T:ClassTag](override val a:NArray[T], val selfMap: T => T) extends HasNArray[T] {
    def test(): Unit = {

      // mapInPlace
      val aCopy:NArray[T] = a.copy
      aCopy.mapInPlace(selfMap)

      var i:Int = 0; while (i < N) {
        assertEquals(selfMap(a(i)), aCopy(i))
        i += 1
      }
    }
  }

  ////////////////
  // Value Types:
  ////////////////

  test("NArrayWithDuplicateElementsOpsTest[Unit]") { NArrayWithDuplicateElementsOpsTest[Unit](NArray.tabulate[Unit](N)(_ => ())).test() }
  test("NArrayWithDuplicateElementsOpsTest[Boolean]") {
    val t1 = NArrayWithDuplicateElementsOpsTest[Boolean](NArray.tabulate[Boolean](N)((i: Int) => i % 2 == 0))
    t1.test()
    NArraySelfMapOpsTest[Boolean](t1.a, (b: Boolean) => !b).test()
  }

  test("NArrayOfUniquelyValuedElementsOpsTest[Byte]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Byte](NArray.tabulate[Byte](N)((i:Int) => i.toByte))
    t1.test()
    NArraySelfMapOpsTest[Byte](t1.a, (b: Byte) => (-b).toByte).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[Short]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Short](NArray.tabulate[Short](N)((i: Int) => i.toShort))
    t1.test()
    NArraySelfMapOpsTest[Short](t1.a, (s: Short) => (-s).toShort).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[Int]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Int](NArray.tabulate[Int](N)((i: Int) => i))
    t1.test()
    NArraySelfMapOpsTest[Int](t1.a, (i: Int) => -i).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[Long]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Long](NArray.tabulate[Long](N)((i: Int) => i.toLong))
    t1.test()
    NArraySelfMapOpsTest[Long](t1.a, (l: Long) => -l).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[Float]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Float](NArray.tabulate[Float](N)((i: Int) => i.toFloat))
    t1.test()
    NArraySelfMapOpsTest[Float](t1.a, (f: Float) => -f).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[Double]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Double](NArray.tabulate[Double](N)((i: Int) => i.toDouble))
    t1.test()
    NArraySelfMapOpsTest[Double](t1.a, (d: Double) => -d).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[Char]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Char](NArray.tabulate[Char](N)((i: Int) => ('a'.toInt + i).toChar))
    t1.test()
    NArraySelfMapOpsTest[Char](t1.a, (c: Char) => (c.toInt + 1).toChar).test()
  }

  ////////////////////
  // Reference Types:
  ////////////////////

  test("NArrayOfUniquelyValuedElementsOpsTest[String]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[String](NArray.tabulate[String](N)((i: Int) => i.toString))
    t1.test()
    NArraySelfMapOpsTest[String](t1.a, (s: String) => s.reverse).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[AnyRef]") { NArrayWithDuplicateElementsOpsTest[AnyRef](NArray.tabulate[AnyRef](N)(_ => new AnyRef())).test() }

}