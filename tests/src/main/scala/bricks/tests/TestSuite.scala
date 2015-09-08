package bricks
package tests

import bricks.Platform
import bricks.macros._
import bricks.macros.ClassMacros._

import org.scalactic.anyvals.{PosZDouble, PosInt}
import org.scalatest.{Assertions, FunSuite, PropSpec, Matchers}

import org.typelevel.discipline.{Laws, Predicate}
import org.scalatest.prop.{Configuration, PropertyChecks}
import org.typelevel.discipline.scalatest.Discipline

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary

import scala.util.{Failure, Success, Try}

case class TestModifier(minSuccessfulMult: Double, maxDiscardedMult: Double) {

  def + (that: TestModifier): TestModifier = {
    val minSuccess = this.minSuccessfulMult * that.minSuccessfulMult
    val maxDiscard = this.maxDiscardedMult * that.maxDiscardedMult
    TestModifier(minSuccess, maxDiscard)
  }

  def minSuccessful(i: PosInt): PosInt  =  {
    val d = i * minSuccessfulMult
    val result = if (d < 1.0) 1 else d.toInt
    PosInt.from(result).get
  }

  def maxDiscardedFactor(d: PosZDouble): PosZDouble = PosZDouble.from(d * maxDiscardedMult).get
}


sealed trait TestType  {
  def testType: TestModifier
}

trait StdTest extends TestType {
  override def testType: TestModifier = TestModifier(1.0, 1.0)
}

trait WellTested extends TestType {
  override def testType: TestModifier =
    if (Platform.isJvm) TestModifier(0.1, 1.0)
    else TestModifier(0.1, 1.0)
}

trait OnyOneInJsTestType extends TestType {
  override def testType: TestModifier = TestModifier(0.1, 1.0)
}


// Just a synonym, may change later
//trait WellCovered extends WellTested

case class TestValues (minSuccessful: Double, maxDiscarded: Double)

trait TestSettings extends Configuration with Matchers with TestInfo { self: TestType =>

  def minSuccessful: PosInt =
    if (Platform.isJvm) PosInt(100)
    else PosInt(10)

  def maxDiscardedFactor: PosZDouble =
    if (Platform.isJvm) PosZDouble(5.0)
    else PosZDouble(50.0)

  def checkConfiguration(mode:TestModifier): PropertyCheckConfiguration =
    PropertyCheckConfiguration(
      minSuccessful = mode.minSuccessful(minSuccessful),
       maxDiscardedFactor = mode.maxDiscardedFactor(maxDiscardedFactor)
    )
}

trait TestInfo {
  def testName = className(this).split('.').last

  def projectKey = "bricks"
}


trait TestNotifications extends TestInfo {
  def shouldNotify: Boolean = true

  def notifyTestLoading(): Unit = {
    import scala.Console.GREEN

    if (shouldNotify)
      println(s"[info]${GREEN} ${testName} loading...")
  }
}

import org.scalatest.FunSuiteLike
trait LawChecks extends Discipline { self: FunSuiteLike =>

  case class LawChecker[L <: Laws](name: String, laws: L) {
    def check(f: L => L#RuleSet): Unit = checkAll(name, f(laws))
  }

  def laws[L[_] <: Laws, A](implicit laws: L[A], tag: TypeTagM[A]): LawChecker[L[A]] =
    LawChecker("[" + tag.tpe.toString + "]", laws)
}

trait ScalatestExtra { self: Assertions =>

  // disable scalatest's ===
  override def convertToEqualizer[T](left: T): Equalizer[T] = ???
}

/**
 * An opinionated stack of traits to improve consistency and reduce
 * boilerplate in tests.
 */
trait TestSuite extends FunSuite with LawChecks with ScalatestExtra with TestSettings with TestInstances with TestNotifications with StdTest {

  notifyTestLoading()

  implicit override val generatorDrivenConfig: PropertyCheckConfiguration = checkConfiguration(testType)
}

trait TestProps extends PropSpec with PropertyChecks with ScalatestExtra with TestSettings with TestInstances with TestNotifications with StdTest {

  notifyTestLoading()

  implicit override val generatorDrivenConfig: PropertyCheckConfiguration = checkConfiguration(testType)
}

sealed trait TestInstances {
  // To be replaced by https://github.com/rickynils/scalacheck/pull/170
  implicit def arbitraryTry[A: Arbitrary]: Arbitrary[Try[A]] =
    Arbitrary(Gen.oneOf(
      arbitrary[A].map(Success(_)),
      arbitrary[Throwable].map(Failure(_))))
}
