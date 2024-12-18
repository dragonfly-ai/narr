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

/**
 * Scale back the ambitions of the NArrayBuilder!
 * All it needs to do is provide the functionality of a growable array.
 * Don't worry about resizing or anything else beyond increasing capacity as allocated indices fill up.
 */

package narr
import narr.*

import narr.native.NativeArrayBuilder

import scala.compiletime.erasedValue
import scala.reflect.ClassTag

object NArrayBuilder {
  val DefaultInitialSize:Int = 16
  val MAX_NArraySize:Int = 2147483639

  transparent inline def apply[A:ClassTag](initialCapacity: Int = DefaultInitialSize)(using ClassTag[NArray[A]]):NArrayBuilder[A] = (inline erasedValue[A] match {
    case _: Byte => ByteArrayBuilder(initialCapacity)
    case _: Short => ShortArrayBuilder(initialCapacity)
    case _: Int => IntArrayBuilder(initialCapacity)
    case _: Float => FloatArrayBuilder(initialCapacity)
    case _: Double => DoubleArrayBuilder(initialCapacity)
    case _ => NativeArrayBuilder[A](initialCapacity)
  }).asInstanceOf[NArrayBuilder[A]]

//  def main(args:Array[String]):Unit = {
//    var i:Int = DefaultInitialSize
//    var li:Int = 0
//    var c:Int = 0
//    while (i > 0) {
//      println(s"$i $c ${c + 1}")
//      c += 1
//      li = i
//      i = i * 2
//    }
//    println(s"$MAX_NArraySize - $li = ${MAX_NArraySize - li}")
//    println(s"(Math.log(MAX_NArraySize) / Math.log(2)) - (Math.log($DefaultInitialSize) / Math.log(2)) = ${(Math.log(MAX_NArraySize) / Math.log(2))} - ${(Math.log(DefaultInitialSize) / Math.log(2))} = ${Math.ceil((Math.log(MAX_NArraySize) / Math.log(2)) - (Math.log(DefaultInitialSize) / Math.log(2)))}")
//  }


}

/**
 * Design notes:
 *   - If you already know the size of the result, you wouldn't use a builder at all.
 *   - Minimize memory footprint and copy operations.
 *   - som-snytt
 *     + That's an interesting experiment worth benchmarking. Personally I'd optimize for new Builder().addAll(xs).result but it depends.
 *     + I like the idea of keeping the "strategy" in a var instead of a confusing if/else.
 *     + Alternatively, switch on the current state
 *
 * @tparam T type of the Array elements.
 */

trait NArrayBuilder[T] {
  def makeNArray(len:Int): NArray[T]
  def copyInto(src: NArray[T], dest: NArray[T], dstPos:Int): Unit
  def copyInto(src: NArray[T], srcPos:Int, dest: NArray[T], dstPos:Int, length:Int): Unit
  def size:Int
  def addOne(e:T):this.type
  def addAll(es: NArray[T]):this.type
  /** Add a slice of an array. */
  def addAll(xs: NArray[T], offset: Int, length: Int): this.type = {
    val offset1 = offset.max(0)
    addAll(
      xs.slice(
        offset1,
        length.max(0).min(xs.length - offset1)
      )
    )
  }
  def addAll(itr: Iterator[T]): this.type = {
    while (itr.hasNext) addOne(itr.next())
    this
  }
  def addAll(xs: IterableOnce[T]): this.type = {
    addAll(xs.iterator)
    this
  }
  def result: NArray[T]
  def apply(idx: Int): T
  inline def +=(e:T): this.type = addOne(e)
  inline def ++=(xs:NArray[T]): this.type = addAll(xs)
  inline def ++=(itr: Iterator[T]): this.type = addAll(itr)
  inline def ++=(xs: IterableOnce[T]): this.type = addAll(xs)
}

trait TypedArrayBuilder[T](using ClassTag[T]) extends NArrayBuilder[T] {
  //type AT <: NArray[T]
  protected[this] val initCapacity:Int // = NArrayBuilder.DefaultInitialSize
  private var capacity: Int = 0

  def make2DNArray(len: Int): NArray[NArray[T]]

  private enum NArrayBuilderState:
    case UNINITIALIZED, FIRST_WORKING_ARRAY, BUCKETS
  import NArrayBuilderState.*
  private var state: NArrayBuilderState = UNINITIALIZED

