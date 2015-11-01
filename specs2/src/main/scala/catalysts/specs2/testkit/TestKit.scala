package catalysts
package specs2
package testkit

import catalysts.testkit.{TestKit => BaseTestKit}
import catalysts.lawkit.{LawKit => BaseLawKit}
import org.specs2.execute.Result
import org.specs2.specification.core.{Fragment, Fragments}

trait TestKit extends BaseLawKit with BaseTestKit {

  // Types related to test type
  type AssertResult = Result
  type ExceptionResult =  Result
  type TestEqual[A] = Unit
  type TestNest = Fragment
  type TestBlock = Fragment
  type TestShow[A] = Unit

  // Types related to Laws
  type Structure = Fragments

  // Types related to Property testing
  type ForAllResult = Result
}
