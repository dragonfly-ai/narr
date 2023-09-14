import narr.*

object Comparison {

  import munit.*
  import munit.Assertions.*

  def assertNArrayEquality[T](nArr1: NArray[T], nArr2: NArray[T])(using loc: Location, compare: Compare[T, T]): Unit = {
    assertEquals(nArr1.length, nArr2.length)
    var i: Int = 0
    while (i < nArr1.length) {
      assertEquals(nArr1(i), nArr2(i))
      i += 1
    }
  }
}
