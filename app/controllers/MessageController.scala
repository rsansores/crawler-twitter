package controllers

import play.api.mvc.{ Action, Controller }
import play.api.libs.json.Json
import play.api.Routes
import crawler.TwitterCrawler
import models.Words
import play.api.GlobalSettings
import play.api.db.slick.Config.driver.simple._
import play.api.Application
import play.api.Play.current
import views.html.index
import play.Logger
import play.api.data._
import play.api.data.Forms._
import java.text.SimpleDateFormat

case class Message(value: String)

object MessageController extends Controller {

  implicit val fooWrites = Json.writes[Message]

  def index = Action { implicit request =>
    TwitterCrawler.start
    play.api.db.slick.DB.withSession {
      implicit session: Session =>
        val words = for (word <- Words) yield word
        Ok(views.html.index(words.sortBy(_.word_count * -1).take(100).list))
    }
  }

  val form = Form("date" -> text)
  def submit = Action { implicit request =>
    play.api.db.slick.DB.withSession {
      implicit session: Session =>
        val sdf = new SimpleDateFormat("dd/MM/yyyy")
        try {
          val date = new java.sql.Date(sdf.parse(form.bindFromRequest.get).getTime())
          val words = for (word <- Words if(word.in_day === date)) yield word
          Ok(views.html.index(words.sortBy(_.word_count * -1).take(100).list))
        } catch {
          case e: Exception => Ok(views.html.index(Nil))
        }
    }
  }

}