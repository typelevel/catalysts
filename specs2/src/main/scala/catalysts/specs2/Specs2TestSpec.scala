package catalysts
package specs2

import testkit.TestSpec

import org.specs2.specification.core.{Execution, Fragments}
//import org.specs2.execute.AsResult//
import org.specs2.mutable.SpecificationLike
import org.specs2.execute.AnyValueAsResult//
import org.specs2.execute.ResultImplicits

trait Specs2SpecTest extends TestSpec[Specs2Kit] with SpecificationLike {
 import org.specs2.specification.create.FragmentFactory

 val frf = fragmentFactory

  def assertEq[A](actual: => A, expected: => A): Specs2Kit#AssertResult = { 
    actual must_== expected
  } 


  def assertEq[A](msg: String, actual: => A, expected: => A): Specs2Kit#AssertResult = { 
    actual aka msg must_== expected
  } 

 // def fail(s: String): Nothing 
/*
 def test(s: String)(a: => Any): Unit =  {
    val r= Fragments( frf.example(s, new AnyValueAsResult().asResult(a)), frf.break).fragments // s >> {a}//s in {val r = a}
  }
 */
//import org.specs2.matcher.ShouldMatchers
import org.specs2.specification.dsl.mutable.MutableDsl
 def block(s: String)(a: => Any): Specs2Kit#TestBlock =  {
   //val r = s should { s in new AnyValueAsResult().asResult(a)}  
   s in new AnyValueAsResult().asResult(a)
 }

def nest(s: String)(a: => Any): Specs2Kit#TestNest = {s should  a.asInstanceOf[Specs2Kit#TestBlock]}

}
