package catalysts
package specs2

import catalysts.macros.ClassInfo._
import catalysts.macros._

class MacroTests extends TestSuite {

  "Test className" >> {
    className(this) must_== "catalysts.specs2.MacroTests"
  }

  "Test TypeTagM" >> {
    def f1[A](implicit tag: TypeTagM[A]): String = tag
    def f2[A](implicit tag: TypeTagM[A]): String = tag.name
    def f3[A](implicit tag: TypeTagM[A]): String = tag.toString

    val expected = "List[Map[Int,Double]]"
    f1[List[Map[Int, Double]]] must_== expected
    f2[List[Map[Int, Double]]] must_== expected
    f3[List[Map[Int, Double]]] must_== expected
  }
}
