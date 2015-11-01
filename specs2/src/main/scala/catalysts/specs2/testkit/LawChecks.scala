package catalysts
package specs2
package testkit

import catalysts.lawkit.{LawChecks => BaseLawChecks}
import org.specs2.mutable.SpecificationLike
import org.typelevel.discipline.specs2.mutable.Discipline
import org.typelevel.discipline.Laws

trait LawChecks  extends BaseLawChecks with Discipline {
  self: SpecificationLike with TestKit =>

  def checkAllLaws(name: String, ruleSet: Laws#RuleSet): Structure =
    checkAll(name, ruleSet)
}
