package catalysts
package testkit

trait TestKit {

  // Types related to test type
  type AssertResult
  type ExceptionResult
  type TestBlock
  type TestEqual[A]
  type TestNest
  type TestShow[A]
}
