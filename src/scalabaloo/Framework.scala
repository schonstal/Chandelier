package scalabaloo

import collection.mutable.ArrayBuffer
import psmove.psmoveapi
import maackle.stateful.Stateful
import maackle.util

abstract class Framework(val dt:Double) {

   private var lastT = 0.0
   val dt_ms = dt*1000 toInt
   val controllers = ArrayBuffer[MoveController]()

   var frames = 0

   def +=(m:MoveController) { controllers += m }

   var _exit:Boolean = false
   def exit() { _exit = true }

   def run() {
      while(!_exit) {
         for(c <- controllers) {
            c.tick()
         }
         while(util.getSeconds - lastT < dt) {
            Thread.sleep(1)
         }
         lastT = util.getSeconds
         frames += 1
      }
   }

}
