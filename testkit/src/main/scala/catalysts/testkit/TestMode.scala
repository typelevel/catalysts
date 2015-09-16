package catalysts
package testkit

sealed trait TestMode {
  def testMode: TestModifier
}

final object NormalTestMode extends TestMode {
  def testMode: TestModifier = TestModifier(1.0, 1.0)
}

final object StressTestMode extends TestMode {
  def testMode: TestModifier = 
    if (Platform.isJvm) TestModifier(1000, 100.0) else TestModifier(100, 10.0)
}
