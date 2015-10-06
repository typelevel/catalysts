package org.junit

//import scala.reflect.macros.whitebox.Context
import scala.language.experimental.macros
import scala.reflect.macros.whitebox
import macrocompat.bundle

import scala.annotation.StaticAnnotation
import scala.annotation.compileTimeOnly

@compileTimeOnly("enable macro paradise to expand macro annotations")
class Test  extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro TestMacro.impl
}

@bundle
class TestMacro(val c: whitebox.Context) {
  import c.universe._

//object TestMacro {
//  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {

//    import c.universe._

  def impl(annottees: c.Expr[Any]*): c.Expr[Any] = {

    val inputs = annottees.map(_.tree). toList
    val (annottee, expandees) = inputs match {
      case (param: ValDef) :: (rest @ (_ :: _)) => (param, rest)
      case (param: TypeDef) :: (rest @ (_ :: _)) => (param, rest)
      case _ => (EmptyTree, inputs)
    }
    val n = expandees(0).toString().replace("def ", "").split(":").head
    val t =  annottees.map(_.tree)

    val pp = TermName(n)
    val exp = q"""
            $n in {
              $pp
             }
             ..$t
      """

    c.Expr[Any](exp)
  }
}

object Assert {
  def assertArrayEquals = 0
}
