import InteropExperiments.*
import ai.dragonfly.democrossy.*
import narr.*
import Extensions.given
import scala.language.implicitConversions

case class TArrayDemonstration[T <: TypedArrayPrimitive] (override val name:String, nArr: TArray[T]) extends Demonstration {

  def arr2Ops(a:TArray[T]): TypedArrayOps[T] = new TypedArrayOps[T](a.asInstanceOf[NArr[T]])

  def demo():Unit = {
    def ops:TypedArrayOps[T] = arr2Ops(nArr)
    val nar:NArr[T] = nArr.asInstanceOf[NArr[T]]
    println(s"nArr => $nArr")
    println(s"TypedArrayOps[T](nArr) => $ops")
    println(s"length => ${nar.length}")
    val olen:Int = r.nextInt(42)
    println(s"size => ${ops.size}")
    println(s"knownSize => ${ops.knownSize}")
    println(s"isEmpty => ${ops.isEmpty}")
    println(s"nonEmpty => ${ops.nonEmpty}")
    println(s"head => ${ops.head}")
    println(s"last => ${ops.last}")
    println(s"headOption => ${ops.headOption}")
    println(s"lastOption => ${ops.lastOption}")
    println(s"sizeCompare($olen) => ${ops.sizeCompare(olen)}")
    println(s"lengthCompare($olen) => ${ops.lengthCompare(olen)}")
    println(s"sizeIs => ${ops.sizeIs}")
    println(s"lengthIs => ${ops.lengthIs}")
    println(s"indices => ${ops.indices}")

    println("Iteration/Access:")

    println("for (i <- 0 until nArr.length) println(s\"\tnArr($i) => ${nArr(i)}\") => {")
    for (i <- 0 until nar.length) println(s"\tnArr($i) => ${nar(i)}")
    println("}")

    println("var i:Int = 0; while (i < nArr.length) { println(s\"\\nArr($i) => ${nArr(i)}\"); i += 1 } => {")
    var i:Int = 0; while (i < nar.length) { println(s"\tnArr($i) => ${nar(i)}"); i += 1 }
    println("}")

    println("ops.foreach((d:Int) => println(d)) => {")
    ops.foreach((d:T) => println(d))
    println("}")

  }
}

object Demo extends XApp(DivConsole(style = "padding: 8px; overflow: scroll;")) with App {

  readNarrayInt(NArray(1, 2, 3))

  val demonstrations: Array[Demonstration] = Array[Demonstration](
    TArrayDemonstration[Byte]( "NArray[Byte](1, 2, 3)", NArray[Byte](1, 2, 3) ),
    TArrayDemonstration[Short]( "NArray[Short](1, 2, 3)", NArray[Short](1, 2, 3) ),
    TArrayDemonstration[Int]( "NArray[Int](1, 2, 3)", NArray[Int](1, 2, 3) ),
    TArrayDemonstration[Float]( "NArray.fill[Float](4)( (2.0*Math.PI).toFloat )", NArray.fill[Float](4)( (2.0*Math.PI).toFloat ) ),
    TArrayDemonstration[Double]( "NArray.tabulate[Double](4)( (i:Int) => i * Math.PI ) )", NArray.tabulate[Double](4)( (i:Int) => i * Math.PI ) ),
//    StringNArrayDemonstration( "NArray[String](\"Testing\", \"1\", \"2\", \"3\")", NArray[String]("Testing", "1", "2", "3") ),
//    CharNArrayDemonstration( "NArray[Char]('a', 'b', 'c', 'X', 'Y', 'Z')", NArray[Char]('a', 'b', 'c', 'X', 'Y', 'Z') ),
//    LongNArrayDemonstration( "NArray.tabulate[Long](7)( (_:Int) => r.nextLong(777L*777L) )", NArray.tabulate[Long](7)( (_:Int) => r.nextLong(777L*777L) ) ),
  )

  for (d <- demonstrations) d.demonstrate

//  val nArr: NArray[Int] = NArray.ofSize[Int](1)
//  val nArr:NArray[Long] = NArray.tabulate[Long](7)( (_:Int) => r.nextLong(777L*777L) )
//  val nArr: NArray[String] = NArray.tabulate[String](10)( _ => r.nextString(7) )
  val nArr: NArray[String | Integer] = NArray.tabulate[String | Integer](10)( (i: Int) => if (i % 2 == 0) r.nextInt() else r.nextString(7) )
//  val nArr: NArray[Long | String] = NArray.tabulate[Long | String](10)((i: Int) => if (i % 2 == 0) r.nextLong() else r.nextString(7) )

  println(nArr)

  println(s"length => ${nArr.length}")

  println(s"size => ${nArr.size}")
  println(s"knownSize => ${nArr.knownSize}")
}
