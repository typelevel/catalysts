package catalysts
package testkit

import syntax.{FSpec, FSpecMatchers}

trait FSpecTests extends FSpec with FSpecMatchers { self: TestSpec with TestKit =>

  describe("One") {
    it("equal One") {
      assertEquals(1, 1)
    }
  }
}
