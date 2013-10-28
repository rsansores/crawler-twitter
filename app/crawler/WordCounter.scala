package crawler

import java.text.BreakIterator
import java.util.Locale

/*
 * Little modification from code obtained from
 * http://tuxscala.blogspot.mx/2010/02/breaking-text-into-words.html
 */
class WordCounter(text: String)(implicit langCountry: (String,String)) {

  val currentLocale = new Locale(langCountry._1, langCountry._2)
  val wordIterator = BreakIterator.getWordInstance(currentLocale)

  class BreakIt(target: String, bi: BreakIterator) extends Iterator[String] {
    bi.setText(target)
    private var start = bi.first
    private var end = bi.next
    def hasNext = end != BreakIterator.DONE
    def next = {
      val result = target.substring(start, end)
      start = end
      end = bi.next
      result
    }
  }

  object Extract {
    def words(target: String, wordIterator: BreakIterator): List[(String,Int)] =
      (new BreakIt(target, wordIterator)).map(word =>
        if (word(0) isLetterOrDigit) Option(word) else None).toList.flatten.groupBy(x => x).map { case (x, y) => (x, y.size) }.toList

  }

  def extractWords = Extract.words(text, wordIterator)
}