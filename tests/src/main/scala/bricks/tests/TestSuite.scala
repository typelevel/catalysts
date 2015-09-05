package platform
package tests

import bricks.Platform
import bricks.macros._
import org.scalactic.anyvals.{PosZDouble, PosInt}
import org.scalatest.{FunSuite, PropSpec, Matchers}

import org.typelevel.discipline.{Laws, Predicate}
import org.scalatest.prop.{Configuration, PropertyChecks}
import org.typelevel.discipline.scalatest.Discipline

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary

import scala.util.{Failure, Success, Try}

trait TestSettings extends Configuration with Matchers {

  lazy val checkConfiguration: PropertyCheckConfiguration =
    PropertyCheckConfiguration(
      minSuccessful = if (Platform.isJvm) PosInt(100) else PosInt(10),
      maxDiscardedFactor = if (Platform.isJvm) PosZDouble(5.0) else PosZDouble(50.0))

  lazy val slowCheckConfiguration: PropertyCheckConfiguration =
    if (Platform.isJvm) checkConfiguration
    else PropertyCheckConfig(maxSize = 1, minSuccessful = 1)
}

/**
 * An opinionated stack of traits to improve consistency and reduce
 * boilerplate in Cats tests.
 */
trait TestSuite extends FunSuite with Discipline with TestSettings with TestInstances {

  case class LawChecker[L <: Laws](name: String, laws: L) {
    def check(f: L => L#RuleSet): Unit = checkAll(name, f(laws))
  }

  def laws[L[_] <: Laws, A](implicit
      laws: L[A], tag: TypeTagM[A]): LawChecker[L[A]] =
    LawChecker("[" + tag.tpe.toString + "]", laws)

  implicit override val generatorDrivenConfig: PropertyCheckConfiguration =
    checkConfiguration

  // disable scalatest's ===
  override def convertToEqualizer[T](left: T): Equalizer[T] = ???
}

trait TestProps extends PropSpec with PropertyChecks with TestSettings with TestInstances {
  implicit override val generatorDrivenConfig: PropertyCheckConfiguration =
    checkConfiguration

  // disable scalatest's ===
  override def convertToEqualizer[T](left: T): Equalizer[T] = ???
}

trait SlowTestSuite extends TestSuite {
  implicit override val generatorDrivenConfig: PropertyCheckConfiguration =
    slowCheckConfiguration
}

sealed trait TestInstances {
  // To be replaced by https://github.com/rickynils/scalacheck/pull/170
  implicit def arbitraryTry[A: Arbitrary]: Arbitrary[Try[A]] =
    Arbitrary(Gen.oneOf(
      arbitrary[A].map(Success(_)),
      arbitrary[Throwable].map(Failure(_))))
}
