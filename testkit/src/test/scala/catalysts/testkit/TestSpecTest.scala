package catalysts
package testkit

trait TestSpecTests { self: TestSpec =>

  def err(i: Int) = {
    if (i < 0) throw new ArrayIndexOutOfBoundsException else i
  }

  implicit val showInt: Show[Int] = Show.fromToString[Int]
  implicit val equalInt: Equal[Int] = Equal.instance[Int]( _ == _  )

  block("ss") {
    assert_==("123", showInt.show(123))
    assert_==("123", Show.show[Int](_.toString).show(123))

    assert_==(true, equalInt.eqv(123, 123))

    assert_==(1, 1)
    assert_==("One == 1", 1, 1)
    assert_===(1, 1)
    assert_Throw[Int, ArrayIndexOutOfBoundsException](err(-1))

    val assertThrowTest1 =
      try {
        assert_Throw[Int, ArrayIndexOutOfBoundsException](err(1))
        false
      } catch {
        case ex: Throwable => true
      }
    assert_==(assertThrowTest1, true)

    val assertThrowTest2 =
      try {
        assert_Throw[Int, Error](err(-1))
        false
      } catch {
        case ex: Throwable => true
      }
    assert_==(assertThrowTest2, true)
  }

  val assertEqEqMsg1 =
    try {
      assert_==("One == 2", 1, 2)
      false
    } catch {
      case ex: Throwable => true
    }
  assert_==(assertEqEqMsg1, true)

  val assertEqEqEq1 =
    try {
      assert_===(1, 2)
      false
    } catch {
      case ex: Throwable => true
    }
  assert_==(assertEqEqEq1, true)
}
