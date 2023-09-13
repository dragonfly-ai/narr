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

class Instantiate extends munit.FunSuite:

   test(" NArray constructors and factories ") {

     var N:Int = 11

     ////////////////
     // Value Types:
     ////////////////

     // Unit
     val uaNew: NArray[Unit] = new NArray[Unit](N)
     val uaOfSize: NArray[Unit] = NArray.ofSize[Unit](N)
     val uaFill: NArray[Unit] = NArray.fill[Unit](N)(())
     val uaTabulate: NArray[Unit] = NArray.tabulate[Unit](N)(_ => ())
     assertEquals(uaNew.length, uaOfSize.length)
     assertEquals(uaOfSize.length, uaFill.length)
     assertEquals(uaFill.length, uaTabulate.length)

     N += 1
     // Boolean
     val boolArrNew: NArray[Boolean] = new NArray[Boolean](N)
     val boolArrOfSize: NArray[Boolean] = NArray.ofSize[Boolean](N)
     val boolArrFill: NArray[Boolean] = NArray.fill[Boolean](N)(false)
     val boolArrTabulate: NArray[Boolean] = NArray.tabulate[Boolean](N)((i: Int) => i % 2 == 0)
     assertEquals(boolArrNew.length, boolArrOfSize.length)
     assertEquals(boolArrOfSize.length, boolArrFill.length)
     assertEquals(boolArrFill.length, boolArrTabulate.length)

     N += 1
     // Byte
     val baNew:NArray[Byte] = new NArray[Byte](N)
     val baOfSize:NArray[Byte] = NArray.ofSize[Byte](N)
     val baFill:NArray[Byte] = NArray.fill[Byte](N)(1)
     val baTabulate:NArray[Byte] = NArray.tabulate[Byte](N)((i:Int) => i.toByte)
     assertEquals(baNew.length, baOfSize.length)
     assertEquals(baOfSize.length, baFill.length)
     assertEquals(baFill.length, baTabulate.length)

     N += 1
     // Short
     val saNew: NArray[Short] = new NArray[Short](N)
     val saOfSize: NArray[Short] = NArray.ofSize[Short](N)
     val saFill: NArray[Short] = NArray.fill[Short](N)(1)
     val saTabulate: NArray[Short] = NArray.tabulate[Short](N)((i: Int) => i.toShort)
     assertEquals(saNew.length, saOfSize.length)
     assertEquals(saOfSize.length, saFill.length)
     assertEquals(saFill.length, saTabulate.length)

     N += 1
     // Int
     val iaNew: NArray[Int] = new NArray[Int](N)
     val iaOfSize: NArray[Int] = NArray.ofSize[Int](N)
     val iaFill: NArray[Int] = NArray.fill[Int](N)(1)
     val iaTabulate: NArray[Int] = NArray.tabulate[Int](N)((i: Int) => i)
     assertEquals(iaNew.length, iaOfSize.length)
     assertEquals(iaOfSize.length, iaFill.length)
     assertEquals(iaFill.length, iaTabulate.length)

     N += 1
     // Long
     val laNew: NArray[Long] = new NArray[Long](N)
     val laOfSize: NArray[Long] = NArray.ofSize[Long](N)
     val laFill: NArray[Long] = NArray.fill[Long](N)(1L)
     val laTabulate: NArray[Long] = NArray.tabulate[Long](N)((i: Int) => i.toLong)
     assertEquals(laNew.length, laOfSize.length)
     assertEquals(laOfSize.length, laFill.length)
     assertEquals(laFill.length, laTabulate.length)

     N += 1
     // Float
     val faNew: NArray[Float] = new NArray[Float](N)
     val faOfSize: NArray[Float] = NArray.ofSize[Float](N)
     val faFill: NArray[Float] = NArray.fill[Float](N)(1L)
     val faTabulate: NArray[Float] = NArray.tabulate[Float](N)((i: Int) => i.toFloat)
     assertEquals(faNew.length, faOfSize.length)
     assertEquals(faOfSize.length, faFill.length)
     assertEquals(faFill.length, faTabulate.length)

     N += 1
     // Double
     val daNew: NArray[Double] = new NArray[Double](N)
     val daOfSize: NArray[Double] = NArray.ofSize[Double](N)
     val daFill: NArray[Double] = NArray.fill[Double](N)(1L)
     val daTabulate: NArray[Double] = NArray.tabulate[Double](N)((i: Int) => i.toDouble)
     assertEquals(daNew.length, daOfSize.length)
     assertEquals(daOfSize.length, daFill.length)
     assertEquals(daFill.length, daTabulate.length)

     N += 1
     // Char
     val caNew: NArray[Char] = new NArray[Char](N)
     val caOfSize: NArray[Char] = NArray.ofSize[Char](N)
     val caFill: NArray[Char] = NArray.fill[Char](N)('a')
     val caTabulate: NArray[Char] = NArray.tabulate[Char](N)((i: Int) => i.toChar)
     assertEquals(caNew.length, caOfSize.length)
     assertEquals(caOfSize.length, caFill.length)
     assertEquals(caFill.length, caTabulate.length)


     ////////////////////
     // Reference Types:
     ////////////////////

     N += 1
     // String
     val strArrNew: NArray[String] = new NArray[String](N)
     val strArrOfSize: NArray[String] = NArray.ofSize[String](N)
     val strArrFill: NArray[String] = NArray.fill[String](N)("Asdf.")
     val strArrTabulate: NArray[String] = NArray.tabulate[String](N)((i: Int) => i.toString)
     assertEquals(strArrNew.length, strArrOfSize.length)
     assertEquals(strArrOfSize.length, strArrFill.length)
     assertEquals(strArrFill.length, strArrTabulate.length)

     N += 1
     // AnyRef
     val anyRefArrNew: NArray[AnyRef] = new NArray[AnyRef](N)
     val anyRefArrOfSize: NArray[AnyRef] = NArray.ofSize[AnyRef](N)
     val anyRefArrFill: NArray[AnyRef] = NArray.fill[AnyRef](N)(new AnyRef())
     val anyRefArrTabulate: NArray[AnyRef] = NArray.tabulate[AnyRef](N)(_ => new AnyRef())
     assertEquals(anyRefArrNew.length, anyRefArrOfSize.length)
     assertEquals(anyRefArrOfSize.length, anyRefArrFill.length)
     assertEquals(anyRefArrFill.length, anyRefArrTabulate.length)


   }

end Instantiate