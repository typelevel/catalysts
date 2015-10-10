package catalysts
package testkit


import syntax.{FSpec, FSpecMatchers}

trait  FSpecTests[Tk <: TestKit]  extends TestSpec[Tk] with FSpec[Tk] with FSpecMatchers[Tk] {
  self: TestSuite =>
 
  describe("One") {
    it("equal One") {
      assertEquals(1, 1)
    }
  }
}
