package catalysts
package testkit

import catalysts.macros._

/**
 * Trait that facilitates law checks
 * 
 * LawChecks adds some helper methods to `Discipline`'s extension of `FunSuiteLike`.
 *
 * @see [[https://github.com/non/algebra/issues/57 Original issue in Algebra]], 
 *      [[https://github.com/non/algebra/pull/65 Original implementation in Algebra]]
 */
trait LawChecks[Tk <: TestKit] {

  /**
   * Check the `laws` using `name` as the base name for the tests. 
   */
  case class LawChecker[L <: Tk#Laws](name: String, laws: L) {
    def check(f: L => Tk#RuleSet): Tk#Structure = checkAllLaws(name, f(laws))
  }

  /**
   * Check the `laws` for some type `A`, using the full name of `A` as the base name for the tests.
   * 
   * {{{
   * implicit def orderLaws[A: Eq: Arbitrary] = OrderLaws[A]
   * implicit def groupLaws[A: Eq: Arbitrary] = GroupLaws[A]
   * 
   * laws[OrderLaws, String].check(_.order)
   * laws[GroupLaws, String].check(_.monoid)
   * }}}

   * @tparam L   the type of laws
   * @tparam A   the type to run the laws on
   * @param laws the laws to use
   * @param tag  the tag associated with type to test
   * @return     the `LawChecker` to check the law
   */
  def laws[L[_] <: Tk#Laws, A](implicit laws: L[A], tag: TypeTagM[A]): LawChecker[L[A]] =
    LawChecker("[" + tag.name + "]", laws)

  def checkAllLaws(name: String, ruleSet: Tk#RuleSet): Tk#Structure
}

object LawChecks {
  implicit def apply[T]: TypeTagM[T] = TypeTagM[T]
}