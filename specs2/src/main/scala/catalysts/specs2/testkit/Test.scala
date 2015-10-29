package catalysts
package specs2
package testkit

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

import org.specs2.scalacheck.Parameters
import catalysts.testkit.{TestModifier, TestSettings, TestSuite => BaseTestSuite}

/**
 *
 */
trait Test extends BaseTestSuite with TestSettings with ScalaCheck  {

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
trait TestSuite extends Specification with LawChecks with Test with TestKit

trait TestProps extends Test with ScalaCheck
