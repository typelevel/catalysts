package catalysts
package scalatest

import catalysts.testkit.{LawChecks => BaseLawChecks}
import org.scalatest.FunSuiteLike
import org.typelevel.discipline.scalatest.Discipline
import org.typelevel.discipline.Laws

trait LawsChecks extends BaseLawChecks[ScalatestKit] with Discipline with ScalaTests {
  self: FunSuiteLike =>

  def checkAllLaws(name: String, ruleSet: Laws#RuleSet): ScalatestKit#Structure =
    checkAll(name, ruleSet)
}
