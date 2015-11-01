package catalysts
package lawkit

import org.scalacheck.util.Pretty

trait LawKit {

  // Types related to Laws
  type Structure

  // Types related to Property testing
  type ForAllResult
  type Prettify[T] = T => Pretty
}
