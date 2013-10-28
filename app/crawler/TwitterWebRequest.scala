package crawler

import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.commons.io.IOUtils
import net.liftweb.json._
import play.Logger

class AuthData(val accessToken: String, val accessSecret: String, val consumerKey: String, val consumerSecret: String)

abstract class TwitterRestRequest(implicit val authData: AuthData) {
  val requestString: String

  
  lazy val rawJson = IOUtils.toString(rawResponse.getEntity.getContent)
  lazy val json = parse(rawJson)
  def encodeStrings(string: String): String =
    string.replaceAll("#", "%23").replaceAll(" ", "%20").replaceAll("@", "%40")

  private lazy val requestEncodedString = encodeStrings(requestString)
  private lazy val request: HttpGet = new HttpGet(requestEncodedString)

  def rawResponse = {
    val consumer = new CommonsHttpOAuthConsumer(authData.consumerKey, authData.consumerSecret)
    consumer.setTokenWithSecret(authData.accessToken, authData.accessSecret)
    consumer sign request
    val client = new DefaultHttpClient
    client execute request
  }

}