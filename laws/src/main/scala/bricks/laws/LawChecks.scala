package bricks
package laws

import bricks.macros._
import org.scalatest.FunSuiteLike
import org.typelevel.discipline.Laws
import org.typelevel.discipline.scalatest.Discipline

trait LawChecks extends Discipline { self: FunSuiteLike =>

  case class LawChecker[L <: Laws](name: String, laws: L) {
    def check(f: L => L#RuleSet): Unit = checkAll(name, f(laws))
  }

  def laws[L[_] <: Laws, A](implicit laws: L[A], tag: TypeTagM[A]): LawChecker[L[A]] =
    LawChecker("[" + tag.name + "]", laws)
}
