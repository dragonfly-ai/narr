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
import scala.util.Random as r

class NArrayOpsTest extends munit.FunSuite {

  val N: Int = 11
  val lastIndex: Int = N - 1

  private trait HasNArray[T:ClassTag] {
    val a: NArray[T]
    val nt: NArrayType
  }

  private trait UniversalNArrayOpsTest[T:ClassTag] extends HasNArray[T] {
    def universalTest(): Unit = {
      val arr: Array[T] = a.toArray[T]

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

      val sliced = a.slice(start, end)
      assertNArrayType(sliced, nt)
      val tabulated = NArray.tabulate[T](end - start)((i:Int) => a(start + i))
      assertNArrayType(tabulated, nt)
      assertNArrayEquality[T](
        sliced,
        tabulated,
        nt
      )

      // tail
      assertNArrayEquality[T](
        a.tail,
        NArray.tabulate[T](lastIndex)((i: Int) => a(1 + i)),
        nt
      )

      // init
      assertNArrayEquality[T](
        a.init,
        NArray.tabulate[T](lastIndex)((i: Int) => a(i)),
        nt
      )

      // tails
      val aTails = a.tails
      val arrTails = arr.tails
      while (aTails.hasNext && arrTails.hasNext) {
        val t = aTails.next()
        assertNArrayType(t, nt)
        assertArray2NArrayEquality(arrTails.next(), t)
        assertEquals(aTails.hasNext, arrTails.hasNext)
      }

      // inits
      val aInits = a.inits
      val arrInits = arr.inits
      while (aInits.hasNext && arrInits.hasNext) {
        val t = aInits.next()
        val arrT = arrInits.next()
        assertNArrayType(t, nt)
        //println(s"${arrT.mkString(",")}\n$t")
        assertArray2NArrayEquality(arrT, t)
        assertEquals(arrInits.hasNext, aInits.hasNext)
      }
      
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

        val lArr: Array[T] = left.toArray[T]
        val rArr: Array[T] = right.toArray[T]

        // take
        assertNArrayEquality[T]( a.take(fulcrum), left, nt)

        // drop
        assertNArrayEquality[T]( a.drop(fulcrum), right, nt )

        // takeRight
        assertNArrayEquality[T](
          a.takeRight(fulcrum),
          NArray.tabulate[T](fulcrum)((i: Int) => a(N - fulcrum + i)),
          nt
        )

        // dropRight
        assertNArrayEquality[T](
          a.dropRight(fulcrum),
          NArray.tabulate[T](N - fulcrum)((i: Int) => a(i)),
          nt
        )

        // splitAt
        val (s1:NArray[T], s2:NArray[T]) = a.splitAt(fulcrum)
        assertNArrayEquality[T](s1, left, nt)
        assertNArrayEquality[T](s2, right, nt)

        // appended
        assertArray2NArrayEquality[T](lArr.appended(arr(0)), left.appended(a(0)))
        //prepended
        assertArray2NArrayEquality[T](rArr.prepended(arr(0)), right.prepended(a(0)))

        // Doesn't work for Unit.  https://github.com/scala/bug/issues/13068
        if (a(0) != ()) {
          // prependedAll
          assertNArrayEquality[T](right.prependedAll[T](left), a, nt)
          assertNArrayEquality[T](right.prependedAll[T](left.toSeq), a, nt)

          // appendedAll
          assertNArrayEquality[T](left.appendedAll[T](right), a, nt)
          assertNArrayEquality[T](left.appendedAll[T](right.toSeq), a, nt)

          // concat, toSeq, toIndexedSeq
          assertNArrayEquality[T](left.concat[T](right), a, nt)
          assertNArrayEquality[T](left.concat[T](right.toSeq), a, nt)
        }

        // find
        a.find((t:T) => t == a(fulcrum)) match {
          case Some(t:T) => assertEquals( t, a(fulcrum) )
          case _ => assertEquals(false, true)
        }

        // exists
        assertEquals( a.exists((t: T) => t == a(fulcrum)), true)

        // contains
        assertEquals(true, a.contains(a(fulcrum)))

        // patch
        assertArray2NArrayEquality(
            arr.patch[T](0, rArr.toSeq, rArr.length),
            a.patch[T](0, right.toSeq, right.length)
        )

        // startsWith
        assertEquals(a.startsWith(left), true)
        assertEquals(a.startsWith(right, fulcrum), true)

        assertEquals(a.startsWithIterable(left.toSeq), true)
        assertEquals(a.startsWithIterable(right.toSeq, fulcrum), true)

        // endsWith
        assertEquals(a.endsWith(right), true)

        assertEquals(a.endsWithIterable(right.toSeq), true)

        // diff
        assertArray2NArrayEquality(arr.diff(lArr), a.diff(left.toSeq))
        assertArray2NArrayEquality(arr.diff(rArr), a.diff(right.toSeq))

        // intersect
        assertArray2NArrayEquality(arr.intersect(lArr), a.intersect(left.toSeq))
        assertArray2NArrayEquality(arr.intersect(rArr), a.intersect(right.toSeq))

        // zip
        assertArray2NArrayEquality(lArr.zip(rArr), left.zip(right))

        // zipAll
        val e1 = a(fulcrum)
        val e2 = a(a.length- (1 + fulcrum))
        assertArray2NArrayEquality(lArr.zipAll(rArr, e1, e2), left.zipAll(right, e1, e2))

        // updated
        assertArray2NArrayEquality(arr.updated(fulcrum, a(0)), a.updated(fulcrum, a(0)))

        fulcrum += 1
      }

