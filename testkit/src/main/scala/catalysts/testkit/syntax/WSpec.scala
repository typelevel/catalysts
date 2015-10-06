package catalysts
package testkit
package syntax

import scala.reflect.ClassTag

trait WSpec[Tk <: TestKit] {self : TestSpec[Tk] => 

//  def assertEquals[A](actual: => A, expected: => A) = assertEq(actual, expected)

  implicit class StringOps(s: String) {

    def should[A](a: => Any): Tk#TestNest = nest(s)(a)

    def in[A](a: => A): Tk#TestBlock = block(s)(a) 
  }
}

trait WSpecMatchers [Tk <: TestKit] { self : TestSpec[Tk] =>

  implicit class AnyOps[A](actual: => A) {

    def must_==(expected: A) = assertEq(actual, expected)
    def mustThrowA[T <: Throwable](implicit m: ClassTag[T]) = throwA
  }
}
