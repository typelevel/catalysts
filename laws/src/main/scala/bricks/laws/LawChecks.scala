package bricks
package laws

import bricks.macros._
import org.scalatest.FunSuiteLike
import org.typelevel.discipline.Laws
import org.typelevel.discipline.scalatest.Discipline

/**
 * Trait that facilitates law checks
 * 
 * LawChecks adds some helper methods to `Discipline`'s extension of `FunSuiteLike`.
 *
 * @see [[https://github.com/non/algebra/issues/57 Original issue in Algebra]], 
 *      [[https://github.com/non/algebra/pull/65 Original implementation in Algebra]]
 */
trait LawChecks extends Discipline { self: FunSuiteLike =>

  /**
   * Check the `laws` using `name` as the base name for the tests. 
   */
  case class LawChecker[L <: Laws](name: String, laws: L) {
    def check(f: L => L#RuleSet): Unit = checkAll(name, f(laws))
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
  def laws[L[_] <: Laws, A](implicit laws: L[A], tag: TypeTagM[A]): LawChecker[L[A]] =
    LawChecker("[" + tag.name + "]", laws)
}