      // view
      val arrView = arr.view
      val aView = a.view
      assertEquals(arrView.length, aView.length)

      i = 0; while (i < arrView.length) {
        assertEquals(arrView(i), aView(i))
        i = i + 1
      }

      // sliding
      var step = 1
      while (step < 6) {
        var width = 1
        while (width < 9) {
          val alItr: Iterator[Array[T]] = arr.sliding(width, step)
          val nalItr: Iterator[NArray[T]] = a.sliding(width, step)
          while (alItr.hasNext) {
            val a = alItr.next()
            val n = nalItr.next()
            assertArray2NArrayEquality(a, n)
          }
          assertEquals(alItr.hasNext, nalItr.hasNext)
          width = width + 1
        }
        step = step + 1
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
        assertNArrayEquality[T](group, t, nt)
        i += groupSize
      }

      // foreach
      i = 0; a.foreach((t:T) => i += 1)
      assertEquals(i, a.length)

      // distinct
      val arrD = arr.distinct
      val aD = a.distinct
      assertNArrayType[T](aD, nt)
      assertArray2NArrayEquality[T](arrD, aD)

      // padTo
      val arr_padTo = arr.padTo[T](arr.length + 5, arr(0))
      val a_padTo = a.padTo[T](arr.length + 5, arr(0))
      assertNArrayType[T](a_padTo, nt)
      assertArray2NArrayEquality[T](arr_padTo, a_padTo)

      // indices
      val arrInd = arr.indices
      val aInd = a.indices
      assertEquals(arrInd.isEmpty, aInd.isEmpty)
      assertEquals(arrInd.start, aInd.start)
      assertEquals(arrInd.end, aInd.end)
      assertEquals(arrInd.step, aInd.step)
      assertEquals(arrInd.length, aInd.length)
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
          left,
          nt
        )

        // dropWhile
        assertNArrayEquality[T](
          a.dropWhile((t: T) => t != a(fulcrum)),
          right,
          nt
        )

        val (spanLeft, spanRight) = a.span((t: T) => t != a(fulcrum))
        // span
        assertNArrayEquality[T](spanLeft, left, nt)
        assertNArrayEquality[T](spanRight, right, nt)

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

  private case class NArrayWithDuplicateElementsOpsTest[T:ClassTag](override val a:NArray[T], override val nt:NArrayType) extends UniversalNArrayOpsTest[T] {
    def test():Unit = universalTest()
  }

