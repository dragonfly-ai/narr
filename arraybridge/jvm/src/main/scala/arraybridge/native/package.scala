package arraybridge

package object native {

  type ARRAY[T] = Array[T]

  object ARRAY {
    export Array.*
  }

}
