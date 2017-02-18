package js.example

import js.brain.common.Vector
import js.brain.gscng.{ConvConfig, GScNg, InputConvProcessor}
import js.example.breastcancerimagereader.ImageReaderCrop

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
  * Created b    println("T:"+T)
  * y jonysugiantohp on R = findNearestNeuron(n)._2/2.0
     03/11/16.
  */
object GScNgRunner{

  def init(algo:GScNg, convconfig:ConvConfig, ir:ImageReaderCrop,
           M:Int, alpha_pl:Double): Unit ={
    val imgdata=ir.loadRandomImage()
    var initinput=InputConvProcessor.getRandomConvData(imgdata._1, convconfig.input_w, convconfig.input_h,
      convconfig.kernel_w, convconfig.kernel_h, convconfig.stride_w, convconfig.stride_h)
    algo.init(M, alpha_pl, initinput.muli(1.0/255.0))
  }

  def getAllFeatures(convconfig:ConvConfig, ir:ImageReaderCrop): Array[Vector] ={
    var targetlength_x = ((convconfig.input_w - convconfig.kernel_w + 1).toDouble / convconfig.stride_w.toDouble).toInt
    var targetlength_y = ((convconfig.input_h - convconfig.kernel_h + 1).toDouble / convconfig.stride_h.toDouble).toInt

    var ret=ArrayBuffer[Vector]()
    for(i<-0 until ir.size()){
      val imgdata=ir.loadImage(i)
      for(r<-0 until targetlength_x){
        for(c<-0 until targetlength_y){
          var tileInput = ArrayBuffer[Double]()
          for (k <- 0 until convconfig.kernel_w) {
            for (l <- 0 until convconfig.kernel_h) {
              tileInput.appendAll(imgdata._1.get(r * convconfig.stride_w + k, c * convconfig.stride_h + l).data)
            }
          }
          var tileInputVector = Vector(tileInput.toArray)
          ret.append(tileInputVector.muli(1.0/255.0))
        }
      }
     }
    println("length", ret.length)
    ret.toArray
  }


  def main(args: Array[String]): Unit = {
    var rndgen=new Random(System.currentTimeMillis())
    val baseimagefolder=args(0)
//    val baseimagefolder="/data/neocortexid/breast/mkfold/fold1/train/40X"
    val ir=new ImageReaderCrop(left=150, right=550, bottom = 30, top = 430)
    ir.initialize(baseimagefolder)
//   var conf=NGConfig.readFromJSon("/data/tmp/ng/ng.conf")
    var gscng:GScNg=null
    val convconfig=new ConvConfig(input_w=400, input_h=400, kernel_w = 5, kernel_h = 5, stride_h = 2, stride_w = 2)
    var dataset=getAllFeatures(convconfig, ir)

   // if(conf.updatetraining){
   //   nglayer1=NGAlgo.load(conf.filename)
   // }else{
    gscng=new GScNg(input_dim=75)
    var M=300
    var alpha_pl=0.001
    var K=40
    var beta=50d
    var alpha_lr=0.5
    init(gscng, convconfig, ir, M, alpha_pl)
    //}

    var start=System.currentTimeMillis()
    var step=System.currentTimeMillis()
    var size=dataset.length
    for(i<-0 until size) {
      var rndnumber=rndgen.nextInt(size)
      var data=dataset(rndgen.nextInt(dataset.length))
      gscng.online_learning(data, K, alpha_lr, beta)
      if ((i % 1000) == 0) {
        val durationstep=System.currentTimeMillis()-step
        println(i, " data processed with duration step:", durationstep.toDouble/1000.0)
        step=System.currentTimeMillis()
      }
    }
    val duration=System.currentTimeMillis()-start
    println("duration:", duration.toDouble/1000.0)

    println("=========================save network=========================")
    GScNg.save("/tmp/gscng/gscng.network", gscng)
  }
}
