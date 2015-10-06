package catalysts
package testkit


import syntax.{FSuite, FSuiteMatchers}

trait  FSuiteTests[Tk <: TestKit]  extends TestSpec[Tk] with FSuite[Tk] with FSuiteMatchers[Tk] {
  self: TestSuite =>
 
  suite("One") {
    test("equal One") {
      assertEquals(1, 1)
    }
  }
}
