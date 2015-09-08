package bricks
package tests

class StdTestTests extends TestSuite {

  test("StdTest minSuccessful") {
    val min = if (Platform.isJvm) 100 else 10
    assert(generatorDrivenConfig.minSuccessful.value == min)
  }

  test("StdTest maxDiscardedFactor") {
    val max = if (Platform.isJvm) 5.0 else 50.0
    assert(generatorDrivenConfig.maxDiscardedFactor.value == max)
  }
}

class WellTestedTests extends TestSuite with WellTested {

  test("WellTested minSuccessful") {
    val min = if (Platform.isJvm) 10 else 1
    assert(generatorDrivenConfig.minSuccessful.value == min)
  }

  test("WellTested maxDiscardedFactor") {
    val max = if (Platform.isJvm) 5.0 else 50.0
    assert(generatorDrivenConfig.maxDiscardedFactor.value == max)
  }
}

import org.scalactic.anyvals.{PosZDouble, PosInt}

class WellTestedCustomPlatformTests extends TestSuite with WellTested {

 override def minSuccessful: PosInt =
    if (Platform.isJvm) PosInt(50)
    else PosInt(5)

  override def maxDiscardedFactor: PosZDouble =
    if (Platform.isJvm) PosZDouble(2.0)
    else PosZDouble(20.0)

  override def testType: TestModifier =
    if (Platform.isJvm) TestModifier(0.1, 0.5)
    else TestModifier(0.1, 0.5)

  test("WellTested CustomPlatform  minSuccessful") {
    val min = if (Platform.isJvm) 5 else 1
    assert(generatorDrivenConfig.minSuccessful.value == min)
  }

  test("WellTested CustomPlatform maxDiscardedFactor") {
    val max = if (Platform.isJvm) 1.0 else 10.0
    assert(generatorDrivenConfig.maxDiscardedFactor.value == max)
  }
}

trait MySetup extends TestSettings with StdTest with TestNotifications {

  override  def shouldNotify: Boolean = false

 override def minSuccessful: PosInt =
    if (Platform.isJvm) PosInt(50)
    else PosInt(5)

  override def maxDiscardedFactor: PosZDouble =
    if (Platform.isJvm) PosZDouble(2.0)
    else PosZDouble(20.0)

  override def testType: TestModifier =
    if (Platform.isJvm) TestModifier(0.1, 0.5)
    else TestModifier(0.1, 0.5)
}

class MySetupTests extends TestSuite with MySetup {


  test("MySetup  minSuccessful") {
    val min = if (Platform.isJvm) 5 else 1
    assert(generatorDrivenConfig.minSuccessful.value == min)
  }

  test("MySetup maxDiscardedFactor") {
    val max = if (Platform.isJvm) 1.0 else 10.0
    assert(generatorDrivenConfig.maxDiscardedFactor.value == max)
  }
}
