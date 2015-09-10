package bricks
package tests

import bricks.laws.LawChecks
import org.scalatest.{FunSuite, PropSpec}
import org.scalatest.prop.PropertyChecks

sealed trait AllTests 
  extends ScalatestExtra 
  with    TestSettings 
  with    TestInstances 
  with    TestNotifications 
  with    StdTest {

  notifyTestLoading()
  val mod = testType + getTestMode 
  implicit override val generatorDrivenConfig = checkConfiguration(mod)
}

/**
 * An opinionated stack of traits to improve consistency and reduce
 * boilerplate in tests.
 */
trait TestSuite extends FunSuite with LawChecks with AllTests

trait TestProps extends PropSpec with PropertyChecks with AllTests

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
