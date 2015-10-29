package catalysts
package testkit

import catalysts.testkit.syntax.junit.{Assert,Test}
import catalysts.testkit.syntax.{FSuiteMatchers, FSuite}

trait JSpec extends FSuiteMatchers with FSuite with TestSpec with TestKit

trait JUnitTests extends JSpec {

   @Test
   def equalOne() = {
      assertEquals(1, 1)

      assertEquals("1 == 1", 1, 1)
      assertTrue(true)
      assertFalse(false)
      assertTrue()
      assertEquals(1.1, 1.2, 0.5)


      val assertFalseTest =
        try{
          assertFalse()
          false
        } catch {
          case ex: Throwable => true
        }
      assert(assertFalseTest)
    }

  @Test
  def typeEq() = {
    assertTypedEquals(1, 1)
    assertTypedSame(List(), List())
    assertEquals(0, Assert.assertArrayEquals)
  }
}
