/**
 * Based on https://github.com/scalaz/scalaz/blob/series/7.2.x/tests/src/test/scala/scalaz/SpecLite.scala
 */
package catalysts
package checklite

import org.scalacheck
import org.scalacheck.{Arbitrary, Gen, Prop, Properties, Shrink}

import catalysts.macros.ClassInfo._
import catalysts.testkit.{Equal, Show}
import reflect.ClassTag
import scala.scalajs.js.annotation.JSExportDescendentClasses

import Prop.{Exception, Result}
import Gen.Parameters

@JSExportDescendentClasses
class InStringOps[A](a: => A)(implicit ev: (=> A) => Prop) extends Prop{
  def apply(prms: Parameters): Result = {
    try ev(a).apply(prms) catch {
      case e: Throwable => Result(status = Exception(e))
    }
  }
}
@JSExportDescendentClasses
class PropertyOpsWithProp(propName: String, prop: Prop, name:String, props: Properties) extends Properties(props.name) {
  for {(name, p) <- props.properties} property(name) = p
  property(propName) = prop
}

abstract class CheckLite extends Properties("") {

  override val name:String =  className(this)

  def checkAll(name: String, props: Properties) =
    for ((name2, prop) <- props.properties) yield {
      property(name + ":" + name2) = prop
    }


  def checkAll(props: Properties) = for ((name, prop) <- props.properties) yield  property(name) = prop

  implicit class PropertyOps2(props: Properties) {


    def withProp(propName: String, prop: Prop) = new PropertyOpsWithProp(propName, prop, name, props)
    /*  new Properties(props.name) {
      for {(name, p) <- props.properties} property(name) = p
      property(propName) = prop
    } */
  }

  private var context: String = ""

  def check(x: => Boolean): Prop = x must_== true

  def fail(msg: String): Nothing = throw new AssertionError(msg)

  implicit class StringOps2(s: String) {

    def should[A](a: => Any): Unit = {
      val saved = context
      context = s
      try {
        val r = a
      } finally {
        context = saved
      }
    }

    /*
    def ![A](a: => A)(implicit ev: (A) => Prop): Unit = in(a)
    /*   def in[A](a: => A)(implicit ev: (A) => Prop): Unit = property(context + ":" + s) = new Prop {
          def apply(prms: Parameters): Result = {
           try ev(a).apply(prms) catch {
             case e: Throwable => Result(status = Exception(e))
           }
          } // TODO sort out the laziness / implicit conversions properly
       }
     */
    def in[A](a: => A)(implicit ev: (A) => Prop): Unit = property(context + ":" + s) = new InStringOps(a)
*/
    def ![A](a: => A)(implicit ev: (=> A) => Prop): Unit = in(a)

    def in[A](a: => A)(implicit ev: (=> A) => Prop): Unit = {
      property(context + ":" + s) = new InStringOps(a)
      ()
    }

  }

  implicit class AnyOps2[A](actual: => A) {

    def must_===(expected: A)(implicit s: Show[A], e: Equal[A]): Unit = {
      val act = actual
      def test = e.eqv(expected, act)
      def koMessage = "%s !== %s".format(s.show(act), s.show(expected))

      if (!test)
        fail(koMessage)
    }

    def must_==(expected: A): Unit = {
      val act = actual
      def test = expected == act
      def koMessage = "%s !== %s".format(act, expected)
      if (!test)
        fail(koMessage)
    }

    def mustMatch(f: PartialFunction[A, Boolean]): Unit = {
      val act = actual
      def test = f.isDefinedAt(act) && f(act)
      def koMessage = "%s does not satisfy partial function".format(act)
      if (!test)
        fail(koMessage)
    }

    def and[B](b: => B): B = {
      actual
      b
    }

    def mustBe_<(x: Int)(implicit ev: A <:< Int) = {
      val act = actual
      def test = ev(act) < x
      def koMessage = "%s <! %s".format(actual, x)
      if (!test)
        fail(koMessage)
    }

    def mustThrowA[T <: Throwable](implicit man: ClassTag[T]): Unit = {
      val erasedClass = man.runtimeClass
      val failed:Boolean  =
        try {
          actual
          true
        } catch {
          case ex: Throwable =>
            if (!erasedClass.isInstance(ex))
              fail("wrong exception thrown, expected: " + erasedClass + " got: " + ex)
            else false
        }
      if (failed) fail("no exception thrown, expected " + erasedClass)
    }
  }

  def prop[T, R](result: T => R)(implicit toProp: (=>R) => Prop, a: Arbitrary[T], s: Shrink[T]): Prop = check1(result)
  implicit def propToProp(p: => Prop): Prop = p
  implicit def check1[T, R](result: T => R)(implicit toProp: (=>R) => Prop, a: Arbitrary[T], s: Shrink[T]): Prop = Prop.forAll((t: T) => toProp(result(t)))
  implicit def anyToProp(u: => Any): Prop = booleanToProp({u; true})
  implicit def unitToProp(u: => Unit): Prop = booleanToProp({u; true})
  implicit def unitToProp2(u: Unit): Prop = booleanToProp(true)
  implicit def booleanToProp(b: => Boolean): Prop = Prop.secure(b)

  /**
   * Most of our scalacheck tests use (Int => Int). This generator includes non-constant
   * functions (id, inc), to have a better chance at catching bugs.
   */
  implicit def Function1IntInt[A](implicit A: Arbitrary[Int]): Arbitrary[Int => Int] =
    Arbitrary(Gen.frequency[Int => Int](
     (1, Gen.const((x: Int) => x)),
     (1, Gen.const((x: Int) => x + 1)),
     (3, A.arbitrary.map(a => (_: Int) => a))
   ))
}