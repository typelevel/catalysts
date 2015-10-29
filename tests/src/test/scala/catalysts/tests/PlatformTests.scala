package catalysts
package tests

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

  test("dead code elimination") {
    // look at disassembled class files to verify this reduces to a `bipush 42` or `bipush 43`
    val x = if( Platform.isJvm ) 42 else 43
    assert(Platform.isJvm || x == 43)
  }
}
