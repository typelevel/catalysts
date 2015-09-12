package bricks
package tests

import org.scalatest.Matchers
import org.scalactic.anyvals.{PosZDouble, PosInt}
import org.scalatest.prop.Configuration

trait TestSettings extends Configuration with Matchers with TestInfo { self: TestType =>

  def minSuccessful: PosInt =
    if (Platform.isJvm) PosInt(100) else PosInt(10)

  def maxDiscardedFactor: PosZDouble =
    if (Platform.isJvm) PosZDouble(5.0) else PosZDouble(50.0)

  def checkConfiguration(mode:TestModifier): PropertyCheckConfiguration =
    PropertyCheckConfiguration(
      minSuccessful = mode.minSuccessful(minSuccessful),
       maxDiscardedFactor = mode.maxDiscardedFactor(maxDiscardedFactor)
    )

  def getTestMode: TestModifier = NormalTestMode.testMode
}
