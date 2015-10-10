package catalysts
package testkit

import syntax.{FSuite, FSuiteMatchers}

trait TestSpecTests[Tk <: TestKit] extends TestSpec[Tk] { self: TestSuite =>

  def err(i: Int) = {
    if (i < 0) throw new Error else i
  }


  implicit val showInt: Show[Int] = Show.fromToString[Int]
  implicit val equalInt: Equal[Int] = Equal.instance[Int]( _ == _  )

  assert_==(1, 1)
  assert_==("One == 1", 1, 1)
  assert_===(1, 1)
  assertThrow[Int, Error](err(-1))

  val assertThrowTest1 = 
    try{
      assertThrow[Int, AssertionError](err(1))
      false
    } catch {
      case ex: Throwable => true
    }
  assert(assertThrowTest1)
 
  val assertThrowTest2 = 
    try{
      assertThrow[Int, AssertionError](err(-1))
      false
    } catch {
      case ex: AssertionError => false
      case ex: Throwable => true
    }
  assert(assertThrowTest2)
}
