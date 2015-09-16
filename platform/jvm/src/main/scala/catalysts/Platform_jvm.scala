package catalysts

import scala.reflect.macros.whitebox
import macrocompat.bundle

object Platform {
  def isJvm: Boolean = macro PlatformMacros.isJvmImpl
  def isJs: Boolean = macro  PlatformMacros.isJsImpl
}

@bundle
class PlatformMacros(val c: whitebox.Context) {
  import c.universe._

  def isJvmImpl: c.Expr[Boolean] = c.Expr(Literal(Constant(true)))

  def isJsImpl: c.Expr[Boolean] = c.Expr(Literal(Constant(false)))
}
