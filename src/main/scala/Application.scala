import java.util.Date

object Application extends App {

  def configs: List[Config] = {
    List {
      Config("in1.txt", "words1.txt", "out1.txt", "trnsl.1.1.20180426T211151Z.a604ec3bb128e123.b721260a3bcf1953b85e2bd632c188328ca07796")
    }
  }

  override def main(args: Array[String]): Unit = configs.foreach(execute)

  def execute(config: Config): Unit = {
    import FileIO._

    val Config(inputFile, earlyUsingWordsFile, outputFile, key) = config

    val prepareWords = cleanAndExtractWords(readFile(inputFile))

    val earlyNotTranslatedWords = filterNotTranslatedWords(prepareWords, readFileIfNeedCreate(earlyUsingWordsFile))

    textPrint(earlyNotTranslatedWords)

    val wordsWithTranslate = earlyNotTranslatedWords.map(word => word -> YandexTranslate.translate("ru", word, key))
    val outText = wordsWithTranslate.map(outputFormattingWords) :+ dateLine(wordsWithTranslate.size)
    writeFileIfNeedCreate(outputFile, outText)
    writeFileIfNeedCreate(earlyUsingWordsFile, earlyNotTranslatedWords)

  }

  def cleanAndExtractWords(text: List[String] /*, isSortByFrequency: Boolean*/): List[String] = {
    text.flatMap { line =>
      val lineLower = line.toLowerCase
      lineLower.split("[\\d\\W]+").filter(_.length >= 2)
    }

  }

  def filterNotTranslatedWords(words: List[String], earlyUsingWords: List[String]): List[String] = {
    val setEarlyUsingWords = earlyUsingWords.toSet
    words.distinct.filterNot(setEarlyUsingWords.contains)
  }

  def outputFormattingWords(pair: (String, String)): String = pair._1 + " " + pair._2

  def dateLine(size: Int): String = "// " + new Date().toString + " Number add words = " + size


  def textPrint(text: List[String]): Unit = text.foreach(println)


}
