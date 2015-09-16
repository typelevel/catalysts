package catalysts
package specs2

import org.specs2.{ScalaCheck, Specification}
import org.specs2.scalacheck.Parameters
import testkit.{TestModifier, TestSettings, TestSuite => BaseTestSuite}

/**
 *
 */
trait Specs2Tests extends BaseTestSuite with TestSettings with ScalaCheck  {

  def checkConfiguration(mode:TestModifier): Parameters = {
    val (min,max) = checkSettings(mode)
    Parameters(minTestsOk = min, maxDiscardRatio = max.toFloat)
  }

  def notifyTestLoading(): Unit = {
    if (shouldNotify)
      println(s"[info] ${testName} loading...") // scalastyle:ignore
  }

  implicit val params = checkConfiguration(mod)
}

/**
 * An opinionated stack of traits to improve consistency and reduce
 * boilerplate in tests.
 */
trait TestSuite extends Specification with LawsChecks

trait TestProps extends Specification with ScalaCheck with Specs2Tests
