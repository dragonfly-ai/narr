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

import narr.*

case class NArrayOpsTest[T](a:NArray[T]) {
  // head
  // last
  //
}

class Ops extends munit.FunSuite:

   test(" NArray Ops ") {

     val N:Int = 11
     val end:Int = N - 1

     ////////////////
     // Value Types:
     ////////////////

     // Unit
     val uaTabulate: NArray[Unit] = NArray.tabulate[Unit](N)(_ => ())
     assertEquals(uaTabulate.head, uaTabulate(0))
     assertEquals(uaTabulate.last, uaTabulate(end))

     // Boolean
     val boolArrTabulate: NArray[Boolean] = NArray.tabulate[Boolean](N)((i: Int) => i % 2 == 0)
     assertEquals(boolArrTabulate.head, boolArrTabulate(0))
     assertEquals(boolArrTabulate.last, boolArrTabulate(end))

     // Byte
     val baTabulate:NArray[Byte] = NArray.tabulate[Byte](N)((i:Int) => i.toByte)
     assertEquals(baTabulate.head, baTabulate(0))
     assertEquals(baTabulate.last, baTabulate(end))

     // Short
     val saTabulate: NArray[Short] = NArray.tabulate[Short](N)((i: Int) => i.toShort)
     assertEquals(saTabulate.head, saTabulate(0))
     assertEquals(saTabulate.last, saTabulate(end))

     // Int
     val iaTabulate: NArray[Int] = NArray.tabulate[Int](N)((i: Int) => i)
     assertEquals(iaTabulate.head, iaTabulate(0))
     assertEquals(iaTabulate.last, iaTabulate(end))

     // Long
     val laTabulate: NArray[Long] = NArray.tabulate[Long](N)((i: Int) => i.toLong)
     assertEquals(laTabulate.head, laTabulate(0))
     assertEquals(laTabulate.last, laTabulate(end))

     // Float
     val faTabulate: NArray[Float] = NArray.tabulate[Float](N)((i: Int) => i.toFloat)
     assertEquals(faTabulate.head, faTabulate(0))
     assertEquals(faTabulate.last, faTabulate(end))

     // Double
     val daTabulate: NArray[Double] = NArray.tabulate[Double](N)((i: Int) => i.toDouble)
     assertEquals(daTabulate.head, daTabulate(0))
     assertEquals(daTabulate.last, daTabulate(end))

     // Char
     val caTabulate: NArray[Char] = NArray.tabulate[Char](N)((i: Int) => i.toChar)
     assertEquals(caTabulate.head, caTabulate(0))
     assertEquals(caTabulate.last, caTabulate(end))

     ////////////////////
     // Reference Types:
     ////////////////////

     // String
     val strArrTabulate: NArray[String] = NArray.tabulate[String](N)((i: Int) => i.toString)
     assertEquals(strArrTabulate.head, strArrTabulate(0))
     assertEquals(strArrTabulate.last, strArrTabulate(end))

     // AnyRef
     val anyRefArrTabulate: NArray[AnyRef] = NArray.tabulate[AnyRef](N)(_ => new AnyRef())
     assertEquals(anyRefArrTabulate.head, anyRefArrTabulate(0))
     assertEquals(anyRefArrTabulate.last, anyRefArrTabulate(end))

   }

end Ops