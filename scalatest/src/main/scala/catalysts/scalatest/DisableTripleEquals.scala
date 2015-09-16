package catalysts
package scalatest

import org.scalatest.Assertions

trait DisableTripleEquals { self: Assertions =>

  // disable scalatest's ===
  override def convertToEqualizer[T](left: T): Equalizer[T] = ???
}
