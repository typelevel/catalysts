package catalysts
package lawkit

import org.scalacheck.{Arbitrary, Prop}
import org.typelevel.discipline.Laws

// adatped from Discipline, adapted from spire

object Dummy {
  def prop = Prop(_ => Prop.Result(status = Prop.True))
}

object GroupLaws {
  def apply[A : Arbitrary] = new GroupLaws[A] {}
}

trait GroupLaws[A] extends Laws {

  def semigroup = new GroupProperties(
    name = "semigroup",
    parent = None,
    "associative" → Dummy.prop
  )

  def monoid = new GroupProperties(
    name = "monoid",
    parent = Some(semigroup),
    "identity" → Dummy.prop
  )

  def group = new GroupProperties(
    name = "group",
    parent = Some(monoid),
    "inverse" → Dummy.prop
  )

  def additiveSemigroup = new AdditiveProperties(
    base = semigroup,
    parent = None
  )

  def additiveMonoid = new AdditiveProperties(
    base = monoid,
    parent = Some(additiveSemigroup)
  )

  def additiveGroup = new AdditiveProperties(
    base = group,
    parent = Some(additiveMonoid)
  )

  class GroupProperties(
    name: String,
    parent: Option[GroupProperties],
    props: (String, Prop)*
  ) extends DefaultRuleSet(name, parent, props: _*)

  class AdditiveProperties(
    val base: GroupProperties,
    val parent: Option[AdditiveProperties],
    val props: (String, Prop)*
  ) extends RuleSet with HasOneParent {
    val name = base.name
    val bases = Seq("base" → base)
  }
}
