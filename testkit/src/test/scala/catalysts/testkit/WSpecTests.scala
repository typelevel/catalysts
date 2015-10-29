package catalysts
package testkit

import syntax.{WSpec, WSpecMatchers}

trait  WSpecTests extends WSpec with WSpecMatchers {
  self: TestSpec with TestKit =>

  def err(i: Int) = {
    if (i < 0) throw new Error else i
  }

  "A number" when {
    "One" would {
      "equal One" in {
        1 must_== 1
        err(-1).mustThrowA[Error]
      }
    }
    "Two" would {
      "equal Two" in {
        2 must_== 2
        err(-1).mustThrowA[Error]
      }
    }
  }

  "An empty set" should {
    "have size 0" in {
      assert(Set.empty.size == 0)
    }

    "produce NoSuchElementException when head is invoked" in {
      assert(Set.empty.size == 0)
    }
  }
}
