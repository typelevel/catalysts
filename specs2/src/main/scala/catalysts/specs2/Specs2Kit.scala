package catalysts
package specs2

import catalysts.testkit._
import org.specs2.execute.Result
import org.specs2.scalacheck.Parameters
import org.specs2.specification.core.{Fragment, Fragments}

trait Specs2Kit extends TestKit {
  
  type AssertResult = Result
  type ExceptionResult =  Result
  type TestEqual[A] = Unit
  type TestNest = Fragment
  type TestBlock = Fragment
  type TestShow[A] = Unit

  type Params = Parameters
  type Structure = Fragments
}
