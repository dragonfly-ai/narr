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

import narr.{NArray, NArrayBuilder, NativeArray, TypedArrayBuilder}

import scala.reflect.ClassTag


// in JavaScript, just use a js.Array.
case class NativeArrayBuilder[T](override val initCapacity:Int = NArrayBuilder.DefaultInitialSize)(using ClassTag[T]) extends TypedArrayBuilder[T] {
  //given clz:Class[T] = implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]]
  override inline def makeNArray(len: Int): NArray[T] = NArray.ofSize[T](len)

  override inline def make2DNArray(len: Int): NArray[NArray[T]] = {
    narr.native.makeNativeArrayOfSize[NativeArray[T]](len).asInstanceOf[NArray[NArray[T]]]
  }

  override inline def copyInto(src: NArray[T], dest: NArray[T], dstPos: Int): Unit = NArray.copyNativeArray(
    src.asInstanceOf[NativeArray[T]], dest.asInstanceOf[NativeArray[T]], dstPos
  )
  override inline def copyInto(src: NArray[T], srcPos: Int, dest: NArray[T], dstPos: Int, length: Int): Unit = {
    NArray.copyNativeArray(src.asInstanceOf[NativeArray[T]], srcPos, dest.asInstanceOf[NativeArray[T]], dstPos, length)
  }

}
