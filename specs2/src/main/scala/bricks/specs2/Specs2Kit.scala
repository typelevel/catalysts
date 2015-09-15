package bricks
package specs2

import bricks.testkit._
import org.specs2.scalacheck.Parameters
import org.typelevel.discipline.{Laws => DisciplineLaws}

trait Specs2Kit extends TestKit {

  type Params = Parameters
  type Laws = DisciplineLaws
  type RuleSet = DisciplineLaws#RuleSet
  type Structure = org.specs2.specification.core.SpecStructure
}
