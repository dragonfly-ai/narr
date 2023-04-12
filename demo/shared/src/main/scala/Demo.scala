//import InteropExperiments.*
import ai.dragonfly.democrossy.*
import narr.*
import Extensions.given

import scala.collection.ArrayOps
import scala.language.implicitConversions
import scala.util.Random

object DemoScope {
  val r:Random = Random()
}

import DemoScope.*
case class TArrayDemonstration[AT <: NativeTypedArray] (override val name:String, nar: AT) extends Demonstration {

  type A = ArrayElementType[AT]
 // def arr2Ops(a:AT): TypedArrayOps[T, AT] = a

  def asDouble(a:A):Double = a match {
    case i:Int => i.toDouble
    case f:Float => f.toDouble
  }


  def demo():Unit = {
    //val nArr0:TypedArrayOps[AT] = new TypedArrayOps[AT](nArr0)
    println(s"nArr => $nar")
    println(s"TypedArrayOps[T](nArr) => $nar")
    println(s"length => ${nar.length}")
    val olen:Int = r.nextInt(42)
    println(s"size => ${nar.size}")
    println(s"knownSize => ${nar.knownSize}")
    println(s"isEmpty => ${nar.isEmpty}")
//    println(s"nonEmpty => ${ops.nonEmpty}")
    println(s"head => ${nar.head}")
    println(s"last => ${nar.last}")
    println(s"headOption => ${nar.headOption}")
    println(s"lastOption => ${nar.lastOption}")
    println(s"sizeCompare($olen) => ${nar.sizeCompare(olen)}")
//    println(s"lengthCompare($olen) => ${nArr0.lengthCompare(olen)}")
    println(s"sizeIs => ${nar.sizeIs}")
//    println(s"lengthIs => ${nar.lengthIs}")
//    println(s"indices => ${nar.indices()}")

    println(s"ops.slice(1, nArr.length / 2) => ${nar.slice(1, nar.length / 2)}")
    println(s"ops.tail => ${nar.tail}")
    println(s"ops.init => ${nar.init}")

//    val tails = ops.tails
//    println(s"val tails = ops.tails => $tails")
//    println("while (tails.hasNext) println(tails.next) {")
//    while (tails.hasNext) println(tails.next)
//    println("}")
//
//    val inits = ops.inits
//    println(s"val inits = ops.inits => $inits")
//    println("while (inits.hasNext) println(inits.next) {")
//    while (inits.hasNext) println(inits.next)
//    println("}")

    println(s"ops.take(nar.length / 2) => ${nar.take(nar.length / 2)}")
    println(s"ops.drop(nar.length / 2) => ${nar.drop(nar.length / 2)}")
    println(s"ops.takeRight(nar.length / 2) => ${nar.takeRight(nar.length / 2)}")
    println(s"ops.dropRight(nar.length / 2) => ${nar.dropRight(nar.length / 2)}")
//    println(s"ops.takeWhile((a:T) => asDouble(a) < asDouble(nar(nar.length / 2))) => ${nArr0.takeWhile((a:A) => asDouble(a) < asDouble(nArr0(nArr0.length / 2).asInstanceOf[A]))}")
//    println(s"ops.dropWhile((a:T) => asDouble(a) < asDouble(nar(nar.length / 2))) => ${nArr0.dropWhile((a:A) => asDouble(a) < asDouble(nArr0(nArr0.length / 2).asInstanceOf[A]))}")

//    val stepper = ops.stepper
//    println(s"val stepper = ops.stepper => $stepper")
//    println("while (stepper.hasStep) println(stepper.nextStep()) {")
//    while (stepper.hasStep) println(stepper.nextStep())
//    println("}")

    println("Iteration/Access:")

    println("for (i <- 0 until nArr.length) println(s\"\tnArr($i) => ${nArr(i)}\") => {")
    for (i <- 0 until nar.length) println(s"\tnArr($i) => ${nar(i)}")
    println("}")

    println("var i:Int = 0; while (i < nArr.length) { println(s\"\\nArr($i) => ${nArr(i)}\"); i += 1 } => {")
    var i:Int = 0; while (i < nar.length) { println(s"\tnArr($i) => ${nar(i)}"); i += 1 }
    println("}")

    println("ops.foreach((d:Int) => println(d)) => {")
    nar foreach(_ => println(_))
    println("}")
  }
}

object Demo extends XApp(NativeConsole(style = "padding: 8px; overflow: scroll;")) with App {

//  readNarrayInt(NArray(1, 2, 3))

