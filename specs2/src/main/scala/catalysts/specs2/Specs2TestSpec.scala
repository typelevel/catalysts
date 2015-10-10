package catalysts
package specs2

import testkit.{Equal, Show, TestSpec}
import scala.reflect.ClassTag

import org.specs2.specification.core.{Execution, Fragments}
import org.specs2.mutable.SpecificationLike
import org.specs2.execute.AnyValueAsResult
import org.specs2.execute.ResultImplicits
import org.specs2.matcher.MatchersImplicits

trait Specs2SpecTest extends TestSpec[Specs2Kit] with SpecificationLike {
 import org.specs2.specification.create.FragmentFactory

 val frf = fragmentFactory

  def assert_==[A](actual: => A, expected: => A): Specs2Kit#AssertResult = {
    actual must_== expected
  }

  def assert_==[A](msg: String, actual: => A, expected: => A): Specs2Kit#AssertResult = {
    actual aka msg must_== expected
  }

  def assert_===[A](actual: => A, expected: => A)
     (implicit show: Show[A], equal: Equal[A]): Specs2Kit#AssertResult =
    actual must_=== expected

  def assertThrow[A, T<:Throwable](actual: => A)(implicit m: ClassTag[T]): Specs2Kit#ExceptionResult = {
    val erasedClass = m.runtimeClass
     val failed  =
       try {
         actual
         "no exception thrown, expected " + erasedClass
       } catch {
         case ex: Throwable =>
           if (!erasedClass.isInstance(ex))
             "wrong exception thrown, expected: " + erasedClass + " got: " + ex
           else ""
       }
     //if (failed) fail("no exception thrown, expected " + erasedClass)
     assert_==(failed, failed.isEmpty, true)
  }

 def block(s: String)(a: => Any): Specs2Kit#TestBlock =  {
   s in new AnyValueAsResult().asResult(a)
 }

 def nest(s: String)(a: => Any): Specs2Kit#TestNest = {s should  a.asInstanceOf[Specs2Kit#TestBlock]}

}
