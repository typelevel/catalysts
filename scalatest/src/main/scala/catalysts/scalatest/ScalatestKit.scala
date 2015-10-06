package catalysts
package scalatest

import catalysts.testkit._
import org.scalatest.prop.Configuration.PropertyCheckConfiguration

trait ScalatestKit extends TestKit {
  
  type AssertResult = Unit
  type ExceptionResult =  Unit
  type TestEqual[A] = Unit
  type TestNest = Unit
  type TestBlock = Unit
  type TestShow[A] = Unit

  type Params = PropertyCheckConfiguration
  type Structure = Unit
}
