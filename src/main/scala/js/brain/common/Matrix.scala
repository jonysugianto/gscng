package js.brain.common

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

import scala.collection.mutable.ArrayBuffer

/**
  * Created by jonysugiantohp on 16/11/16.
  */
class MatrixINDArray(val row:Int, val col:Int)  extends Serializable{
  val data:Array[INDArray]=init()

  def size()=row*col

  private def init(): Array[INDArray] ={
    var m=ArrayBuffer[INDArray]()
    for(r<-0 until row){
      for(c<-0 until col){
        m.append(Nd4j.zeros(2,2))
      }
    }
    m.toArray
  }

  def get(r:Int, c:Int):INDArray={
    data(r*col+c)
  }

  def put(r:Int, c:Int, d:INDArray):Unit={
    data(r*col+c)=d
  }
}

class MatrixVector(val row:Int, val col:Int)  extends Serializable{
  val data:Array[Vector]=init()

  def size()=row*col

  private def init(): Array[Vector] ={
    var m=ArrayBuffer[Vector]()
    for(r<-0 until row){
      for(c<-0 until col){
        m.append(Vector(2))
      }
    }
    m.toArray
  }

  def get(r:Int, c:Int):Vector={
    data(r*col+c)
  }

  def put(r:Int, c:Int, d:Vector):Unit={
    data(r*col+c)=d
  }

  def flatten(): Vector ={
    var values=ArrayBuffer[Double]()
    for(r<-0 until row){
      for(c<-0 until col){
        var itemvector=get(r,c)
        var length=itemvector.size()
        for(i<-0 until length){
          values.append(itemvector.data(i))
        }
      }
    }
    Vector(values.toArray)
  }
}

class MatrixDouble(val row:Int, val col:Int)  extends Serializable{
  val data:Array[Double]=init()

  def size()=row*col

  private def init(): Array[Double] ={
    var m=ArrayBuffer[Double]()
    for(r<-0 until row){
      for(c<-0 until col){
        m.append(0d)
      }
    }
    m.toArray
  }

  def get(r:Int, c:Int):Double={
    data(r*col+c)
  }

  def put(r:Int, c:Int, d:Double):Unit={
    data(r*col+c)=d
  }
}

object Matrix{
  def mergeMatrixDoubles(input:Array[MatrixDouble]): Vector ={
    var values=ArrayBuffer[Double]()
    for(i<-0 until input.length){
      values.appendAll(input(i).data)
    }
    Vector(values.toArray)
  }
}
