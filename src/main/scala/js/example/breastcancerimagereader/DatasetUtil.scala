package js.example.breastcancerimagereader

import java.io.File
import java.nio.file.{Files, Paths, StandardCopyOption}

import scala.util.Random

/**
  * Created by sugianto on 11/29/16.
  */
object DatasetUtil {

  def main(args: Array[String]): Unit = {
    val srcdir=args(0)
    val destdir=args(1)
    val probabilityasexample=args(2).toDouble
    val rnggen=new Random(System.currentTimeMillis())

    val fsrcdir=new File(srcdir)
    val sourcefiles=fsrcdir.listFiles()
    for(i<-0 until sourcefiles.length){
      if(rnggen.nextDouble()<probabilityasexample) {
        val fname = sourcefiles(i).getName()
        val destfname = destdir + "/" + fname
        Files.move(Paths.get(sourcefiles(i).getAbsolutePath), Paths.get(destfname),
          StandardCopyOption.REPLACE_EXISTING)
      }
    }
  }

}
