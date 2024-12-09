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

import scala.language.implicitConversions

import Util.*

class CopyTest extends munit.FunSuite {

  test(" NArray copy ") {

    var N: Int = 11

    ////////////////
    // Value Types:
    ////////////////

    // Unit
    val uaTabulate: NArray[Unit] = NArray.tabulate[Unit](N)(_ => ())
    assertNArrayEquality[Unit](uaTabulate, uaTabulate.copy)
    assertNArrayEquality[Unit](uaTabulate, copy(uaTabulate))

    N += 1
    // Boolean
    val boolArrTabulate: NArray[Boolean] = NArray.tabulate[Boolean](N)((i: Int) => i % 2 == 0)
    assertNArrayEquality[Boolean](boolArrTabulate, boolArrTabulate.copy)
    assertNArrayEquality[Boolean](boolArrTabulate, copy(boolArrTabulate))


    N += 1
    // Byte
    val baTabulate: NArray[Byte] = NArray.tabulate[Byte](N)((i: Int) => i.toByte)
    assertNArrayEquality[Byte](baTabulate, baTabulate.copy)
    assertNArrayEquality[Byte](baTabulate, copy(baTabulate))

    N += 1
    // Short
    val saTabulate: NArray[Short] = NArray.tabulate[Short](N)((i: Int) => i.toShort)
    assertNArrayEquality[Short](saTabulate, saTabulate.copy)
    assertNArrayEquality[Short](saTabulate, copy(saTabulate))

    N += 1
    // Int
    val iaTabulate: NArray[Int] = NArray.tabulate[Int](N)((i: Int) => i)
    assertNArrayEquality[Int](iaTabulate, iaTabulate.copy)
    assertNArrayEquality[Int](iaTabulate, copy(iaTabulate))

    N += 1
    // Long
    val laTabulate: NArray[Long] = NArray.tabulate[Long](N)((i: Int) => i.toLong)
    assertNArrayEquality[Long](laTabulate, laTabulate.copy)
    assertNArrayEquality[Long](laTabulate, copy(laTabulate))


    N += 1
    // Float
    val faTabulate: NArray[Float] = NArray.tabulate[Float](N)((i: Int) => i.toFloat)
    assertNArrayEquality[Float](faTabulate, faTabulate.copy)
    assertNArrayEquality[Float](faTabulate, copy(faTabulate))

    N += 1
    // Double
    val daTabulate: NArray[Double] = NArray.tabulate[Double](N)((i: Int) => i.toDouble)
    assertNArrayEquality[Double](daTabulate, copy(daTabulate))

    N += 1
    // Char
    val caTabulate: NArray[Char] = NArray.tabulate[Char](N)((i: Int) => i.toChar)
    assertNArrayEquality[Char](caTabulate, copy(caTabulate))

    ////////////////////
    // Reference Types:
    ////////////////////

    N += 1
    // String
    val strArrTabulate: NArray[String] = NArray.tabulate[String](N)((i: Int) => i.toString)
    assertNArrayEquality[String](strArrTabulate, strArrTabulate.copy)
    assertNArrayEquality[String](strArrTabulate, copy(strArrTabulate))

    N += 1
    // AnyRef
    val anyRefArrTabulate: NArray[AnyRef] = NArray.tabulate[AnyRef](N)(_ => new AnyRef())
    assertNArrayEquality[AnyRef](anyRefArrTabulate, anyRefArrTabulate.copy)
    assertNArrayEquality[AnyRef](anyRefArrTabulate, copy(anyRefArrTabulate))

  }
}