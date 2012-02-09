package scalabaloo

import psmove.PSMove
import psmove.{Button=>B}
import maackle.vec3
import maackle.stateful.{Stateful}

object Button {
   val CIRCLE = B.Btn_CIRCLE.##
   val CROSS = B.Btn_CROSS.##
   val DOWN = B.Btn_DOWN.##
   val L1 = B.Btn_L1.##
   val L2 = B.Btn_L2.##
   val L3 = B.Btn_L3.##
   val LEFT = B.Btn_LEFT.##
   val MOVE = B.Btn_MOVE.##
   val PS = B.Btn_PS.##
   val R1 = B.Btn_R1.##
   val R2 = B.Btn_R2.##
   val R3 = B.Btn_R3.##
   val RIGHT = B.Btn_RIGHT.##
   val SELECT = B.Btn_SELECT.##
   val START = B.Btn_START.##
   val T = B.Btn_T.##
   val TRIANGLE = B.Btn_TRIANGLE.##
   val UP = B.Btn_UP.##
}


abstract class MoveController(val id:Int) extends PSMove(id) {

   val GSCALE = 4000f
   val buttons = new Stateful[Int](2, 0)
   val pollFrames = new Stateful(10,0)

   var _avgFrames = 0f // frames per tick
   def framesPerTick = _avgFrames

   object color {
      var (r,g,b) = (0, 0, 0)
      def apply(r:Int, g:Int, b:Int) {
         this.r = r
         this.g = g
         this.b = b
      }
      def set() { set_leds(r,g,b) }
      override def toString = "color(%d, %d, %d)".format(r,g,b)
   }

   /** called once or every poll() frame received */
   protected def everyPoll()
   /** called once per tick where any poll data was received */
   protected def anyPoll()
   /** called once per tick */
   protected def update()

   def tick() {
      var p:Int = 0
      var polls = 0
      while({p=poll(); p}>0) {
         everyPoll()
         polls+=1
      }
      pollFrames << polls
      if(polls>0) {
         buttons << get_buttons()
         anyPoll()
      }
      _avgFrames = (pollFrames.mem.reduce(_+_).toFloat / pollFrames.size)
      update()
      color.set()
      update_leds()
   }

   def rawAccel = vec3(getAx, getAy, getAz)
   def accel = rawAccel / GSCALE
   def gyro = vec3(getGx, getGy, getGz)
   def magnet = vec3(getMx, getMy, getMz)
   def temperature = get_temperature
   def battery = get_battery
   def connection = getConnection_type

   def buttonDown(b:Int) = (buttons.now & b) > 0
   def buttonUp(b:Int)   = (buttons.now & b) == 0
   def buttonPressed(b:Int)  = (buttons.now & b) > 0  && (buttons.prev & b) == 0
   def buttonReleased(b:Int) = (buttons.now & b) == 0 && (buttons.prev & b) > 0

}
