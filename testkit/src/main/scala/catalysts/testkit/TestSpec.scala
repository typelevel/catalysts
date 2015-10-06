package catalysts
package testkit

import scala.reflect.ClassTag

/**
 * 
 */
trait TestSpec[Tk <: TestKit] {self => 

  implicit val tk: TestSpec[Tk] = self
  
  def assert_==[A](actual: => A, expected: => A): Tk#AssertResult

  def assert_==[A](msg: String, actual: => A, expected: => A): Tk#AssertResult
 
  def assert_===[A](actual: => A, expected: => A)
     (implicit show: Tk#TestShow[A], equal: Tk#TestEqual[A]): Tk#AssertResult

  def assertThrow[A, T <: Throwable](actual: => A)(implicit m: ClassTag[T]): Tk#ExceptionResult

  def block(s: String)(a: => Any): Tk#TestBlock

  //def fail(s: String): Nothing 

  def nest(s: String)(a: => Any): Tk#TestNest
}


