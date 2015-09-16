package bricks
package scalatest

import bricks.testkit._
import org.scalatest.prop.Configuration.PropertyCheckConfiguration
import org.typelevel.discipline.{Laws => DisciplineLaws}

trait ScalatestKit extends TestKit {

  type Params = PropertyCheckConfiguration
  type Laws = DisciplineLaws
  type RuleSet = DisciplineLaws#RuleSet
  type Structure = Unit
}
