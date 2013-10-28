package crawler

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.commons.io.IOUtils
import org.apache.http.HttpResponse
import play.Logger
import models._
import play.api.GlobalSettings
import play.api.db.slick.Config.driver.simple._
import play.api.Application
import play.api.Play.current
import java.sql.Date

object TwitterCrawler {
  val crawler = new Thread(new Crawler)
  def start = if (!crawler.isAlive) crawler.start
}

private class Crawler extends Runnable {

  val STARVATION_SLEEP_TIME = 15 * 60 * 1000 //15 minutes
  val LOOP_TIME = 5 * 1000 //5 seconds

  val topics = List("Aguascalientes", "Morelos", "Cuernavaca",
    "BajaCalifornia", "Mexicali", "Nayarit", "Tepic", "La Paz", "Nuevo León", "Monterrey",
    "Campeche", "Oaxaca", "Coahuila", "Saltillo", "Puebla", "Colima", "Queretaro", "Chiapas",
    "Tuxtla", "Quintana Roo", "Chetumal", "Chihuahua", "San Luis Potosí", "DistritoFederal", "Mexico",
    "Sinaloa", "Culiacán", "Durango", "Durango", "Sonora", "Hermosillo",
    "Guanajuato", "Tabasco", "Villahermosa", "Guerrero", "Chilpancingo", "Tamaulipas", "Ciudad Victoria",
    "Hidalgo", "Pachuca", "Tlaxcala", "Jalisco", "Guadalajara", "Veracruz", "Xalapa",
    "Toluca", "Yucatán", "Mérida", "Michoacán", "Morelia", "Zacatecas")

  implicit val authData = new AuthData("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
    "YYYYYYYYYYYYYYYYYYYYYYYYYYYY",
    "ZZZZZZZZZZZZZZZZZZZZZZZZZZZ",
    "WWWWWWWWWWWWWWWWWWWWWWWWWWWWW")
  implicit val langCountry = ("es", "MX")

  def searchInTopics(modifyString: String => String) = topics.foreach(topic => {
    Logger.debug("CRAWLING TWEET...." + modifyString(topic))
    TwitterSearch(modifyString(topic)).tweets.foreach(insertTweet(_))
    checkQuota
  })

  def insertTweet(t: Tweet) = play.api.db.slick.DB.withSession {
    implicit session: Session =>
      try {
        Tweets.insert(t)
        new WordCounter(t.text).extractWords.filterNot { case (w, c) => topics.contains(w) || w.length < 4 || w.contains(".") || w.contains("http") }.foreach {
          case (w, c) =>
            val queryWordInDay = (for { word <- Words if (word.in_day === new Date(t.createdAt.getTime) && word.word === w) } yield word.word_count)
            queryWordInDay.list.headOption match {
              case Some(_) =>
                queryWordInDay.update(queryWordInDay.first + c)
                queryWordInDay.updateStatement
                queryWordInDay.updateInvoker
              case None =>
                Words.insert(Word(None, w, c, new Date(t.createdAt.getTime)))
            }
        }
      } catch { case e: Throwable => }
  }

  def checkQuota = {
    TwitterStatus().remainingTwitterSearchs match {
      case remain :: Nil if (remain > 0) =>
        Logger.debug("Remaining requests....." + remain)
        Thread sleep (LOOP_TIME)
      case _ =>
        Logger.debug("MAX QUOTA EXCEEDED, WAITING.....")
        Thread sleep (STARVATION_SLEEP_TIME)
    }
  }

  def run = {
    //main loop
    while (true) {
      //crawls twitter until the API is starved.
      searchInTopics(s => s)
      searchInTopics(s => "#" + s)

    }

  }
}
