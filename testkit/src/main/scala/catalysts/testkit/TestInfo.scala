package catalysts
package testkit

import catalysts.macros.ClassInfo._

trait TestInfo {
  def testName = className(this).split('.').last

  def projectKey = "catalysts"
}
