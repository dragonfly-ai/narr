//package narr.native

/*
 * Scala (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc.
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */
//
//import scala.collection.mutable
//import scala.collection.IterableOnce
//import scala.reflect.ClassTag
//import narr.native.Extensions.*
//import narr.native.Extensions.given
//
//import scala.scalajs.js.typedarray.TypedArray
//import scala.language.implicitConversions

///** A builder class for arrays.
// *
// *  @tparam T    the type of the elements for the builder.
// */
//@SerialVersionUID(3L)
//trait TypedArrayBuilder[T <: TypedArrayPrimitive, AT <: TypedArray[T, AT]](using ct: ClassTag[T], cat: ClassTag[AT])
//  extends mutable.ReusableBuilder[T, AT]
//    with Serializable {
//  protected[this] var capacity: Int = 0
//  protected[this] var elems: AT
//  protected var size: Int = 0
//
//  def length: Int = size
//
//  override def knownSize: Int = size
//
//  protected[this] final def ensureSize(size: Int): Unit = {
//    if (capacity < size || capacity == 0) {
//      var newsize = if (capacity == 0) 16 else capacity * 2
//      while (newsize < size) newsize *= 2
//      resize(newsize)
//    }
//  }
//
//  override final def sizeHint(size: Int): Unit = if (capacity < size) resize(size)
//
//  def clear(): Unit = size = 0
//
////  protected[this] def resize(size: Int): Unit
//
//  /** Add all elements of an array */
//  def addAll(xs: AT): this.type = addAll(xs, 0, xs.length)
//
//  /** Add a slice of an array */
//  def addAll(xs: AT, offset: Int, length: Int): this.type = {
//    ensureSize(this.size + length)
//    TArray.copy(xs, offset, elems, this.size, length)
//    size += length
//    this
//  }
//
//  override def addAll(xs: IterableOnce[T]): this.type = {
//    val it = xs.iterator
//    if (it.hasNext) {
//      var k = this.size + Math.max(1, xs.knownSize)
//      while (it.hasNext) {
//        ensureSize(k)
//        while (size < capacity && it.hasNext) {
//          elems(size) = it.next()
//          size += 1
//        }
//        k = this.size + 1
//      }
//    }
//    this
//  }
//
//
//  private inline def mkArray(size: Int): AT = {
//    val newelems:AT = TArray.ofSize[T](size).asInstanceOf[AT]
//    if (this.size > 0) TArray.copy(elems, 0, newelems, 0, this.size)
//    newelems
//  }
//
//  protected[this] def resize(size: Int): Unit = {
//    elems = mkArray(size)
//    capacity = size
//  }
//
//  def addOne(elem: T): this.type = {
//    ensureSize(size + 1)
//    elems(size) = elem
//    size += 1
//    this
//  }
//
//  def result(): AT = {
//    if (capacity != 0 && capacity == size) {
//      capacity = 0
//      val res = elems
//      elems = TArray.ofSize[T](0).asInstanceOf[AT]
//      res
//    }
//    else mkArray(size)
//  }
//
//  override def equals(other: Any): Boolean = other match {
//    case x: TypedArrayBuilder[T, AT] => (size == x.size) && (elems == x.elems)
//    case _ => false
//  }
//
//  override def toString = s"TypedArrayBuilder[${elems.getClass}]"
//}