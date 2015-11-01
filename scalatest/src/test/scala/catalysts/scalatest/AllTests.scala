package catalysts
package scalatest

import catalysts.lawkit.{LawSpecTests, LawTests}
import catalysts.testkit._

class ScalatestLawTests extends Suite with LawTests
class ScalatestLawSpecTests extends Suite with LawSpecTests
class ScalatestFSpecTests extends Suite with FSpecTests
class ScalatestFSuiteTests extends Suite with FSuiteTests
class ScalatestJUnitTests extends Suite with JUnitTests
class ScalatestTestSpecTests extends Suite with TestSpecTests
class ScalatestWSpecTests extends Suite with WSpecTests
