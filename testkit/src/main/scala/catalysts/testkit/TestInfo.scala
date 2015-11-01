package catalysts
package testkit

import catalysts.macros.ClassInfo._

trait TestInfo {
  def testName:String = className(this).split('.').last

  def projectKey:String = className(this).split('.').head
}
