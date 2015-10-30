package catalysts
package speclite

import catalysts.specbase.SpecBaseTests
import catalysts.testkit._

class SpecliteSpecBaseTests extends Suite with SpecBaseTests[Prop, Properties]
class SpecliteFSpecTests extends Suite with FSpecTests
class SpecliteFSuiteTests extends Suite with FSuiteTests
class SpecliteJUnitTests extends Suite with JUnitTests
class SpecliteTestSpecTests extends Suite with TestSpecTests
class SpeclitetWSpecTests extends Suite with WSpecTests
