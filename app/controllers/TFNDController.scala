package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.{ Environment, LogoutEvent, Silhouette }
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import models.User
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.libs.json._

import scala.concurrent.Future
import java.io._
/**
 * The basic application controller.
 *
 * @param messagesApi The Play messages API.
 * @param env The Silhouette environment.
 */
class TFNDController @Inject() (
  val messagesApi: MessagesApi,
  val env: Environment[User, JWTAuthenticator]) extends Silhouette[User, JWTAuthenticator] {

  /**
   *
   * Returns the user.
   *
   * @return The result to display.
   */
  def submit = SecuredAction.async { implicit request =>
    Future.successful(Ok(JsObject(
      Seq( "Sent" -> JsString("TFND"))
    )))
  }

}
