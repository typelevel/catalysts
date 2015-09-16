package catalysts
package testkit

trait TestNotifications extends TestInfo {
  def shouldNotify: Boolean = true

  def notifyTestLoading(): Unit
}
