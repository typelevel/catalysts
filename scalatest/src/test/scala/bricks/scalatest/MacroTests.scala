package bricks
package scalatest

import bricks.macros.ClassInfo._
import bricks.macros._

class MacroTests extends TestSuite {

  test("Test className") {
    assert(className(this) == "bricks.scalatest.MacroTests")
  }

  test("Test TypeTagM") {
    def f1[A](implicit tag: TypeTagM[A]): String = tag
    def f2[A](implicit tag: TypeTagM[A]): String = tag.name
    def f3[A](implicit tag: TypeTagM[A]): String = tag.toString

    val expected = "List[Map[Int,Double]]"
    assert(f1[List[Map[Int, Double]]] == expected)
    assert(f2[List[Map[Int, Double]]] == expected)
    assert(f3[List[Map[Int, Double]]] == expected)
  }
}
