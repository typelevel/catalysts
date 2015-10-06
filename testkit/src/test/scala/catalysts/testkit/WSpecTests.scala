package catalysts
package testkit


import syntax.{WSpec, WSpecMatchers}

trait  WSpecTests[Tk <: TestKit]  extends TestSpec[Tk] with WSpec[Tk] with WSpecMatchers[Tk] {
  self: TestSuite =>
 
  "One" should {
    "equal One" in {
       1 must_== 1
    }
  }
}
