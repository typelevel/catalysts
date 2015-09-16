package bricks
package specs2

import bricks.testkit.{LawChecks => BaseLawChecks}
import org.specs2.SpecificationLike
import org.typelevel.discipline.specs2.Discipline

trait LawsChecks  extends BaseLawChecks[Specs2Kit] with Discipline with Specs2Tests {
  self: SpecificationLike =>

  def checkAllLaws(name: String, ruleSet: Specs2Kit#RuleSet): Specs2Kit#Structure = {
    checkAll(name, ruleSet)
  }
}
