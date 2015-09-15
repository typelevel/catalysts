package bricks
package specs2

import bricks.macros.ClassInfo._
import bricks.macros._

class MacroTests extends TestSuite {

  def f1[A](implicit tag: TypeTagM[A]): String = tag
  def f2[A](implicit tag: TypeTagM[A]): String = tag.name
  def f3[A](implicit tag: TypeTagM[A]): String = tag.toString

  def is = s2"""

   This is a specification to check Macros

   Macros should
     Use the correct class name              $TestClassName
     get the tag name from a tag             $e1
     have a name method                      $e2
     convert to string                       $e3
   """

  def TestClassName = className(this)  must beEqualTo("bricks.specs2.MacroTests")
 
    val expected = "List[Map[Int,Double]]"
    def e1 = f1[List[Map[Int, Double]]] must beEqualTo(expected)
    def e2 = f2[List[Map[Int, Double]]] must beEqualTo(expected)
    def e3 = f3[List[Map[Int, Double]]] must beEqualTo(expected)
}
