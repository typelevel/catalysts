package catalysts
package speclite

import catalysts.macros.ClassInfo._
import catalysts.specbase.SpecBase
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

@JSExportDescendentClasses
abstract class SpecLite extends Properties("") with SpecBase[Prop, Properties]{

  override val name:String =  className(this)

  def checkAll(name: String, props: Properties) =
    for ((name2, prop) <- props.properties) yield {
      property(name + ":" + name2) = prop
    }

  def checkAll(props: Properties) = for ((name, prop) <- props.properties) yield property(name) = prop

  implicit class PropertyOps(props: Properties) {

    def withProp(propName: String, prop: Prop) = new PropertyOpsWithProp(propName, prop, name, props)
  }


  implicit class StringOps2(s: String) extends StringOps(s){

    def in[A](a: => A)(implicit ev: (=> A) => Prop): Unit = {
      property(context + ":" + s) = new InStringOps(a)
      ()
    }
  }

  implicit def booleanToProp(b: => Boolean): Prop = Prop.secure(b)
}