  private case class NArrayOfUniquelyValuedElementsOpsTest[T:ClassTag](override val a:NArray[T], override val nt:NArrayType) extends UniversalNArrayOpsTest[T] with NArrayOpsSearchTest[T] {
    def test(): Unit = {
      universalTest()
      searchTest()
    }

  }

  private case class NArraySelfMapOpsTest[T:ClassTag](override val a:NArray[T], override val nt:NArrayType, selfMap: T => T) extends HasNArray[T] {
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

  test("NArrayWithDuplicateElementsOpsTest[Unit]") {
    NArrayWithDuplicateElementsOpsTest[Unit](NArray.tabulate[Unit](N)(_ => ()), NATIVE_ARRAY).test()
  }
  test("NArrayWithDuplicateElementsOpsTest[Boolean]") {
    val a1: NArray[Boolean] = NArray.tabulate[Boolean](N)((i: Int) => i % 2 == 0)
    val t1 = NArrayWithDuplicateElementsOpsTest[Boolean](a1, NATIVE_ARRAY)
    t1.test()
    NArraySelfMapOpsTest[Boolean](t1.a, NATIVE_ARRAY, (b: Boolean) => !b).test()
  }

  test("NArrayOfUniquelyValuedElementsOpsTest[Byte]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Byte](NArray.tabulate[Byte](N)((i:Int) => i.toByte), BYTE_ARRAY)
    t1.test()
    NArraySelfMapOpsTest[Byte](t1.a, BYTE_ARRAY, (b: Byte) => (-b).toByte).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[Short]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Short](NArray.tabulate[Short](N)((i: Int) => i.toShort), SHORT_ARRAY)
    t1.test()
    NArraySelfMapOpsTest[Short](t1.a, SHORT_ARRAY, (s: Short) => (-s).toShort).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[Int]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Int](NArray.tabulate[Int](N)((i: Int) => i), INT_ARRAY)
    t1.test()
    NArraySelfMapOpsTest[Int](t1.a, INT_ARRAY, (i: Int) => -i).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[Long]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Long](NArray.tabulate[Long](N)((i: Int) => i.toLong), NATIVE_ARRAY)
    t1.test()
    NArraySelfMapOpsTest[Long](t1.a, NATIVE_ARRAY, (l: Long) => -l).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[Float]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Float](NArray.tabulate[Float](N)((i: Int) => i.toFloat), FLOAT_ARRAY)
    t1.test()
    NArraySelfMapOpsTest[Float](t1.a, FLOAT_ARRAY, (f: Float) => -f).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[Double]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Double](NArray.tabulate[Double](N)((i: Int) => i.toDouble), DOUBLE_ARRAY)
    t1.test()
    NArraySelfMapOpsTest[Double](t1.a, DOUBLE_ARRAY, (d: Double) => -d).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[Char]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[Char](NArray.tabulate[Char](N)((i: Int) => ('a'.toInt + i).toChar), NATIVE_ARRAY)
    t1.test()
    NArraySelfMapOpsTest[Char](t1.a, NATIVE_ARRAY, (c: Char) => (c.toInt + 1).toChar).test()
  }

  ////////////////////
  // Reference Types:
  ////////////////////

  test("NArrayOfUniquelyValuedElementsOpsTest[String]") {
    val t1 = NArrayOfUniquelyValuedElementsOpsTest[String](NArray.tabulate[String](N)((i: Int) => i.toString), NATIVE_ARRAY)
    t1.test()
    NArraySelfMapOpsTest[String](t1.a, NATIVE_ARRAY, (s: String) => s.reverse).test()
  }
  test("NArrayOfUniquelyValuedElementsOpsTest[AnyRef]") { NArrayWithDuplicateElementsOpsTest[AnyRef](NArray.tabulate[AnyRef](N)(_ => new AnyRef()), NATIVE_ARRAY).test() }

}