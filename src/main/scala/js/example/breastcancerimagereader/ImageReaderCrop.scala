package js.example.breastcancerimagereader

import java.io.File
import javax.imageio.ImageIO

import js.brain.common.{MatrixVector,Vector}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random


/**
  * Created by sugianto on 11/30/16.
  */
class ImageReaderCrop(left:Int, right:Int, bottom:Int, top:Int) {
  var listImageFilenames: Array[String] = null
  var listClassLabel:Array[Int]=null
  val rndgen=new Random(System.currentTimeMillis())
  var baseimagefolder:String=null

  def size():Int=listClassLabel.length

  def readRGBImage(imgfile: File): MatrixVector = {
    val ret=new MatrixVector(right-left, top-bottom)
    val img = ImageIO.read(imgfile)
    val r = ArrayBuffer[Double]()
    val g = ArrayBuffer[Double]()
    val b = ArrayBuffer[Double]()

    for {w1 <- (left until right).toVector
         h1 <- (bottom until top).toVector
    } yield {
       val col = img.getRGB(w1, h1)
      val red = (col & 0xff0000) / 65536
      val green = (col & 0xff00) / 256
      val blue = (col & 0xff)
      val v=Vector(Array(red.toDouble, green.toDouble, blue.toDouble))
      ret.put(w1-left,h1-bottom, v)
    }
    ret
  }


  def initialize(imagefolder: String): Unit = {
    baseimagefolder=imagefolder
    val f = new File(imagefolder)
    val listfiles = f.listFiles()
    val temp_name=ArrayBuffer[String]()
    val temp_class=ArrayBuffer[Int]()
    for(i<-0 until listfiles.length){
      val fname=listfiles(i).getName()
      temp_name.append(fname)
      temp_class.append(filename2label(fname))
    }
    listImageFilenames=temp_name.toArray
    listClassLabel=temp_class.toArray
  }

  def filename2label(filename: String): Int = {
    if (filename.startsWith("SOB_B")) {
      return 1
    } else if (filename.startsWith("SOB_M")) {
      return 2
    } else {
      return 0
    }
  }

  def loadRandomImage():(MatrixVector, Int, String)={
    val rndnumber=rndgen.nextInt(listClassLabel.length)
    return loadImage(rndnumber)
  }

  def loadImage(index:Int):(MatrixVector, Int, String)={
    val m=readRGBImage(new File(baseimagefolder+"/"+listImageFilenames(index)))
    val classlabel=filename2label(listImageFilenames(index))
    return (m,classlabel, listImageFilenames(index))
  }
}

object ImageReaderCrop{
  def main(args: Array[String]): Unit = {
    val ir=new ImageReaderCrop(150, 550, 30, 430)
    ir.initialize("/data/neocortexid/breast/mkfold/fold1/train/100X")
    val (m,c, n)=ir.loadRandomImage()
    println(m)
    println("========================================")
    println(c)
  }
}
