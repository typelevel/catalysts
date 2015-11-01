package catalysts
package testkit

/**
 * 
 */
trait TestSuite extends TestNotifications with StdTest { self: TestSettings =>

  notifyTestLoading()
  val mod = testType * getTestMode  
}
