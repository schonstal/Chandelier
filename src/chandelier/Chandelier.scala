package chandelier

import scalabaloo.Framework

object Chandelier extends Framework(0.010) {

   def main(args: Array[String]) {
      this += new Move(0)
      run()
   }

}
