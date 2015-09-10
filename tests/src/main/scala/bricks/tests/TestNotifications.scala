package bricks
package tests

trait TestNotifications extends TestInfo {
  def shouldNotify: Boolean = true

  def notifyTestLoading(): Unit = {
    import scala.Console.GREEN

    if (shouldNotify)
      println(s"[info]${GREEN} ${testName} loading...")
  }
}
