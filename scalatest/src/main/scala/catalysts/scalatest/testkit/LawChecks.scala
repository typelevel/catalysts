package catalysts
package scalatest
package testkit


import catalysts.lawkit.{LawChecks => BaseLawChecks}
import org.scalatest.FunSpecLike
import org.scalatest.prop.Checkers
import org.typelevel.discipline.Laws

trait Discipline extends Checkers {
  self: FunSpecLike =>

  def checkAll(name: String, ruleSet: Laws#RuleSet) = {
    for ((id, prop) ← ruleSet.all.properties)
      it(name + "." + id) {
        check(prop)
      }
  }
}

trait LawChecks extends BaseLawChecks with Discipline {
  self: FunSpecLike with TestKit=>

  def checkAllLaws(name: String, ruleSet: Laws#RuleSet): Structure =
    checkAll(name, ruleSet)
}
