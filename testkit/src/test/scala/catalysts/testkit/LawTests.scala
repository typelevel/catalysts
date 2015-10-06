package catalysts
package testkit

import org.scalacheck.Arbitrary
import catalysts.macros._

trait LawTests[Tk <: TestKit] extends TestSpec[Tk] { self: TestSuite with LawChecks[Tk] =>
 
  implicit def groupLaws[A: Arbitrary] = new GroupLaws[A]{}

  laws[GroupLaws, Int].check(_.group)
  laws[GroupLaws, List[Int]].check(_.additiveGroup)
}

