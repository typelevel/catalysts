package catalysts
package specbase

import catalysts.testkit.{TestKit, TestSpec}

trait  SpecBaseTests[P,PS] extends SpecBase[P,PS] {
  self: TestSpec with TestKit =>

  check(1 == 1)

  1 mustBe_< 2

  (2 mustBe_< 2).mustThrowA[AssertionError]

  val fTrue: PartialFunction[Boolean, Boolean] = { case b => b == true }
  val fFalse: PartialFunction[Int, Boolean] = { case x => false }

  true mustMatch fTrue and !false mustMatch fTrue

  (false mustMatch(fTrue)).mustThrowA[AssertionError]

  this mustBeTheSameAs this

  (List(false) mustBeTheSameAs List(true)).mustThrowA[AssertionError]
}