package catalysts
package specs2

import catalysts.testkit._

//import testkit.TestSpec
class Specs2LawTests extends TestSuite with LawTests[Specs2Kit] with Specs2SpecTest

class Specs2FSpecTests extends TestSuite with FSpecTests[Specs2Kit] with Specs2SpecTest
class Specs2FSuiteTests extends TestSuite with FSuiteTests[Specs2Kit] with Specs2SpecTest
class Specs2WSpecTests extends TestSuite with WSpecTests[Specs2Kit] with Specs2SpecTest
