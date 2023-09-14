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

import ai.dragonfly.democrossy.*
import narr.*
import Extensions.given

import scala.language.implicitConversions
import scala.util.Random as r
object TArrayDemonstration {
  def printNArrayInDetail[T](nArr: NArray[T]):Unit = {
    var i:Int = 0; while (i < nArr.length) { println(s"\tnArr($i) => ${nArr(i)}"); i += 1 }
  }
}

case class TArrayDemonstration[T] (override val name:String, nar: NArray[T]) extends Demonstration {

  def asDouble(a:T):Double = a match {
    case i:Int => i.toDouble
    case f:Float => f.toDouble
  }

  def demo():Unit = {
    println(s"nArr => $nar")
    println(s"TypedArrayOps[T](nArr) => $nar")
    println(s"length => ${nar.length}")
    val olen:Int = r.nextInt(42)
    println(s"size => ${nar.size}")
    println(s"knownSize => ${nar.knownSize}")
    println(s"isEmpty => ${nar.isEmpty}")

    println(s"head => ${nar.head}")
    println(s"last => ${nar.last}")
    println(s"headOption => ${nar.headOption}")
    println(s"lastOption => ${nar.lastOption}")
    println(s"sizeCompare($olen) => ${nar.sizeCompare(olen)}")

    println(s"sizeIs => ${nar.sizeIs}")


    println(s"ops.slice(1, nArr.length / 2) => ${nar.slice(1, nar.length / 2)}")
    println(s"ops.tail => ${nar.tail}")
    println(s"ops.init => ${nar.init}")


    println(s"ops.take(nar.length / 2) => ${nar.take(nar.length / 2)}")
    println(s"ops.drop(nar.length / 2) => ${nar.drop(nar.length / 2)}")
    println(s"ops.takeRight(nar.length / 2) => ${nar.takeRight(nar.length / 2)}")
    println(s"ops.dropRight(nar.length / 2) => ${nar.dropRight(nar.length / 2)}")

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

  def printNArrayInline[T](nArr:NArray[T]):Unit = {
    var i:Int = 0; while (i < nArr.length) { print(s"${nArr(i)}, "); i += 1 }; println()
  }

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
  )

  for (d <- demonstrations) d.demonstrate

  val nArr:NArray[Int] = NArray.tabulate[Int](7)( (_:Int) => r.nextInt(777) )
  //printNArrayInline[Int](nArr.copy)
  nArr foreach ((d:Any) => println(d) )
  printNArrayInline[String](NArray.ofSize[String](10))
//val nArr:NArray[Long] = NArray.tabulate[Long](7)( (_:Int) => r.nextLong(777L*777L) )
//val nArr: NArray[String] = NArray.tabulate[String](10)( _ => r.nextString(7) )
//val nArr: NArray[Int | String] = NArray.tabulate[Int | String](10)( (i: Int) => if (i % 2 == 0) r.nextInt() else r.nextString(7) )
//val nArr: NArray[Integer | String] = NArray.tabulate[Integer | String](10)( (i: Int) => if (i % 2 == 0) r.nextInt() else r.nextString(7) )
  //  val nArr: NArray[Long | String] = NArray.tabulate[Long | String](10)((i: Int) => if (i % 2 == 0) r.nextLong() else r.nextString(7) )
//  println(nArr)
//  println(s"length => ${nArr.length}")
  println(s"size => ${nArr.size}")
//  println(s"knownSize => ${nArr.knownSize}")

  printNArrayInline[Int](NArray.tabulate[Int](7)( (_:Int) => r.nextInt(777) ))
  print(NArray.tabulate[Int](7)( (_:Int) => r.nextInt(777) ).size)

  printNArrayInline[Long](NArray.tabulate[Long](10)( (_:Int) => r.nextLong(777L*777L) ))
  print(NArray.tabulate[Long](10)( (_:Int) => r.nextLong(777L*777L) ).size)

  printNArrayInline[String](NArray.tabulate[String](10)( _ => r.nextString(10) ))
  println(NArray.tabulate[String](10)( _ => r.nextString(10)).size)

  printNArrayInline(NArray.tabulate[String | Int](7)( (i: Int) => if (i % 2 == 0) r.nextInt() else r.nextString(7) ))
//  println(NArray.tabulate[String | Int](7)( (i: Int) => if (i % 2 == 0) r.nextInt() else r.nextString(7) ).size)

  printNArrayInline[Integer | String](NArray.tabulate[Integer | String](7)( (i: Int) => if (i % 2 == 0) r.nextInt() else r.nextString(7) ))
  print(NArray.tabulate[Integer | String](7)( (i: Int) => if (i % 2 == 0) r.nextInt() else r.nextString(7) ).size)

  printNArrayInline[Long | String](NArray.tabulate[Long | String](10)( (i: Int) => if (i % 2 == 0) r.nextLong() else r.nextString(7) ))
  print(NArray.tabulate[Long | String](10)( (i: Int) => if (i % 2 == 0) r.nextLong() else r.nextString(7) ).size)

  printNArrayInline(NArray.tabulate[String | Float](10)( (i: Int) => if (i % 2 == 0) r.nextFloat() else r.nextString(7) ))
//  println(NArray.tabulate[String | Float](10)( (i: Int) => if (i % 2 == 0) r.nextFloat() else r.nextString(7) ).size)

  val ssa: NArray[String] = NArray.tabulate[String](10)( _ => r.nextString(10) )
  printNArrayInline[String](ssa.copy)

  // Int
  val sia: NArray[Int] = NArray[Int](555, 4444, 9, 11111, 88)
  printNArrayInline[Int](sia)
  printNArrayInline[Int](sia.sorted) // alphabetical default JavaScript sorting.
  printNArrayInline[Int](sia)
  printNArrayInline[Int](sia.sorted(Ordering.Int))
  printNArrayInline[Int](sia.sorted(Ordering.Int.reverse))
  printNArrayInline[Int](sia.sort(Ordering.Int))

  printNArrayInline[Int](sia.slice(0, sia.length / 2))

  printNArrayInline[Int](sia.copy)

  val sla: NArray[Long] = NArray[Long](555L, 4444L, 9L, 11111L, 88L)
  printNArrayInline[Long](sla)
  printNArrayInline[Long](sla.sorted) // alphabetical default JavaScript sorting.
  printNArrayInline[Long](sla)
  printNArrayInline[Long](sla.sorted(Ordering.Long))
  printNArrayInline[Long](sla.sorted(Ordering.Long.reverse))
  printNArrayInline[Long](sla.sort(Ordering.Long))


  printNArrayInline[Long](sla.copy)
}