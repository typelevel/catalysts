package catalysts
package checklite
package testkit


import catalysts.testkit.{TestSettings, TestSuite => BaseTestSuite}

/**
 *
 */
trait Test extends BaseTestSuite with TestSettings   {

/*
def checkConfiguration(mode:TestModifier): Parameters = {
    val (min,max) = checkSettings(mode)
    Parameters(minTestsOk = min, maxDiscardRatio = max.toFloat)
  }
 */
  def notifyTestLoading(): Unit = {
    import scala.Console.{GREEN, RESET}

    if (shouldNotify)
      println(s"${RESET}[info] ${GREEN} ${testName} loading...${RESET}") // scalastyle:ignore
  }

 // implicit val params = checkConfiguration(mod)
 
}

/**
 * An opinionated stack of traits to improve consistency and reduce
 * boilerplate in tests.
 */
trait TestSuite extends Test

trait TestProps extends Test
