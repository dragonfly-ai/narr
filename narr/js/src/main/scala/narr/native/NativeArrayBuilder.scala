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

package narr.native

import narr.{NArray, NArrayBuilder, NativeArray}

import scala.reflect.ClassTag


// in JavaScript, just use a js.Array.
case class NativeArrayBuilder[T](initCapacity:Int = NArrayBuilder.DefaultInitialSize)(using ClassTag[T]) extends NArrayBuilder[T] {
  //given clz:Class[T] = implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]]
  override inline def makeNArray(len: Int): NArray[T] = new scala.scalajs.js.Array[T]().asInstanceOf[NArray[T]]

  override inline def copyInto(src: NArray[T], dest: NArray[T], dstPos: Int): Unit = NArray.copyNativeArray(
    src.asInstanceOf[NativeArray[T]], dest.asInstanceOf[NativeArray[T]], dstPos
  )
  override inline def copyInto(src: NArray[T], srcPos: Int, dest: NArray[T], dstPos: Int, length: Int): Unit = {
    NArray.copyNativeArray(src.asInstanceOf[NativeArray[T]], srcPos, dest.asInstanceOf[NativeArray[T]], dstPos, length)
  }

  private var elements: scala.scalajs.js.Array[T] = new scala.scalajs.js.Array[T](initCapacity).asInstanceOf[NativeArray[T]]

  private var length:Int = 0

  override def size: Int = length

  override def addOne(e: T): this.type = {
    elements(length) = e
    length = length + 1
    this
  }

  override def addAll(es: NArray[T]): this.type = {
    //copyInto(es, elements.asInstanceOf[NArray[T]], length) // slow copy
    elements = result.asInstanceOf[NArr[T]].concat(es.asInstanceOf[NArr[T]]).asInstanceOf[scala.scalajs.js.Array[T]]
    length = length + es.asInstanceOf[NArr[T]].length
    this
  }

  override def result: NArray[T] = {
    elements.slice(0, length).asInstanceOf[NArray[T]]
  }

  override def apply(idx: Int): T = if (idx > -1 && idx < length) elements(idx) else throw ArrayIndexOutOfBoundsException(
    s"No index: $idx for array builder of length: $length."
  )
}
