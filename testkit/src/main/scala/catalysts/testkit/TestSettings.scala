package catalysts
package testkit

trait TestSettings {

  def minSuccessful: Int =
    if (Platform.isJvm) 100 else 10

  def maxDiscardedFactor: Double =
    if (Platform.isJvm) 5.0 else 50.0

  def checkSettings(mode:TestModifier): (Int, Double) =
    (mode.minSuccessful(minSuccessful), mode.maxDiscardedFactor(maxDiscardedFactor))

  def getTestMode: TestModifier = NormalTestMode.testMode
}
