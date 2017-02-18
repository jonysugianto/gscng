package js.brain.gscng

/**
  * Created by sugianto on 12/5/16.
  */

import js.brain.common.{MatrixVector, Vector}
import org.nd4j.linalg.api.ndarray.INDArray

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object InputConvProcessor {
  val rndgen = new Random(System.currentTimeMillis())

  def mergeMatrixVector(r: MatrixVector, g: MatrixVector, b: MatrixVector): MatrixVector = {
    val ret = new MatrixVector(r.row, r.col)
    for (i <- 0 until r.row) {
      for (j <- 0 until r.col) {
        var mdata = ArrayBuffer[Double]()
        mdata.appendAll(r.get(i, j).data)
        mdata.appendAll(g.get(i, j).data)
        mdata.appendAll(b.get(i, j).data)
        val newvector = Vector(mdata.toArray)
        ret.put(i, j, newvector)
      }
    }
    return ret
  }

  def createConvolutionInputs(input: INDArray,
                              input_w: Int, input_h: Int, kernel_w: Int, kernel_h: Int,
                              stride_w: Int, stride_h: Int): MatrixVector = {
    var targetlength_x = ((input_w - kernel_w + 1).toDouble / stride_w.toDouble).toInt
    var targetlength_y = ((input_h - kernel_h + 1).toDouble / stride_h.toDouble).toInt
    var ret = new MatrixVector(targetlength_x, targetlength_y)

    for (i <- 0 until targetlength_x) {
      for (j <- 0 until targetlength_y) {
        var tileInput = ArrayBuffer[Double]()
        for (k <- 0 until kernel_w) {
          for (l <- 0 until kernel_h) {
            tileInput.append(input.getDouble(i * stride_w + k, j * stride_h + l))
          }
        }
        var tileInputVector = Vector(tileInput.toArray)
        ret.put(i, j, tileInputVector)
      }
    }
    ret
  }

  def createConvolutionInputs(raw_input: MatrixVector,
                              input_w: Int, input_h: Int, kernel_w: Int, kernel_h: Int,
                              stride_w: Int, stride_h: Int): MatrixVector = {
    var targetlength_x = ((input_w - kernel_w + 1).toDouble / stride_w.toDouble).toInt
    var targetlength_y = ((input_h - kernel_h + 1).toDouble / stride_h.toDouble).toInt
    var ret = new MatrixVector(targetlength_x, targetlength_y)

    for (i <- 0 until targetlength_x) {
      for (j <- 0 until targetlength_y) {
        var tileInput = ArrayBuffer[Double]()
        for (k <- 0 until kernel_w) {
          for (l <- 0 until kernel_h) {
            tileInput.appendAll(raw_input.get(i * stride_w + k, j * stride_h + l).data)
          }
        }
        var tileInputVector = Vector(tileInput.toArray)
        ret.put(i, j, tileInputVector)
      }
    }
    ret
  }

  def getRandomConvData(raw_input: MatrixVector,
                        input_w: Int, input_h: Int, kernel_w: Int, kernel_h: Int,
                        stride_w: Int, stride_h: Int): Vector = {
    var targetlength_x = ((input_w - kernel_w + 1).toDouble / stride_w.toDouble).toInt
    var targetlength_y = ((input_h - kernel_h + 1).toDouble / stride_h.toDouble).toInt
    var i = rndgen.nextInt(targetlength_x)
    var j = rndgen.nextInt(targetlength_y)
    var tileInput = ArrayBuffer[Double]()
    for (k <- 0 until kernel_w) {
      for (l <- 0 until kernel_h) {
        tileInput.appendAll(raw_input.get(i * stride_w + k, j * stride_h + l).data)
      }
    }
    var tileInputVector = Vector(tileInput.toArray)
    tileInputVector
  }

  def getConvDataAtPos(raw_input: MatrixVector, pos_r:Int, pos_c:Int,
                        kernel_w: Int, kernel_h: Int,
                        stride_w: Int, stride_h: Int): Vector = {
    var tileInput = ArrayBuffer[Double]()
    for (k <- 0 until kernel_w) {
      for (l <- 0 until kernel_h) {
        tileInput.appendAll(raw_input.get(pos_r * stride_w + k, pos_c * stride_h + l).data)
      }
    }
    var tileInputVector = Vector(tileInput.toArray)
    tileInputVector
  }
}
