package catalysts
package lawkit

import catalysts.testkit.{TestKit, TestSpec}
import catalysts.testkit.syntax.FSpec

trait LawSuite  extends FSpec with LawKit with LawSpec with TestSpec with TestKit

trait LawSpecTests extends LawSuite {

  it("all One") {
    all { (s: String) =>
      assert_==(s + s, s + s)
    }
  }

  it("all Two") {
    all { (s: String, by: Byte) =>
      assert_==(s + by, s + by)
    }
  }

  it("all Three") {
    all { (s: String, by: Byte, ch: Char) =>
      assert_==(s + by + ch, s + by + ch)
    }
  }

  it("all Four") {
    all { (s: String, by: Byte, ch: Char, i: Int) =>
      assert_==(s + by + ch + i, s + by + ch + i)
    }
  }

  it("all Five") {
    all { (s: String, by: Byte, ch: Char, i: Int, l: Long) =>
      assert_==(s + by + ch + i + l, s + by + ch + i + l)
    }
  }

  it("all Six") {
    all { (s: String, by: Byte, ch: Char, i: Int, l: Long, li: List[Int]) =>
      assert_==(s + by + ch + i + l + li, s + by + ch + i + l + li)
    }
  }
}
