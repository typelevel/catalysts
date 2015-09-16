package catalysts
package specs2

import org.typelevel.discipline.{Laws, Predicate}
import catalysts.macros._
import catalysts.testkit.{StdTest, TestModifier, TestNotifications, TestSettings, WellTested}
import org.scalacheck.Gen.Parameters
import org.scalacheck.{Arbitrary, Prop}
import catalysts.tests._

class LawTests extends TestSuite {
  implicit def groupLaws[A: Arbitrary] = GroupLaws[A]

  def is = laws[GroupLaws, Int].check(_.group)
}

class ListLawTests extends TestSuite {
  override def shouldNotify = false
  implicit def groupLaws[A: Arbitrary] = GroupLaws[A]

  def is = laws[GroupLaws, List[Int]].check(_.additiveGroup)
}
