package js.brain.common

/**
  * Created by sugianto on 12/16/16.
  */
object ActivationFunction {

  def sigmoid(input:Double):Double={
    var temp=1.0+math.exp(-input)
    return 1.0 /temp
  }

}
