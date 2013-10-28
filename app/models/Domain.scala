package models

import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp
import java.sql.Date

case class Tweet(id: Long, text: String, retweetCount: Int, retweeted: Boolean, createdAt: Timestamp)
object Tweets extends Table[Tweet]("tweets") {
  def id = column[Long]("id", O.PrimaryKey)
  def text = column[String]("text")
  def retweetCount = column[Int]("retweet_count")
  def retweeted = column[Boolean]("retweeted")
  def createdAt = column[Timestamp]("created_at")
  def * = id ~ text ~ retweetCount ~ retweeted ~ createdAt <> (Tweet, Tweet.unapply _)
}

case class Word(id: Option[Int] = None, word: String, word_count: Int, in_day: Date)
object Words extends Table[Word]("words") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def word = column[String]("word")
  def word_count = column[Int]("word_count")
  def in_day = column[Date]("in_day")
  def * = id.? ~ word ~ word_count ~ in_day <> (Word, Word.unapply _)
  def forInsert = word ~ word_count ~ in_day <> ({ t => Word(None, t._1, t._2, t._3) }, { (w: Word) => Some((w.word, w.word_count, w.in_day)) })
}