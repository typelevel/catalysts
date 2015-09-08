package bricks

import macros.XScala

object Platform {
  def isJvm: Boolean = macro isJvmImpl
  def isJs: Boolean = macro isJsImpl

  def isJvmImpl(c: XScala.Context): c.Expr[Boolean] = {
    import c.universe._
    c.Expr(Literal(Constant(false)))
  }

  def isJsImpl(c: XScala.Context): c.Expr[Boolean] = {
    import c.universe._
    c.Expr(Literal(Constant(true)))
  }
}
