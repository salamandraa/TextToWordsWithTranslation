import java.io._

import scala.io.Source
import scala.util.Try

object FileIO {

  def readFile(fileName: String): List[String] = Source.fromFile(fileName).getLines().toList

  def readFileIfNeedCreate(fileName: String): List[String] = {
    Try(readFile(fileName)).recover {
      case _: FileNotFoundException =>
        val file = new File(fileName)
        file.createNewFile()
        readFile(fileName)
    }.get
  }


  private def writeFile(fileName: String, text: List[String], isAppend: Boolean): Unit = {
    val out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName, isAppend)))
    text.foreach(out.println)
    out.close()
  }

  def writeFileIfNeedCreate(fileName: String, text: List[String], isAppend: Boolean = true): Unit = {
    Try(writeFile(fileName, text, isAppend)).recover {
      case _: FileNotFoundException =>
        val file = new File(fileName)
        file.createNewFile()
        writeFile(fileName, text, isAppend)
    }.get
  }
}
