package catalysts
package checklite
package testkit

import catalysts.testkit.{TestSettings, TestSuite => BaseTestSuite}

/**
 *
 */
trait Test extends BaseTestSuite with TestSettings   {

  def notifyTestLoading(): Unit = {
    import scala.Console.{GREEN, RESET}

    if (shouldNotify)
      println(s"${RESET}[info] ${GREEN} ${testName} loading...${RESET}") // scalastyle:ignore
  }
}

/**
 * An opinionated stack of traits to improve consistency and reduce
 * boilerplate in tests.
 */
trait TestSuite extends CheckLite with Test with LawChecks with TestSpec

trait TestProps extends Test
