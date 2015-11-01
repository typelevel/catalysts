package catalysts
package tests


import catalysts.scalatest.testkit._

trait Suite extends TestSuite with TestSpec

import catalysts.lawkit.LawTests
import catalysts.testkit._

class ScalatestLawTests extends Suite with LawTests
class ScalatestFSpecTests extends Suite with FSpecTests
class ScalatestFSuiteTests extends Suite with FSuiteTests
class ScalatestTestSpecTests extends Suite with TestSpecTests
class ScalatestWSpecTests extends Suite with WSpecTests