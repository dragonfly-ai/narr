import Util.*
import Util.NArrayType.*
import narr.*

import scala.reflect.ClassTag

class UnzipTest extends munit.FunSuite {

  val N:Int = 11

  private case class Unzip[T, A1, A2](
    a: NArray[T], f: Function1[T, (A1, A2)],
    ntA1: NArrayType, ntA2: NArrayType
  )(using ClassTag[T], ClassTag[A1], ClassTag[A2]) {

    given asPair: Function1[T, (A1, A2)] = f

    def test():Unit = {
      val (a1, a2) = a.unzip[A1, A2]
      assertEquals(a1.length, a2.length)
      assertNArrayType[A1](a1, ntA1)
      assertNArrayType[A2](a2, ntA2)

      // compare to scala array unzip
      val arr: Array[T] = a.toArray[T]
      val (sa1, sa2) = arr.unzip
      assertArray2NArrayEquality[A1](sa1, a1)
      assertArray2NArrayEquality[A2](sa2, a2)
    }
  }

  // direct
  test("Unzip Directly NArray[Double] ") {
    val arr: NArray[Double] = NArray.tabulate[Double](N)((i: Int) => i / Math.PI)
    given asPair:Function1[Double, (Double, Double)] = (d:Double) => (-d, d)
    val (a1, a2) = arr.unzip
    assertEquals(a1.length, a2.length)
    assertNArrayType[Double](a1, DOUBLE_ARRAY)
    assertNArrayType[Double](a2, DOUBLE_ARRAY)
    var i = 0
    while (i < arr.length) {
      assertEquals(-a1(i), a2(i))
      i = i + 1
    }
  }

  // indirect
  test("UnzipTest[Byte => (Int, Int)]") {
    Unzip[Byte, Int, Int](
      NArray.tabulate[Byte](N)((i: Int) => i.toByte),
      (i:Byte) => (-i.toInt, i.toInt),
      INT_ARRAY, INT_ARRAY
    ).test()
  }

  test("UnzipTest[Int => (Int, Int)]") {
    Unzip[Int, Int, Int](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i:Int) => (-i, i),
      INT_ARRAY, INT_ARRAY
    ).test()
  }

  test("UnzipTest[Int => (Double, String)]") {
    Unzip[Int, Double, String](
      NArray.tabulate[Int](N)((i: Int) => i),
      (i:Int) => (i.toDouble/3.0, i.toString),
      DOUBLE_ARRAY, NATIVE_ARRAY
    ).test()
  }

  test("UnzipTest[String]") {
    Unzip[String, String, String](
      NArray.tabulate[String](N)((i: Int) => i.toString),
      (s: String) => (s.reverse, s),
      NATIVE_ARRAY, NATIVE_ARRAY
    ).test()
  }
}