  val demonstrations: Array[Demonstration] = Array[Demonstration](
    TArrayDemonstration( "NArray[Byte](1, 2, 3)", {
      val ba:ByteArray = new ByteArray(5)
      var i:Byte = 0; while (i < ba.length) { ba(i) = i; i = (i + 1).toByte }
      ba
    } ),
    TArrayDemonstration( "NArray[Short](1, 2, 3)", {
      val sa: ShortArray = new ShortArray(8)
      var i: Short = 0; while (i < sa.length) { sa(i) = i; i = (i + 1).toShort }
      sa
    } ),
    TArrayDemonstration( "NArray[Int](1, 2, 3)", {
      val ia: IntArray = new IntArray(13)
      var i: Int = 0; while (i < ia.length) { ia(i) = i; i += 1 }
      ia
    } ),
    TArrayDemonstration( "NArray.fill[Float](4)( (2.0*Math.PI).toFloat )", NArray.fill[Float](8)( (2.0*Math.PI).toFloat ) ),
    TArrayDemonstration( "NArray.tabulate[Double](4)( (i:Int) => i * Math.PI ) )", NArray.tabulate[Double](8)( (i:Int) => i * Math.PI ) ),
//    StringNArrayDemonstration( "NArray[String](\"Testing\", \"1\", \"2\", \"3\")", NArray[String]("Testing", "1", "2", "3") ),
//    CharNArrayDemonstration( "NArray[Char]('a', 'b', 'c', 'X', 'Y', 'Z')", NArray[Char]('a', 'b', 'c', 'X', 'Y', 'Z') ),
//    LongNArrayDemonstration( "NArray.tabulate[Long](7)( (_:Int) => r.nextLong(777L*777L) )", NArray.tabulate[Long](7)( (_:Int) => r.nextLong(777L*777L) ) ),
  )

  for (d <- demonstrations) d.demonstrate

  val nArr:NArray[Int] = NArray.tabulate[Int](7)( (_:Int) => r.nextInt(777) )
  nArr foreach ((d:Any) => println(d) )
  println(NArray.ofSize[String](10))
//val nArr:NArray[Long] = NArray.tabulate[Long](7)( (_:Int) => r.nextLong(777L*777L) )
//val nArr: NArray[String] = NArray.tabulate[String](10)( _ => r.nextString(7) )
//val nArr: NArray[Int | String] = NArray.tabulate[Int | String](10)( (i: Int) => if (i % 2 == 0) r.nextInt() else r.nextString(7) )
//val nArr: NArray[Integer | String] = NArray.tabulate[Integer | String](10)( (i: Int) => if (i % 2 == 0) r.nextInt() else r.nextString(7) )
  //  val nArr: NArray[Long | String] = NArray.tabulate[Long | String](10)((i: Int) => if (i % 2 == 0) r.nextLong() else r.nextString(7) )
//  println(nArr)
//  println(s"length => ${nArr.length}")
//  println(s"size => ${nArr.size}")
//  println(s"knownSize => ${nArr.knownSize}")

  println(NArray.tabulate[Int](7)( (_:Int) => r.nextInt(777) ))
  println(NArray.tabulate[Int](7)( (_:Int) => r.nextInt(777) ).size)

  println(NArray.tabulate[Long](10)( (_:Int) => r.nextLong(777L*777L) ))
  println(NArray.tabulate[Long](10)( (_:Int) => r.nextLong(777L*777L) ).size)

  println(NArray.tabulate[String](10)( _ => r.nextString(10) ))
  println(NArray.tabulate[String](10)( _ => r.nextString(10)).size)

  println(NArray.tabulate[String | Int](7)( (i: Int) => if (i % 2 == 0) r.nextInt() else r.nextString(7) ))
//  println(NArray.tabulate[String | Int](7)( (i: Int) => if (i % 2 == 0) r.nextInt() else r.nextString(7) ).size)

  println(NArray.tabulate[Integer | String](7)( (i: Int) => if (i % 2 == 0) r.nextInt() else r.nextString(7) ))
  println(NArray.tabulate[Integer | String](7)( (i: Int) => if (i % 2 == 0) r.nextInt() else r.nextString(7) ).size)

  println(NArray.tabulate[Long | String](10)( (i: Int) => if (i % 2 == 0) r.nextLong() else r.nextString(7) ))
  println(NArray.tabulate[Long | String](10)( (i: Int) => if (i % 2 == 0) r.nextLong() else r.nextString(7) ).size)

  println(NArray.tabulate[String | Float](10)( (i: Int) => if (i % 2 == 0) r.nextFloat() else r.nextString(7) ))
//  println(NArray.tabulate[String | Float](10)( (i: Int) => if (i % 2 == 0) r.nextFloat() else r.nextString(7) ).size)


}
