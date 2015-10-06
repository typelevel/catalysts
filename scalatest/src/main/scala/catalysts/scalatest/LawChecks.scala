package catalysts
package scalatest

import catalysts.testkit.{LawChecks => BaseLawChecks}
import org.typelevel.discipline.Laws


import org.scalacheck.Properties

import org.scalatest.FunSpecLike
import org.scalatest.prop.Checkers

trait Discipline extends Checkers { self: FunSpecLike =>

  def checkAll(name: String, ruleSet: Laws#RuleSet) = {
    for ((id, prop) ← ruleSet.all.properties)
      it(name + "." + id) {
        check(prop)
      }
  }

}

trait LawsChecks extends BaseLawChecks[ScalatestKit] with Discipline with ScalaTests {
  self: FunSpecLike =>

  def checkAllLaws(name: String, ruleSet: Laws#RuleSet): ScalatestKit#Structure =
    checkAll(name, ruleSet)
}
