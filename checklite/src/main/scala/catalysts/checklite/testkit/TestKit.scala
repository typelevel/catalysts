package catalysts
package checklite
package testkit

import catalysts.testkit.{TestKit => BaseTestKit}

trait TestKit extends BaseTestKit {
  
  type AssertResult = Unit
  type ExceptionResult =  Unit
  type TestEqual[A] = Unit
  type TestNest = Unit
  type TestBlock = Unit
  type TestShow[A] = Unit

 // type Params = PropertyCheckConfiguration
  type Structure = Unit
}