  private var b: Int = 0  // next bucket index.
  private var i: Int = 0  // next workingArray index.
  private var workingArray: NArray[T] = makeNArray(0)

  private lazy val buckets: NArray[NArray[T]] = {
    val ln2:Double = Math.log(2)
    val maxBucketCount:Double = Math.log(NArrayBuilder.MAX_NArraySize) / ln2

    make2DNArray(
      1 + (
        if (i <= 0) Math.ceil(maxBucketCount - (Math.log(workingArray.length) / ln2)).toInt
        else Math.ceil(maxBucketCount - (Math.log(initCapacity) / ln2)).toInt
      )
    )
  }

  private inline def availableWorkingSpace:Int = workingArray.length - i

  def size:Int = capacity - availableWorkingSpace

  private def allocateNextWorkingArray(bucketCap:Int):Unit = {
    // assume that size == capacity.
    val maxAvailableCapacity: Int = NArrayBuilder.MAX_NArraySize - capacity
    if (maxAvailableCapacity <= 0) throw Exception(s"NArrayBuilder Overflow. Max capacity: ${NArrayBuilder.MAX_NArraySize}; current capacity: $capacity; requested increase: $bucketCap.")

    state match {
      case UNINITIALIZED =>
        state = FIRST_WORKING_ARRAY
      case FIRST_WORKING_ARRAY =>
        buckets(b) = workingArray
        b += 1
        state = BUCKETS
      case BUCKETS =>
        buckets(b) = workingArray
        b += 1
    }

    val safeBucketCap:Int = if (bucketCap < maxAvailableCapacity) bucketCap else maxAvailableCapacity
    workingArray = makeNArray(safeBucketCap)
    capacity = capacity + safeBucketCap
    i = 0

  }

  override def addOne(e:T):this.type = {
    state match {
      case UNINITIALIZED =>
        allocateNextWorkingArray(initCapacity)
      case FIRST_WORKING_ARRAY =>
        if (i >= workingArray.length) {
          allocateNextWorkingArray(capacity)
        }
      case BUCKETS =>
        if (i >= workingArray.length) allocateNextWorkingArray(capacity)
    }

    workingArray(i) = e
    i = i + 1

    this
  }

  // Einstein spent 9 years unemployed.

  override def addAll(es: NArray[T]):this.type = {
    state match {
      case UNINITIALIZED =>
        allocateNextWorkingArray(
          if (es.length <= NArrayBuilder.MAX_NArraySize / 2) Math.max(initCapacity, 2 * es.length)
          else es.length
        )
        copyInto(es, workingArray, i)
        i = i + es.length
      case _ =>
        if (es.length <= availableWorkingSpace) {
          copyInto(es, workingArray, i)
          i = i + es.length
        } else {
          val len0 = availableWorkingSpace
          copyInto(es, 0, workingArray, i, len0)
          i = i + len0
          val len1 = es.length - len0
          allocateNextWorkingArray(Math.max(capacity, len1))
          copyInto(es, len0, workingArray, i, len1)
          i = i + len1
        }
    }
    this
  }

  override def result: NArray[T] = {
    // this method should store the result in buckets(0) and clear all other buckets.
    state match {
      case UNINITIALIZED => makeNArray(size)
      case FIRST_WORKING_ARRAY => workingArray.slice(0, i)
      case BUCKETS =>
        val r:NArray[T] = makeNArray(size)
        var bI: Int = 0
        var j: Int = 0
        while (bI < b) {
          val bucket:NArray[T] = buckets(bI)
          copyInto(bucket, r, j)
          j = j + bucket.length
          bI = bI + 1
        }
        if (i > 0) {
          copyInto(workingArray.slice(0, i), r, j)
        }
        r
    }
  }

  override def apply(idx: Int): T = {
    state match {
      case UNINITIALIZED => throw new ArrayIndexOutOfBoundsException(s"NArrayBuilder not yet initialized.")
      case FIRST_WORKING_ARRAY => workingArray(idx)
      case BUCKETS =>
        if (idx >= size) throw new ArrayIndexOutOfBoundsException(s"Index: $idx is out of bounds for NArrayBuilder of size $size.")
        var j:Int = idx
        var b0: Int = 0; while (b0 < b && j >= buckets(b0).length) {
          j = j - buckets(b0).length
          b0 = b0 + 1
        }
        if (b0 == b) workingArray(j)
        else buckets(b0)(j)
    }
  }
}

