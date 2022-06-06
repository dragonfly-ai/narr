package bridge

package object array {

  type ARRAY[T] = bridge.array.native.ARRAY[T]
  val ARRAY: bridge.array.native.ARRAY.type = bridge.array.native.ARRAY

}
