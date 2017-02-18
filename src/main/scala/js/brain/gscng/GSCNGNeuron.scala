package js.brain.gscng

import js.brain.common.Vector
/**
  * Created by sugianto on 12/23/16.
  */
case class GSCNGNeuron(val index:Int, val w:Vector) {
  var dotprod_quadrat:Double=0
}
