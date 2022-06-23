import narr.ARRAY

object Demo extends App {

  val arr1: ARRAY[String] = ARRAY("Testing", "1", "2", "3")
  val arr2: ARRAY[Int] = ARRAY(1, 2, 3)

  for (i <- arr1.indices) println(arr1(i))

  println("")

  for (i <- arr2.indices) println(arr2(i))

}
