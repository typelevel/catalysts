package catalysts
package checklite

import catalysts.specbase.SpecBaseTests
import catalysts.testkit._
import org.scalacheck.{Prop, Properties}

class CheckliteSpecBaseTests extends Suite with SpecBaseTests[Prop, Properties]

import catalysts.lawkit.{LawSpecTests, LawTests}

class CheckliteLawTests extends Suite with LawTests
class CheckliteLawSpecTests extends Suite with LawSpecTests
class CheckliteFSpecTests extends Suite with FSpecTests
class CheckliteFSuiteTests extends Suite with FSuiteTests
class CheckliteJUnitTests extends Suite with JUnitTests
class CheckliteTestSpecTests extends Suite with TestSpecTests
class ChecklitetWSpecTests extends Suite with WSpecTests

object MyProps extends Properties("MyProps") {
  property("myProp1") = Prop.forAll { (l1: List[Int], l2: List[Int]) =>
    l1.size + l2.size == (l1 ::: l2).size
  }
}

class CheckliteCoreTests extends Suite {

  //testing error condition
  val i = new InStringOps(null).apply(null)

  val p = Prop.forAll { (l1: List[Int], l2: List[Int]) =>
    l1.size + l2.size == (l1 ::: l2).size }

  val p2 = MyProps withProp ("myProp2", p)

  checkAll(MyProps)
  checkAll("myProp3", p2)
}
