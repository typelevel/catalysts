package bricks
package tests

import org.scalactic.anyvals.{PosZDouble, PosInt}

case class TestModifier(minSuccessfulMult: Double, maxDiscardedMult: Double) {

  def + (that: TestModifier): TestModifier = {
    val minSuccess = this.minSuccessfulMult * that.minSuccessfulMult
    val maxDiscard = this.maxDiscardedMult * that.maxDiscardedMult
    TestModifier(minSuccess, maxDiscard)
  }

  def minSuccessful(i: PosInt): PosInt  =  {
    val d = i * minSuccessfulMult
    val result = if (d < 1.0) 1 else d.toInt
    PosInt.from(result).get
  }

  def maxDiscardedFactor(d: PosZDouble): PosZDouble = PosZDouble.from(d * maxDiscardedMult).get
}
