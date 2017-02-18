package js.brain.gscng

/**
  * Created by sugianto on 12/13/16.
  */

import js.brain.common.Vector


class PLInputSpace extends Serializable{
  var InputMagnitude:Double=0
  var ResidualMagnitude:Double=0
  var alpha:Double=0

  def init(a:Double, v:Vector): Unit ={
    alpha=a
    InputMagnitude=v.magnitude()
    ResidualMagnitude=v.magnitude()
  }

  def add(input:Vector, residual:Vector): Unit ={
    InputMagnitude=(1-alpha)*InputMagnitude+input.magnitude()*alpha
    ResidualMagnitude=(1-alpha)*ResidualMagnitude+residual.magnitude()*alpha
  }

  def compute_learningrate():Double={
    math.min(ResidualMagnitude/InputMagnitude, 1)
  }

  def compute_radius(learningrate:Double, beta:Double):Double={
    var temp=1+learningrate*(math.E-1)
    var ret=beta*math.log(temp)
    return ret
  }
}