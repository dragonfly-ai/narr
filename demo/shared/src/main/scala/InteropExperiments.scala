import narr.NArray

import scala.scalajs.js.annotation.{JSExportTopLevel}
import scala.util.Random
import narr.*
import Extensions.given

import scala.language.implicitConversions
import scala.scalajs.js


object InteropExperiments {


  val r:Random = Random()
  /**
   *
   * in testing, this method demonstrated compatability with both:
var nai0 = new Int32Array(6); for (i in nai0) {nai0[i] = 2*i;} narrayInt(nai0);
  and: narrayInt([1, 2, 3.0, 4.0, 5.0])
  but not: narrayInt([1.8, 2.7, 3.6, 4.5, 5.3])
   * @param nai an Int32Array, or a non-typed JavaScript Array.
   */

  @JSExportTopLevel("readNarrayInt")
  def readNarrayInt(nai:NArray[Int]): Unit = {
    println(s"readNarrayInt($nai)")
    for (i <- nai) println(s"$i")
  }

  @JSExportTopLevel("readArrayInt")
  def readArrayInt(nai:Array[Int]): Unit = {
    println(s"readNarrayInt($nai)")
    for (i <- nai) println(s"$i")
  }

  // nai0.constructor === Int32Array
  @JSExportTopLevel("makeNarrayInt")
  def makeNarrayInt(length:Int): NArray[Int] = {
    println(s"makeNarrayInt($length)")
    NArray.tabulate[Int](length)( (i:Int) => r.nextInt(777) )
  }

  @JSExportTopLevel("makeArrayInt")
  def makeArrayInt(length:Int): Array[Int] = {
    println(s"makeArrayInt($length)")
    val out:Array[Int] = new Array[Int](length)
    out.last
    for (i <- 0 until length) out(i) = r.nextInt(777)
    out
  }

}
