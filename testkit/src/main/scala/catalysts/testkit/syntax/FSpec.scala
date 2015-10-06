package catalysts
package testkit
package syntax

trait FSpec[Tk <: TestKit] {self : TestSpec[Tk] => 
  def it(s: String)(a: => Any) = block(s)(a)
  def describe(s: String)(a: => Any) = nest(s)(a)
 // def assertEquals[A](actual: => A, expected: => A) = assertEq(actual, expected)
}


trait FSpecMatchers [Tk <: TestKit] extends FSuiteMatchers[Tk] { self : TestSpec[Tk] =>
}
