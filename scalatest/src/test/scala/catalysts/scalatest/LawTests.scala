package catalysts
package scalatest

import catalysts.testkit._

class ScalatestLawTests extends TestSuite with LawTests[ScalatestKit] with ScalatestTestSpec
class ScalatestTestSpecTests extends TestSuite with TestSpecTests[ScalatestKit] with ScalatestTestSpec
class ScalatestFSpecTests extends TestSuite with FSpecTests[ScalatestKit] with ScalatestTestSpec
class ScalatestFSuiteTests extends TestSuite with FSuiteTests[ScalatestKit] with ScalatestTestSpec
class ScalatestWSpecTests extends TestSuite with WSpecTests[ScalatestKit] with ScalatestTestSpec
