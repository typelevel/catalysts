package catalysts
package scalatest

import testkit.TestSpec
import scala.reflect.ClassTag
import org.scalatest.{FunSpecLike, Matchers, Tag}

trait ScalatestTestSpec extends   FunSpecLike with TestSpec[ScalatestKit]  with Matchers { 
object tagTest extends Tag("ScalaTests")
 
 def assert_==[A](actual: => A, expected: => A): ScalatestKit#AssertResult = { 
    assert( actual == expected)
  } 

  def assert_==[A](msg: String, actual: => A, expected: => A): ScalatestKit#AssertResult = { 
    assert( actual == expected, msg)
  } 

  def assert_===[A](actual: => A, expected: => A)
     (implicit show: ScalatestKit#TestShow[A], equal: ScalatestKit#TestEqual[A]): ScalatestKit#AssertResult = 
     actual should === (expected)
  
  def nest(s: String)(a: => Any): Unit = it(s){val r = a}
  def block(s: String)(a: => Any): Unit = { println(s);a ;()}

  def assertThrow[A, T <: Throwable](actual: => A)(implicit m: ClassTag[T]): ScalatestKit#ExceptionResult = { 
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
}
