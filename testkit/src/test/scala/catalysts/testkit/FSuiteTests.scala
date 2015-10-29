package catalysts
package testkit

import syntax.{FSuiteMatchers, FSuite}

trait  FSuiteTests extends FSuiteMatchers with FSuite { self: TestSpec with TestKit =>

  suite("One") {
    test("equal One") {
      assertEquals(1, 1)

      assertEquals("1 == 1", 1, 1)
      assertTrue(true)
      assertFalse(false)
      assertTrue()
      assertEquals(1.1, 1.2, 0.5)
      assertTypedEquals(1, 1)
      assertTypedSame(List(), List())

      val assertFalseTest =
        try{
          assertFalse()
          false
        } catch {
          case ex: Throwable => true
        }
      assert(assertFalseTest)
    }
  }
}
