package narr

package object native {

  type NArray[T] = Array[T]

  object NArray {
    export Array.*
  }

  object Extensions {

  }
}
