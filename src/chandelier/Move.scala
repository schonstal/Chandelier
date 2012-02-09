package chandelier

import scalabaloo.{Button, MoveController}
import maackle.util

class Move(id: Int) extends MoveController(id) {

   var switch1 = false
   var switch2 = false
   var switch3 = false
   var t = 1.0
   val dt = Chandelier.dt
   def v = (math.sin(t * dt) * 255).toInt

   def everyPoll() {
      util.interval(1) {
         switch1 = !switch1
      }
   }

   def anyPoll() {
      if (buttonDown(Button.CIRCLE)) {
         color(255,255,0)
      }
      else {
         color(0,255,255)
      }
      util.interval(1) {
         switch2 = !switch2
      }

   }

   def update() {
      util.interval(1) {
         switch3 = !switch3
      }
      color.r = if(switch1) 0 else 255
      color.g = if(switch2) 0 else 255
      color.b = if(switch3) 0 else 255
   }

}


