package catalysts
package scalatest
package testkit

import catalysts.testkit.{TestKit => BaseTestKit}
import catalysts.lawkit.{LawKit => BaseLawKit}
import org.scalatest.Assertion

trait TestKit extends BaseLawKit with BaseTestKit {

  // Types related to test type
  type AssertResult = Assertion
  type ExceptionResult =  Unit
  type TestEqual[A] = Unit
  type TestNest = Unit
  type TestBlock = Unit
  type TestShow[A] = Unit

  // Types related to Laws
  type Structure = Unit

  // Types related to Property testing
  type ForAllResult = Assertion
}
