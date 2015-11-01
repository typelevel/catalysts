package catalysts
package specs2
package testkit

import catalysts.testkit.{Equal, Show, TestSpec => BaseTestSpec}
import catalysts.lawkit.{LawSpec => BaseLawSpec}
import org.scalacheck.{Shrink, Arbitrary}

import org.specs2.scalacheck.ScalaCheckProperty
import org.specs2.ScalaCheck
import scala.reflect.ClassTag
import org.specs2.mutable.SpecificationLike
import org.specs2.execute.{AsResult, AnyValueAsResult}

trait TestSpec extends BaseTestSpec with BaseLawSpec with SpecificationLike
    with ScalaCheck with TestKit  {

  def assertEqEqImpl[A](actual: => A, expected: => A): AssertResult = {
    actual must_== expected
  }

  def assertMsgEqEqImpl[A](msg: String, actual: => A, expected: => A): AssertResult = {
    actual aka msg must_== expected
  }

  def assertEqEqEqImpl[A](actual: => A, expected: => A)
      (implicit show: Show[A], equal: Equal[A]): AssertResult =
    actual must_=== expected

  def assert_ThrowImpl[A, T<:Throwable](actual: => A)(implicit m: ClassTag[T]): ExceptionResult = {
    val erasedClass = m.runtimeClass
     val failed  =
       try {
         actual
         "no exception throasResult: AsResult[R]wn, expected " + erasedClass
       } catch {
         case ex: Throwable =>
           if (!erasedClass.isInstance(ex))
             "wrong exception thrown, expected: " + erasedClass + " got: " + ex
           else ""
       }
     assert_==(failed, failed.isEmpty, true)
  }

  def block(s: String)(a: => Any): TestBlock =
    s in new AnyValueAsResult().asResult(a)

  def nest(s: String)(a: => Any): TestNest =
    s >> a.asInstanceOf[TestBlock]

  import org.specs2.execute.AnyValueAsResult
  implicit def anyVal[R]:AsResult[R] = new AnyValueAsResult[R]

  def forAllImpl1[T1, R](fun: (T1) => R)(implicit
      arbA : Arbitrary[T1], shrA : Shrink[T1], pretty1: Prettify[T1]): ForAllResult = {

    val p: ScalaCheckProperty = prop(fun)
    check(p.prop, p.parameters, p.prettyFreqMap)
  }

  def forAllImpl2[T1, T2, R](fun: (T1, T2) => R)(implicit
      arbA : Arbitrary[T1], shrA : Shrink[T1], pretty1: Prettify[T1],
      arbB : Arbitrary[T2], shrB : Shrink[T2], pretty2: Prettify[T2]): ForAllResult = {

    val p: ScalaCheckProperty = prop(fun)
    check(p.prop, p.parameters, p.prettyFreqMap)
  }

  def forAllImpl3[T1, T2, T3, R](fun: (T1, T2, T3) => R)(implicit
      arbA : Arbitrary[T1], shrA : Shrink[T1], pretty1: Prettify[T1],
      arbB : Arbitrary[T2], shrB : Shrink[T2], pretty2: Prettify[T2],
      arbC : Arbitrary[T3], shrC : Shrink[T3], pretty3: Prettify[T3]): ForAllResult = {

    val p: ScalaCheckProperty = prop(fun)
    check(p.prop, p.parameters, p.prettyFreqMap)
  }

  def forAllImpl4[T1, T2, T3, T4, R](fun: (T1, T2, T3, T4) => R)(implicit
      arbA : Arbitrary[T1], shrA : Shrink[T1], pretty1: Prettify[T1],
      arbB : Arbitrary[T2], shrB : Shrink[T2], pretty2: Prettify[T2],
      arbC : Arbitrary[T3], shrC : Shrink[T3], pretty3: Prettify[T3],
      arbD : Arbitrary[T4], shrD : Shrink[T4], pretty4: Prettify[T4]): ForAllResult = {

    val p: ScalaCheckProperty = prop(fun)
    check(p.prop, p.parameters, p.prettyFreqMap)
  }

  def forAllImpl5[T1, T2, T3, T4, T5, R](fun: (T1, T2, T3, T4, T5) => R)(implicit
      arbA : Arbitrary[T1], shrA : Shrink[T1], pretty1: Prettify[T1],
      arbB : Arbitrary[T2], shrB : Shrink[T2], pretty2: Prettify[T2],
      arbC : Arbitrary[T3], shrC : Shrink[T3], pretty3: Prettify[T3],
      arbD : Arbitrary[T4], shrD : Shrink[T4], pretty4: Prettify[T4],
      arbE : Arbitrary[T5], shrE : Shrink[T5], pretty5: Prettify[T5]): ForAllResult = {

    val p: ScalaCheckProperty = prop(fun)
    check(p.prop, p.parameters, p.prettyFreqMap)
  }

  def forAllImpl6[T1, T2, T3, T4, T5, T6, R](fun: (T1, T2, T3, T4, T5, T6) => R)(implicit
      arbA : Arbitrary[T1], shrA : Shrink[T1], pretty1: Prettify[T1],
      arbB : Arbitrary[T2], shrB : Shrink[T2], pretty2: Prettify[T2],
      arbC : Arbitrary[T3], shrC : Shrink[T3], pretty3: Prettify[T3],
      arbD : Arbitrary[T4], shrD : Shrink[T4], pretty4: Prettify[T4],
      arbE : Arbitrary[T5], shrE : Shrink[T5], pretty5: Prettify[T5],
      arbF : Arbitrary[T6], shrF : Shrink[T6], pretty6: Prettify[T6]): ForAllResult = {

    val p: ScalaCheckProperty = prop(fun)
    check(p.prop, p.parameters, p.prettyFreqMap)
  }
}
