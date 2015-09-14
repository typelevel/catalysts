package bricks
package tests


import bricks.scalatest.TestSuite

class PlatformTests extends TestSuite {

  def hackyIsJS(): Boolean = 1.0.toString == "1"

  def hackyIsJVM(): Boolean = !hackyIsJS()

  test("test isJvm") {
    assert(Platform.isJvm == hackyIsJVM())
  }

  test("test isJs") {
    assert(Platform.isJs == hackyIsJS())
  }

  test("test isJvm != isJS") {
    assert(Platform.isJs != Platform.isJvm)
    assert(hackyIsJS != hackyIsJVM)
  }
}

