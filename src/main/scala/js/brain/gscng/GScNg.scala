package js.brain.gscng

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import js.brain.common.Vector

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.mutable.ParArray
import scala.util.Random
/**
  * Created by sugianto on 12/22/16.
  */

class GScNg(input_dim:Int) {
  var lambda_counter:Int=0
  var C:Array[GSCNGNeuron]=null
  var plInputSpace:PLInputSpace=new PLInputSpace

  def init(M:Int, alpha_pl:Double, initSample:Vector): Unit ={
    plInputSpace.init(alpha_pl, initSample)
    var temp=ArrayBuffer[GSCNGNeuron]()
    var rndgen=new Random(System.currentTimeMillis())
    for(i<-0 until M){
      val ff_ws=Array.fill(input_dim)(0d)
      for(j<-0 until input_dim){
        ff_ws(j)=rndgen.nextDouble()
      }
      var v_ff_ws=Vector(ff_ws)
      v_ff_ws.makeUnity()

      val n=GSCNGNeuron(i, v_ff_ws)
      temp.append(n)
    }
    C=temp.toArray
  }

  def getValue(r:Double, max_radius:Double):Double={
    var df=r/(max_radius+1)
    return (1-df)
  }

  def clone_C():Array[GSCNGNeuron]={
    C.map(n=>{
      val neuron_r=GSCNGNeuron(n.index, n.w.VectorClone())
      neuron_r.dotprod_quadrat=n.dotprod_quadrat
      neuron_r
    })
  }

  def ordering(e_U:Vector, R:Array[GSCNGNeuron], U:mutable.HashMap[Int, GSCNGNeuron]): Array[GSCNGNeuron] ={
    var temp=R.map(r=>{
      var dot=r.w.mul(e_U).sum()
      dot=dot*dot
      var d= dot
      r.dotprod_quadrat=d
      r
    })
    val r_dot_e_U=temp.filter(r=>{
      if(U.getOrElse(r.index, null)==null){
        true
      }else{
        false
      }
    })
    var sorted=r_dot_e_U.sortWith( -_.dotprod_quadrat<= -_.dotprod_quadrat)
    return sorted
  }


  def update_C_R(r:GSCNGNeuron, c:GSCNGNeuron, e_U:Vector, alpha:Double, radius:Double, max_radius:Double): Unit ={
    var y=r.w.mul(c.w)
    var dlk1=e_U.sub(y.mul(r.w))
    var dlk2=y.mul(dlk1)
    dlk2.muli(alpha*getValue(radius, max_radius))
    c.w.addi(dlk2)
    r.w.addi(dlk2)
  }

  def findMax(input:Vector, Basis:Array[GSCNGNeuron], U:mutable.HashMap[Int, GSCNGNeuron]):GSCNGNeuron={
    val base_notin_U=Basis.filter(r=>{
      if(U.getOrElse(r.index, null)==null){
        true
      }else{
        false
      }
    })

    var maxindex=0
    var maxvalue=0d
    for(i<-0 until base_notin_U.length){
      var dot=base_notin_U(i).w.mul(input).sum()
      var d= dot*dot
      if(d>maxvalue){
        maxindex=i
        maxvalue=d
      }
    }
    var ret=base_notin_U(maxindex)
    ret.dotprod_quadrat=maxvalue
    ret
  }

  def update_e_U(e_U:Vector, r_win:GSCNGNeuron):Unit={
    var temp1=r_win.w.mul(e_U).sum()
    var temp2=r_win.w.mul(temp1)
    e_U.subi(temp2)
  }

  def remove_proj_R(R:Array[GSCNGNeuron], U:mutable.HashMap[Int, GSCNGNeuron], r_win:GSCNGNeuron): Unit ={
    val r_notin_U=R.filter(r=>{
      if(U.getOrElse(r.index, null)==null){
        true
      }else{
        false
      }
    })
    r_notin_U.foreach(r=>{
      var temp1=r_win.w.mul(r.w).sum()
      var temp2=r_win.w.mul(temp1)
      r.w.subi(temp2)
    })
  }

  def inference(x:Vector, K:Int): Unit ={
    val U=new mutable.HashMap[Int, GSCNGNeuron]()
    val R=clone_C()
    var e_U=x.VectorClone()
    for(h<-0 until K){
      val l_win=findMax(e_U, R, U)
      update_e_U(e_U, l_win)
      remove_proj_R(R,U, l_win)
      U.put(l_win.index, l_win)
      println("index", l_win.index,"a",math.sqrt(l_win.dotprod_quadrat), "e_U", e_U.magnitude())
    }
  }

  def online_learning(x:Vector, K:Int, alpha_lr:Double, beta:Double): Unit ={
    lambda_counter=lambda_counter+1
    var lr=plInputSpace.compute_learningrate()
    val alpha_lr_lr=alpha_lr*lr
    var radius=plInputSpace.compute_radius(lr, beta)
    val U=new mutable.HashMap[Int, GSCNGNeuron]()
    C.foreach(n=>{n.w.makeUnity()})
    val R=clone_C()
    var e_U=x.VectorClone()

    for(h<-0 until K){
      try {
        var ordered_r = ordering(e_U, R, U).take(math.round(radius).toInt)
        for (k <- 0 until ordered_r.length) {
          var r = ordered_r(k)
          var c = C(r.index)
          update_C_R(r, c, e_U, alpha_lr_lr, k, ordered_r.length)
          r.w.makeUnity()
        }
        val l_win = findMax(e_U, R, U)
        update_e_U(e_U, l_win)
        remove_proj_R(R, U, l_win)
        U.put(l_win.index, l_win)
      }catch{
        case e:Exception=>{
          e.printStackTrace()
        }
      }

    }
    plInputSpace.add(x, e_U)
    if(lambda_counter%100==0) {
      lambda_counter=0
      println("inputspace",plInputSpace.InputMagnitude,"residual magnitude", plInputSpace.ResidualMagnitude, "alpha", lr, "radius", radius)
    }
  }

}

object GScNg{
  def save(filename: String, network: GScNg): Unit = {
    try {
      val fout = new FileOutputStream(filename);
      val oos = new ObjectOutputStream(fout);
      oos.writeObject(network);
      oos.close();
    } catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }
  }

  def load(filename: String): GScNg = {
    try {
      val fin = new FileInputStream(filename);
      val ois = new ObjectInputStream(fin);
      var ret = ois.readObject().asInstanceOf[GScNg];
      ois.close();
      return ret;
    } catch {
      case e: Exception => {
        e.printStackTrace()
        return null
      }
    }
  }
}