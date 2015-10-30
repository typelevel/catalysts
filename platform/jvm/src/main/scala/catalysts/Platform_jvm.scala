package catalysts

object Platform {
  // using `final val` makes compiler constant-fold any use of these values, dropping dead code automatically
  // $COVERAGE-OFF$Final vals not checked
  final val isJvm = true
  final val isJs = false
  // $COVERAGE-ON$
}