case class ByteArrayBuilder (override val initCapacity:Int = NArrayBuilder.DefaultInitialSize) extends TypedArrayBuilder[Byte] {
  //override type AT = ByteArray
  //override val clz:Class[Byte] = classOf[Byte]
  override inline def makeNArray(len: Int): NArray[Byte] = new ByteArray(len)

  override inline def make2DNArray(len: Int): NArray[NArray[Byte]] = NArray.ofSize[NArray[Byte]](len)

  override def toString = "ArrayBuilder.ofRef"

  override inline def copyInto(src: ByteArray, dest: ByteArray, dstPos: Int): Unit = NArray.copyByteArray(
    src, dest, dstPos
  )
  override inline def copyInto(src: ByteArray, srcPos: Int, dest: ByteArray, dstPos: Int, length: Int): Unit = {
    NArray.copyByteArray(src, srcPos, dest, dstPos, length)
  }

}


case class ShortArrayBuilder (override val initCapacity:Int = NArrayBuilder.DefaultInitialSize) extends TypedArrayBuilder[Short] {
  //override val clz:Class[Short] = classOf[Short]
  override inline def makeNArray(len: Int): NArray[Short] = new ShortArray(len)

  override inline def make2DNArray(len: Int): NArray[NArray[Short]] = NArray.ofSize[NArray[Short]](len)

  override inline def copyInto(src: ShortArray, dest: ShortArray, dstPos: Int): Unit = NArray.copyShortArray(
    src, dest, dstPos
  )
  override inline def copyInto(src: ShortArray, srcPos: Int, dest: ShortArray, dstPos: Int, length: Int): Unit = {
    NArray.copyShortArray(src, srcPos, dest, dstPos, length)
  }
}


case class IntArrayBuilder (override val initCapacity:Int = NArrayBuilder.DefaultInitialSize) extends TypedArrayBuilder[Int] {
  //override val clz:Class[Int] = classOf[Int]
  override inline def makeNArray(len: Int): NArray[Int] = new IntArray(len)

  override inline def make2DNArray(len: Int): NArray[NArray[Int]] = NArray.ofSize[NArray[Int]](len)

  override inline def copyInto(src: IntArray, dest: IntArray, dstPos: Int): Unit = NArray.copyIntArray(
    src, dest, dstPos
  )
  override inline def copyInto(src: IntArray, srcPos: Int, dest: IntArray, dstPos: Int, length: Int): Unit = {
    NArray.copyIntArray(src, srcPos, dest, dstPos, length)
  }
}


case class FloatArrayBuilder (override val initCapacity:Int = NArrayBuilder.DefaultInitialSize) extends TypedArrayBuilder[Float] {
  //override val clz:Class[Float] = classOf[Float]
  override inline def makeNArray(len: Int): NArray[Float] = new FloatArray(len)

  override inline def make2DNArray(len: Int): NArray[NArray[Float]] = NArray.ofSize[NArray[Float]](len)

  override inline def copyInto(src: FloatArray, dest: FloatArray, dstPos: Int): Unit = NArray.copyFloatArray(
    src, dest, dstPos
  )
  override inline def copyInto(src: FloatArray, srcPos: Int, dest: FloatArray, dstPos: Int, length: Int): Unit = {
    NArray.copyFloatArray(src, srcPos, dest, dstPos, length)
  }
}


case class DoubleArrayBuilder (override val initCapacity:Int = NArrayBuilder.DefaultInitialSize) extends TypedArrayBuilder[Double] {
  //override val clz:Class[Double] = classOf[Double]
  override inline def makeNArray(len: Int): NArray[Double] = new DoubleArray(len)

  override inline def make2DNArray(len: Int): NArray[NArray[Double]] = NArray.ofSize[NArray[Double]](len)

  override inline def copyInto(src: DoubleArray, dest: DoubleArray, dstPos: Int): Unit = NArray.copyDoubleArray(
    src, dest, dstPos
  )
  override inline def copyInto(src: DoubleArray, srcPos: Int, dest: DoubleArray, dstPos: Int, length: Int): Unit = {
    NArray.copyDoubleArray(src, srcPos, dest, dstPos, length)
  }
}