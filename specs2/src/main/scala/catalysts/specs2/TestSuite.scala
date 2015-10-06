package catalysts
package specs2

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

import org.specs2.scalacheck.Parameters
import testkit.{TestModifier, TestSettings, TestSuite => BaseTestSuite}

//import org.specs2.main.{CommandLine, CommandLineAsResult}

//import org.specs2.control.ImplicitParameters._

//import org.specs2.specification.dsl.mutable.ExampleDsl.BlockExample


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
trait TestSuite extends Specification with LawsChecks {
 
//def des(s: String)(a: => Fragments): Unit = {val r = s should  {a }}
/*
  def shouldImpl[A](s: String, a: => Any): Unit =  {
    val b: BlockExample = s
    val f:Fragment = b            .in (a.asInstanceOf[org.specs2.specification.core.Execution])
  }

import org.specs2.specification.create.S2StringContext
  def shouldImpl[A](s: String, a: => Any): Unit =  inImpl(s,a)

  def inImapl[A](s: String, a: => Any): Unit = {
    val b: BlockExample = s
    val f:Fragment = b            .in (a.asInstanceOf[org.specs2.specification.core.Execution])
  }
 */
}

trait TestProps extends Specs2Tests with ScalaCheck
