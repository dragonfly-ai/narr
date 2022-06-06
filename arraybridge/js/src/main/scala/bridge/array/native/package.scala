package bridge.array

package object native {

  type ARRAY[T] = scala.scalajs.js.Array[T]

  object ARRAY {
    export scala.scalajs.js.Array.*

    def fill[T](dimension: Int)(t: T): ARRAY[T] = {
      val values: ARRAY[T] = new ARRAY[T](dimension)
      for (i <- values.indices) values(i) = t
      values
    }

    def tabulate[T](dimension: Int)(f: Int => T): ARRAY[T] = {
      val values: ARRAY[T] = new ARRAY[T](dimension)
      for (i <- values.indices) values(i) = f(i)
      values
    }
  }
}
