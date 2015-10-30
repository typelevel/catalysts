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
abstract class SpecLite extends Properties("") with SpecBase[Prop, Properties]{

  override val name:String =  className(this)

  implicit class SpecLiteStringOps(s: String) extends SpecBaseStringOps(s){

    def in[A](a: => A)(implicit ev: (=> A) => Prop): Unit = {
      property(context + ":" + s) = new InStringOps(a)
      ()
    }
  }

  implicit def booleanToProp(b: => Boolean): Prop = Prop.secure(b)
}


