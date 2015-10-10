package catalysts
package testkit


import syntax.{FSuite, FSuiteMatchers}
import java.util.Arrays

trait  FSuiteTests[Tk <: TestKit]  extends TestSpec[Tk] with FSuite[Tk] with FSuiteMatchers[Tk] {
  self: TestSuite =>

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
