package catalysts
package scalatest
package testkit

import org.scalatest.{FunSpec, Matchers, PropSpec}
import org.scalatest.prop.{Configuration, PropertyChecks}
import org.scalactic.anyvals.{PosZDouble, PosInt}
import catalysts.testkit.{TestModifier, TestSettings, TestSuite => BaseTestSuite}

trait Test extends BaseTestSuite
    with Configuration
    with Matchers
    with TestSettings
    with TestInstances {

  def checkConfiguration(mode: TestModifier): PropertyCheckConfiguration = {
    val (min, max) = checkSettings(mode)
    PropertyCheckConfiguration(PosInt.from(min).get, PosZDouble.from(max).get)
  }

  def notifyTestLoading(): Unit = {

    import scala.Console.GREEN

    if (shouldNotify)
      println(s"[info]${GREEN} ${testName} loading...")
  }

  implicit override val generatorDrivenConfig = checkConfiguration(mod)
}

/**
 * An opinionated stack of traits to improve consistency and reduce
 * boilerplate in tests.
 */
trait TestSuite extends FunSpec with LawChecks with Test with TestKit

trait TestProps extends PropSpec with PropertyChecks with Test

sealed trait TestInstances {

  import org.scalacheck.{Arbitrary, Gen}
  import org.scalacheck.Arbitrary.arbitrary
  import scala.util.{Failure, Success, Try}

  // To be replaced by https://github.com/rickynils/scalacheck/pull/170
  implicit def arbitraryTry[A: Arbitrary]: Arbitrary[Try[A]] =
    Arbitrary(Gen.oneOf(
      arbitrary[A].map(Success(_)),
      arbitrary[Throwable].map(Failure(_))))
}
