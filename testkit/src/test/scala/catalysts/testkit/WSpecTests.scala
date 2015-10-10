package catalysts
package testkit


import syntax.{WSpec, WSpecMatchers}

trait  WSpecTests[Tk <: TestKit]  extends TestSpec[Tk] with WSpec[Tk] with WSpecMatchers[Tk] {
  self: TestSuite =>

   def err(i: Int) = {
    if (i < 0) throw new Error else i
  }

  "One" should {
    "equal One" in {
       1 must_== 1
       err(-1).mustThrowA[Error]
    }
  }
}
