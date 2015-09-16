package catalysts
package testkit

case class TestModifier(minSuccessfulMult: Double, maxDiscardedMult: Double) {

  def * (that: TestModifier): TestModifier = {
    val minSuccess = this.minSuccessfulMult * that.minSuccessfulMult
    val maxDiscard = this.maxDiscardedMult * that.maxDiscardedMult
    TestModifier(minSuccess, maxDiscard)
  }

  def minSuccessful(i: Int): Int  =  {
    val d = i * minSuccessfulMult
    if (d < 1.0) 1 else d.toInt
  }

  def maxDiscardedFactor(d: Double): Double = d * maxDiscardedMult
}
