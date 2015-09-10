package bricks
package tests

import org.scalatest.Assertions

trait ScalatestExtra { self: Assertions =>

  // disable scalatest's ===
  override def convertToEqualizer[T](left: T): Equalizer[T] = ???
}
