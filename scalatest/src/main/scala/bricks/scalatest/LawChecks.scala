package bricks
package scalatest

import bricks.testkit.{LawChecks => BaseLawChecks}
import org.scalatest.FunSuiteLike
import org.typelevel.discipline.scalatest.Discipline

trait LawsChecks extends BaseLawChecks[ScalatestKit] with Discipline with ScalaTests {
  self: FunSuiteLike =>

  def checkAllLaws(name: String, ruleSet: ScalatestKit#RuleSet): ScalatestKit#Structure =
    checkAll(name, ruleSet)
}
