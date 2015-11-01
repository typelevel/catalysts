package catalysts
package tests

import catalysts.testkit.syntax.FSuite
import catalysts.scalatest.testkit.{TestSpec, TestSuite => ScalatestTestSuite}

trait TestSuite extends  ScalatestTestSuite with TestSpec with FSuite
