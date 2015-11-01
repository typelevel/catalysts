package catalysts
package speclite
package testkit

import catalysts.testkit.{TestKit => BaseTestKit}

trait TestKit extends BaseTestKit {

  // Types related to test type
  type AssertResult = Unit
  type ExceptionResult =  Unit
  type TestEqual[A] = Unit
  type TestNest = Unit
  type TestBlock = Unit
  type TestShow[A] = Unit
}
