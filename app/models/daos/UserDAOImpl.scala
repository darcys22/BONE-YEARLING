package models.daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.User

import scala.collection.mutable
import scala.concurrent.Future

import javax.inject.Inject
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global

import reactivemongo.api._

import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection._
import play.modules.reactivemongo.ReactiveMongoApi

class UserDAOImpl @Inject() (val reactiveMongoApi: ReactiveMongoApi) extends UserDAO {

  val db = reactiveMongoApi.db
  def collection: JSONCollection = db.collection[JSONCollection]("user")

  def find(loginInfo: LoginInfo): Future[Option[User]] = {
    collection.find(Json.obj( "loginInfo" -> loginInfo)).one[User]
  }

  def find(userID: UUID): Future[Option[User]] = {
    collection.find(Json.obj("userID" -> userID)).one[User]
  }

  def save(user: User) = {
    collection.insert(user)
    Future.successful(user)
  }

}

