package catalysts
package tests

import org.scalacheck.Arbitrary
import catalysts.scalatest.TestSuite
import catalysts.testkit.LawChecks._

class LawTests extends TestSuite {
  implicit def groupLaws[A: Arbitrary] = GroupLaws[A]

  laws[GroupLaws, Int].check(_.group)

  laws[GroupLaws, List[Int]].check(_.additiveGroup)
}
