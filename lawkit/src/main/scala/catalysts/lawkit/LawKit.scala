package catalysts
package lawkit

import org.scalacheck.util.Pretty

trait LawKit {
  // Types related to Laws
  type Params
  type Structure

  // Types related to Property testing
  type Prettify[T] = T => Pretty
}
