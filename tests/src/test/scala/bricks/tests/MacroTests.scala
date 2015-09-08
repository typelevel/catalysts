package bricks
package tests

import bricks.macros.ClassMacros._
import bricks.macros._

class MacroTests extends TestSuite {

  test("Test className") {
    assert(className(this) == "bricks.tests.MacroTests")
  }

  test("Test TypeTagM") {
    def f[A](implicit tag: TypeTagM[A]): String = tag.tpe.toString

    assert(f[List[Map[Int, Double]]] == "List[Map[Int,Double]]")
  }
}
