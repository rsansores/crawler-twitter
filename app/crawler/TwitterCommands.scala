package crawler

import net.liftweb.json._
import models.Tweet
import java.text.SimpleDateFormat
import java.sql.Timestamp
import java.util.Locale

case class TwitterStatus(implicit override val authData: AuthData) extends TwitterRestRequest() {
  override val requestString = "https://api.twitter.com/1.1/application/rate_limit_status.json?resources=search"

  val remainingTwitterSearchs: List[Int] = for {
    JField("/search/tweets", JObject(o)) <- json
    JField("remaining", JInt(v)) <- o
  } yield v.toInt

}

case class TwitterSearch(topic: String)(implicit override val authData: AuthData) extends TwitterRestRequest() {
  val format = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy",Locale.ENGLISH)
  override val requestString = ("https://api.twitter.com/1.1/search/tweets.json?q=" + topic + "&result_type=realtime")
  
  val tweets = for { 
         JObject(tweets) <-  json \\ "statuses"
         JField("id", JInt(id)) <- tweets
         JField("text", JString(text)) <- tweets
         JField("retweet_count", JInt(retweet)) <- tweets
         JField("retweeted", JBool(retweeted)) <- tweets
         JField("created_at", JString(date)) <- tweets
  } yield new Tweet(id.toLong,text,retweet.toInt,retweeted,new Timestamp(format.parse(date).getTime))
}