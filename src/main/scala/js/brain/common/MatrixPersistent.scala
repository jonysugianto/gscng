package js.brain.common

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import js.brain.common.MatrixVector

/**
  * Created by sugianto on 12/5/16.
  */
object MatrixPersistent {
  def save(filename: String, data: MatrixVector): Unit = {
    try {
      val fout = new FileOutputStream(filename);
      val oos = new ObjectOutputStream(fout);
      oos.writeObject(data);
      oos.close();
    } catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }
  }

  def load(filename: String): MatrixVector = {
    try {
      val fin = new FileInputStream(filename);
      val ois = new ObjectInputStream(fin);
      var ret = ois.readObject().asInstanceOf[MatrixVector];
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
