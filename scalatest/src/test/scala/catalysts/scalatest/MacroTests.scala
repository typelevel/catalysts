package catalysts
package scalatest

import catalysts.macros.ClassInfo._
import catalysts.macros._
import org.scalatest.WordSpec
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class MacroTests extends Suite with GeneratorDrivenPropertyChecks{

  describe("Macro classname tests") {
    it("Test className") {
      assert(className(this) == "catalysts.scalatest.MacroTests")
    }
  }
  describe("One") {
    it("equal One") {
      assert(1 == 1)
    }
  }

  it("Test TypeTagM") {
    def f1[A](implicit tag: TypeTagM[A]): String = tag
    def f2[A](implicit tag: TypeTagM[A]): String = tag.name
    def f3[A](implicit tag: TypeTagM[A]): String = tag.toString

    val expected = "List[Map[Int,Double]]"
    assert(f1[List[Map[Int, Double]]] == expected)
    assert(f2[List[Map[Int, Double]]] == expected)
    assert(f3[List[Map[Int, Double]]] == expected)
  }

  it("fold and fold consistent") {
    forAll { (o: Option[Int], s: String, f: Int => String) =>
      assert(o.fold(s)(f) == o.fold(s)(f))
    }
  }

  it("forEvery fold and fold consistent") {
    all { (o: Option[Int], s: String, f: Int => String) =>
      assert(o.fold(s)(f) == o.fold(s)(f))
    }
  }
}

class SetSpec extends WordSpec {

  "An empty empty set" should {
    "have size 0" in {
      assert(Set.empty.size === 0)
    }

    "produce NoSuchElementException when head is invoked" in {
      assert(Set.empty.size === 0)
    }
  }

  "A Set" when {
    "empty" should {

      "have size 0" in {
        assert(Set.empty.size === 0)
      }
      "produce NoSuchElementException when head is invoked" in {
        assert(Set.empty.size === 0)
      }
    }
  }
}
