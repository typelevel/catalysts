package catalysts
package specs2

import catalysts.testkit.{LawChecks => BaseLawChecks}
import org.specs2.mutable.SpecificationLike
import org.typelevel.discipline.specs2.mutable.Discipline
import org.typelevel.discipline.Laws

trait LawsChecks  extends BaseLawChecks[Specs2Kit] with Discipline with Specs2Tests {
  self: SpecificationLike =>

  def checkAllLaws(name: String, ruleSet: Laws#RuleSet): Specs2Kit#Structure = 
    checkAll(name, ruleSet)
}
