package catalysts
package testkit
package syntax

trait FSpec { self : TestSpec =>

  def it(s: String)(a: => Any) = block(s)(a)

  def describe(s: String)(a: => Any) = nest(s)(a)
}

trait FSpecMatchers extends FSuiteMatchers {
  self: TestKit =>
}