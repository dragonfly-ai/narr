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

package narr

import scala.collection.ArrayOps
import scala.reflect.ClassTag
import scala.language.implicitConversions
import scala.util.Sorting.*
import java.util

package object native {

  type ByteArray = Array[Byte]
  type ShortArray = Array[Short]
  type IntArray = Array[Int]
  type FloatArray = Array[Float]
  type DoubleArray = Array[Double]
  type NativeArray[T] = Array[T]

  type NArray[T] = Array[T]

  type NArr[T] = Array[T]
  type SortableNArr[T] = Array[T]

  inline def makeNativeArrayOfSize[A:ClassTag](n:Int):NativeArray[A] = new Array[A](n)

  object NArray {
    export Array.*
  }

  object Extensions {

    export collection.ArrayOps

    extension (ua: NArray[Unit]) {
      inline def sort(): NArray[Unit] = ua
      inline def sort(ot: Ordering[Unit]): NArray[Unit] = ua
    }

    extension (ba: NArray[Boolean]) {
      inline def sort(): NArray[Boolean] = sort(Ordering.Boolean)
      inline def sort(ot: Ordering[Boolean]): NArray[Boolean] = {
        quickSort[Boolean](ba)(ot); ba
      }
    }

    extension (ba: ByteArray) {
      inline def sort(): ByteArray = { util.Arrays.sort(ba); ba}
      inline def sort(ot: Ordering[Byte]): ByteArray = { quickSort[Byte](ba)(ot); ba}
    }
    extension (sa: ShortArray) {
      inline def sort(): ShortArray = { util.Arrays.sort(sa); sa }
      inline def sort(ot: Ordering[Short]): ShortArray = { quickSort[Short](sa)(ot); sa}
    }

    extension (ia: IntArray) {
      inline def sort(): IntArray = { util.Arrays.sort(ia); ia }
      inline def sort(ot: Ordering[Int]): IntArray = { quickSort[Int](ia)(ot); ia }
    }

    extension (la: NArray[Long]) {
      inline def sort(): NArray[Long] = { util.Arrays.sort(la); la }
      inline def sort(ot: Ordering[Long]): NArray[Long] = { quickSort[Long](la)(ot); la}
    }

    extension (fa: FloatArray) {
      inline def sort(): FloatArray = { util.Arrays.sort(fa); fa }
      inline def sort(ot: Ordering[Float]): FloatArray = { quickSort[Float](fa)(ot); fa }
    }

    extension (da: DoubleArray) {
      inline def sort(): DoubleArray = { util.Arrays.sort(da); da }
      inline def sort(ot: Ordering[Double]): DoubleArray = { quickSort[Double](da)(ot); da }
    }

    extension (ca: NArray[Char]) {
      inline def sort(): NArray[Char] = { util.Arrays.sort(ca); ca }
      inline def sort(ot: Ordering[Char]): NArray[Char] = { quickSort[Char](ca)(ot); ca }
    }

    extension (a: NArray[String]) {
      inline def sort(): NArray[String] = { quickSort[String](a)(Ordering.String); a }
      inline def sort(ot: Ordering[String]): NArray[String] = { quickSort[String](a)(ot); a }
    }

    extension[T <: AnyRef] (a:NArray[T]) {
      inline def sort(ot:Ordering[T]): NArray[T] = { quickSort[T](a)(ot); a }
    }

    extension[T](a:NArray[T]) {
      def copy: NArray[T] = (a match {
        case nArr: Array[Boolean] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Byte] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Short] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Int] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Long] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Float] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Double] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[Char] => util.Arrays.copyOf(nArr, nArr.length)
        case nArr: Array[String] => util.Arrays.copyOf(nArr, nArr.length)
        case _ => util.Arrays.copyOf[T](a.asInstanceOf[Array[AnyRef & T]], a.length)
      }).asInstanceOf[NArr[T] & NArray[T]]
    }
  }
}
