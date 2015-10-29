package catalysts
package lawkit

import org.scalacheck.Arbitrary
import catalysts.macros._

trait LawTests { self: LawSpec with LawChecks =>
 
  implicit def groupLaws[A: Arbitrary] = new GroupLaws[A]{}

  laws[GroupLaws, Int].check(_.group)
  laws[GroupLaws, List[Int]].check(_.additiveGroup)
}
