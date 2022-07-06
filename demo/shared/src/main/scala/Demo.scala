import InteropExperiments.*
import ai.dragonfly.democrossy.XApp
import ai.dragonfly.democrossy.native.DivConsole
import narr.*
import narr.native.Extensions.given

object Demo extends XApp(DivConsole(style = "padding: 8px; overflow: scroll;")) with App {

  val arr1: NArray[String] = NArray("Testing", "1", "2", "3")
  val arr2: NArray[Int] = NArray(1, 2, 3)

  for (i <- arr1.indices) {
    arr1(i) = arr1(i) + " OK!"
  }

  for (i <- 0 until arr1.length) println(s"\tarr1($i) is ${arr1(i)}")

  print("\n")

  for (i <- arr2.indices) arr2(i) = arr2(i) * 2
  for (i <- 0 until arr2.length) println(s"\tarr2($i) is ${arr2(i)}")

  readNarrayInt(arr2)

  val arr3:NArray[Int] = makeNarrayInt(12)
  for (i <- 0 until arr3.length) println(s"\tarr2($i) is ${arr3(i)}")

  arr2.foreach((d:Int) => println(d))

}
