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
     (implicit show: Show[A], equal: Equal[A]): Tk#AssertResult

  def assertThrow[A, T <: Throwable](actual: => A)(implicit m: ClassTag[T]): Tk#ExceptionResult

  def block(s: String)(a: => Any): Tk#TestBlock

  //def fail(s: String): Nothing 

  def nest(s: String)(a: => Any): Tk#TestNest
}


trait Show[T] {
  def show(f: T): String
}

object Show {
  /** creates an instance of [[Show]] using the provided function */
  def show[A](f: A => String): Show[A] = new Show[A] {
    def show(a: A): String = f(a)
  }

  /** creates an instance of [[Show]] using object toString */
  def fromToString[A]: Show[A] = new Show[A] {
    def show(a: A): String = a.toString
  }
}

trait Equal[A] extends Any { self =>

  /**
   * Returns `true` if `x` and `y` are equivalent, `false` otherwise.
   */
  def eqv(x: A, y: A): Boolean

}
object Equal  {

  /**
   * Create an `Eq` instance from an `eqv` implementation.
   */
  def instance[A](f: (A, A) => Boolean): Equal[A] =
    new Equal[A] {
      def eqv(x: A, y: A) = f(x, y)
    }
}
