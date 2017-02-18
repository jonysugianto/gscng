package js.example.breastcancerimagereader

import java.io.File

import js.brain.common.{MatrixPersistent, MatrixVector}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
  * Created by sugianto on 12/5/16.
  */
class MatrixVectorReader {
  var listImageFilenames: Array[String] = null
  var listClassLabel:Array[Int]=null
  val rndgen=new Random(System.currentTimeMillis())
  var baseimagefolder:String=null

  def size():Int=listClassLabel.length

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
    val m=MatrixPersistent.load(baseimagefolder+"/"+listImageFilenames(index))
    val classlabel=filename2label(listImageFilenames(index))
    return (m,classlabel, listImageFilenames(index))
  }
}