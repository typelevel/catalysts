package catalysts
package scalatest
package testkit

import catalysts.testkit.{TestSpec => BaseTestSpec, Equal, Show}
import catalysts.lawkit.{LawSpec => BaseLawSpec}
import org.scalacheck.{Shrink, Arbitrary}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import scala.reflect.ClassTag
import org.scalatest.{Assertion, FunSpecLike, Matchers}
import org.scalatest.exceptions.TestFailedException

trait TestSpec extends BaseTestSpec with BaseLawSpec with FunSpecLike
    with Matchers with TestKit with GeneratorDrivenPropertyChecks
{
  def level = 3

  def assertEqEqImpl[A](actual: => A, expected: => A): AssertResult = {
    try {
      assert(actual == expected)
    } catch {
      case e: Throwable => throw new TestFailedException(e.getMessage, level)
    }
  }

  def assertMsgEqEqImpl[A](msg: String, actual: => A, expected: => A): AssertResult = {
    try {
      assert(actual == expected, msg)
    } catch {
      case e: Throwable => throw new TestFailedException(e.getMessage, level)
    }
  }

  def assertEqEqEqImpl[A](actual: => A, expected: => A)
      (implicit show: Show[A], equal: Equal[A]): AssertResult = {
    try {
      actual should ===(expected)
    } catch {
      case e: Throwable => throw new TestFailedException(e.getMessage, level)
    }
  }


  def forAllImpl1[T1, R] (fun: (T1) => R)(implicit
      arbA : Arbitrary[T1], shrA : Shrink[T1], pretty1: Prettify[T1]): ForAllResult =
    forAll(fun.asInstanceOf[(T1) => Assertion])

  def forAllImpl2[T1, T2, R](fun: (T1, T2) => R)(implicit
      arbA : Arbitrary[T1], shrA : Shrink[T1], pretty1: Prettify[T1],
      arbB : Arbitrary[T2], shrB : Shrink[T2], pretty2: Prettify[T2]): ForAllResult =
    forAll(fun.asInstanceOf[(T1, T2) => Assertion])

  def forAllImpl3[T1, T2, T3, R](fun: (T1, T2, T3) => R)(implicit
      arbA : Arbitrary[T1], shrA : Shrink[T1], pretty1: Prettify[T1],
      arbB : Arbitrary[T2], shrB : Shrink[T2], pretty2: Prettify[T2],
      arbC : Arbitrary[T3], shrC : Shrink[T3], pretty3: Prettify[T3]): ForAllResult =
    forAll(fun.asInstanceOf[(T1, T2, T3) => Assertion])

  def forAllImpl4[T1, T2, T3, T4, R](fun: (T1, T2, T3, T4) => R)(implicit
      arbA : Arbitrary[T1], shrA : Shrink[T1], pretty1: Prettify[T1],
      arbB : Arbitrary[T2], shrB : Shrink[T2], pretty2: Prettify[T2],
      arbC : Arbitrary[T3], shrC : Shrink[T3], pretty3: Prettify[T3],
      arbD : Arbitrary[T4], shrD : Shrink[T4], pretty4: Prettify[T4]): ForAllResult =
    forAll(fun.asInstanceOf[(T1, T2, T3, T4) => Assertion])

  def forAllImpl5[T1, T2, T3, T4, T5, R](fun: (T1, T2, T3, T4, T5) => R)(implicit
      arbA : Arbitrary[T1], shrA : Shrink[T1], pretty1: Prettify[T1],
      arbB : Arbitrary[T2], shrB : Shrink[T2], pretty2: Prettify[T2],
      arbC : Arbitrary[T3], shrC : Shrink[T3], pretty3: Prettify[T3],
      arbD : Arbitrary[T4], shrD : Shrink[T4], pretty4: Prettify[T4],
      arbE : Arbitrary[T5], shrE : Shrink[T5], pretty5: Prettify[T5]): ForAllResult =
    forAll(fun.asInstanceOf[(T1, T2, T3, T4, T5) => Assertion])

  def forAllImpl6[T1, T2, T3, T4, T5, T6, R](fun: (T1, T2, T3, T4, T5, T6) => R)(implicit
       arbA : Arbitrary[T1], shrA : Shrink[T1], pretty1: Prettify[T1],
       arbB : Arbitrary[T2], shrB : Shrink[T2], pretty2: Prettify[T2],
       arbC : Arbitrary[T3], shrC : Shrink[T3], pretty3: Prettify[T3],
       arbD : Arbitrary[T4], shrD : Shrink[T4], pretty4: Prettify[T4],
       arbE : Arbitrary[T5], shrE : Shrink[T5], pretty5: Prettify[T5],
       arbF : Arbitrary[T6], shrF : Shrink[T6], pretty6: Prettify[T6]): ForAllResult =
    forAll(fun.asInstanceOf[(T1, T2, T3, T4, T5, T6) => Assertion])


  def block(s: String)(a: => Any): Unit = it(s) {
    a
    ()
  }

  def nest(s: String)(a: => Any) : Unit = {
    describe(s) {
      a
      ()
    }
  }

  def assert_ThrowImpl[A, T <: Throwable](actual: => A)
      (implicit m: ClassTag[T]): ExceptionResult = {
    try {
     val r = a[T] must be thrownBy (actual)
    } catch {
      case e: Throwable => throw new org.scalatest.exceptions.TestFailedException(e.getMessage, level)
    }
  }
}
