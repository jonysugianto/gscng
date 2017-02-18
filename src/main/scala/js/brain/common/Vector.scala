package js.brain.common

/**
  * Created by jonysugiantohp on 14/11/16.
  */
class Vector extends Serializable{
  var data:Array[Double]=null

  override def equals(obj: scala.Any): Boolean = {
    var newv=obj.asInstanceOf[Vector]
    for(i<-0 until data.length){
      if(data(i)!=newv.data(i)){
        return false
      }
    }
    return true
  }

  override def toString: String = {
    var ret=""
    for(i<-0 until data.length){
      val d="%.2f".format(data(i))
      ret=ret+d+" "
    }
    return ret
  }

  def VectorClone():Vector ={
    Vector(data)
  }

  def copy(offset:Int, values:Array[Double]): Unit ={
    for(i<-0 until values.length) {
      data(i+offset)=values(i)
    }
  }

  def size()=data.length

  def add(v:Vector):Vector={
    var ret=new Vector
    var temp=Array.fill(size())(0d)
    for(i<-0 until temp.length){
      temp(i)=data(i)+v.data(i)
    }
    ret.data=temp
    ret
  }

  def addi(v:Vector):Vector={
    for(i<-0 until data.length){
      data(i)=data(i)+v.data(i)
    }
    this
  }

  def add(scalar:Double):Vector={
    var ret=new Vector
    var temp=Array.fill(size())(0d)
    for(i<-0 until temp.length){
      temp(i)=data(i)+scalar
    }
    ret.data=temp
    ret
  }

  def addi(scalar:Double):Vector={
    for(i<-0 until data.length){
      data(i)=data(i)+scalar
    }
    this
  }

  def sub(v:Vector):Vector={
    var ret=new Vector
    var temp=Array.fill(size())(0d)
    for(i<-0 until temp.length){
      temp(i)=data(i)-v.data(i)
    }
    ret.data=temp
    ret
  }

  def subi(v:Vector):Vector={
    for(i<-0 until data.length){
      data(i)=data(i)-v.data(i)
    }
    this
  }

  def sub(scalar:Double):Vector={
    var ret=new Vector
    var temp=Array.fill(size())(0d)
    for(i<-0 until temp.length){
      temp(i)=data(i)-scalar
    }
    ret.data=temp
    ret
  }

  def subi(scalar:Double):Vector={
    for(i<-0 until data.length){
      data(i)=data(i)-scalar
    }
    this
  }

  def mul(v:Vector):Vector={
    var ret=new Vector
    var temp=Array.fill(size())(0d)
    for(i<-0 until temp.length){
      temp(i)=data(i)*v.data(i)
    }
    ret.data=temp
    ret
  }

  def muli(v:Vector):Vector={
    for(i<-0 until data.length){
      data(i)=data(i)*v.data(i)
    }
    this
  }

  def mul(scalar:Double):Vector={
    var ret=new Vector
    var temp=Array.fill(size())(0d)
    for(i<-0 until temp.length){
      temp(i)=data(i)*scalar
    }
    ret.data=temp
    ret
  }

  def muli(scalar:Double):Vector={
    for(i<-0 until data.length){
      data(i)=data(i)*scalar
    }
    this
  }

  def sum():Double={
    var sum=0.0
    data.foreach(d=>{
      sum=sum+d
    })
    sum
  }

  def limit(low:Double, high:Double): Unit ={
    for(i<-0 until data.length){
      if(data(i)<low){
        data(i)=low
      }else if(data(i)>high){
        data(i)=high
      }
    }
  }

  def magnitude():Double={
    var tot=0d
    data.foreach(d=>{
      tot=tot+d*d
    })
    return math.sqrt(tot)
  }

  def makeUnity(): Unit ={
    val mag=magnitude()
    for(i<-0 until data.length){
      data(i)=data(i)/mag
    }
  }
}

object Vector{

  val MINIMUM_MAGNITUDE=0.000001d

  def apply(size:Int):Vector={
    var v=new Vector
    v.data=Array.fill(size)(0d)
    v
  }

  def apply(size:Int, defval:Double):Vector={
    var v=new Vector
    v.data=Array.fill(size)(defval)
    v
  }

  def apply(values:Array[Double]):Vector={
    var v=new Vector
    v.data=values.map(d=>{d})
    v
  }

  def euclidDistance(v1:Vector, v2:Vector):Double={
    var d0=v1.sub(v2)
    var dw=math.sqrt(d0.muli(d0).sum())
    dw
  }

  def cosineDistance(v1:Vector, v2:Vector):Double={
    var d0=v1.mul(v2).sum()
    var mv1=v1.magnitude()
    if(mv1<MINIMUM_MAGNITUDE){
      mv1=MINIMUM_MAGNITUDE
    }
    var mv2=v2.magnitude()
    if(mv2<MINIMUM_MAGNITUDE){
      mv2=MINIMUM_MAGNITUDE
    }
    var dw=d0/(mv1*mv2)
    dw
  }

  def mergeVector(v1:Vector, v2:Vector):Vector={
    val ret=Vector(v1.size()+v2.size())
    ret.copy(0, v1.data)
    ret.copy(v1.size(), v2.data)
    ret
  }

  def mergeVector(vs:Array[Vector]):Vector={
    var ret=mergeVector(vs(0), vs(1))
    for(i<-2 until vs.length){
      ret=mergeVector(ret, vs(i))
    }
    ret
  }
}