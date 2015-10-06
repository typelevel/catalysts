package catalysts
package testkit
package syntax

trait FSuite[Tk <: TestKit] {self : TestSpec[Tk] => 
  def test(s: String)(a: => Any) = block(s)(a)
  def suite(s: String)(a: => Any) = nest(s)(a)
}

trait FSuiteMatchers [Tk <: TestKit] {self : TestSpec[Tk] =>
  import java.util.Arrays

  def assertEquals[A](actual: => A, expected: => A) = assertEq(actual, expected)

  def assetTrue(x: => Boolean) = assertEq(x, true)

  def assertEquals[A](msg:String, actual: => A, expected: => A) = assertEq(msg, actual, expected)

  def assertFalse(actual: => Boolean) = assertEq[Boolean](actual, false)

  def assertTrue(actual: => Boolean) = assertEq[Boolean](actual, true)

  def assertTrue() = assertEq[Boolean](true, true)

  def assertFalse() = assertEq[Boolean](true, false)

  def assertEquals(v1: Double, v2: Double, delta:Double) =
    assertTrue(Math.abs(v1 - v2) < delta)

  def assertArrayEquals(v1: Array[Object], v2: Array[Object]) =
    assertTrue(Arrays.equals(v1, v2))

  def assertTypedEquals[A](actual: A, expected: A) = assertEq(actual, expected)

  def assertTypedSame[A <: AnyRef](actual: A, expected: A) = assertTrue(actual eq expected)
}
