package bricks
package tests

import bricks.Platform

sealed trait TestType  {
  def testType: TestModifier
}

trait StdTest extends TestType {
  override def testType: TestModifier = TestModifier(1.0, 1.0)
}

trait WellTested extends TestType {
  override def testType: TestModifier =
    if (Platform.isJvm) TestModifier(0.1, 1.0)
    else TestModifier(0.1, 1.0)
}
