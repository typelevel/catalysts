package catalysts
package testkit

/**
 * 
 */
trait TestSpec[Tk <: TestKit] {self => 

  implicit val tk: TestSpec[Tk] = self
  
  def assertEq[A](actual: => A, expected: => A): Tk#AssertResult

  def assertEq[A](msg: String, actual: => A, expected: => A): Tk#AssertResult

  def block(s: String)(a: => Any): Tk#TestBlock

  //def fail(s: String): Nothing 

  def nest(s: String)(a: => Any): Tk#TestNest
}


