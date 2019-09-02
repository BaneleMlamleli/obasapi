package controllers.users

import controllers.ApiResponse
import domain.users.UserRole
import javax.inject.Inject
import io.circe.generic.auto._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import services.login.LoginService
import services.users.UserRoleService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserRoleController @Inject()
(cc: ControllerComponents, api: ApiResponse) extends AbstractController(cc){
  type DomainObject = UserRole

  def className: String = "UserRoleController"
  def domainService: UserRoleService = UserRoleService.roach
  def loginService: LoginService = LoginService.apply


  def create: Action[JsValue] = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val entity = Json.fromJson[DomainObject](request.body).asEither
      entity match {
        case Right(value) =>
          val response: Future[Option[UserRole]] = for {
            results: Option[UserRole] <- domainService.saveEntity(value)
          } yield results
          api.requestResponse[Option[UserRole]](response, className)
        case Left(error) => api.errorResponse(error, className)
      }
  }

  def update: Action[JsValue] = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val entity = Json.fromJson[DomainObject](request.body).asEither
      entity match {
        case Right(value) =>
          val response: Future[Option[UserRole]] = for {
            _ <- loginService.checkLoginStatus(request)
            results: Option[UserRole] <- domainService.saveEntity(value)
          } yield results
          api.requestResponse[Option[UserRole]](response, className)
        case Left(error) => api.errorResponse(error, className)
      }
  }

  def getUserRoleById(userRoleId: String): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val response: Future[Option[DomainObject]] = for {
        results <- domainService.getEntity(userRoleId)
      } yield results
      api.requestResponse[Option[DomainObject]](response, className)
  }

  def getAllUserRole: Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val response: Future[Seq[DomainObject]] = for {
        results <- domainService.getEntities
      } yield results
      api.requestResponse[Seq[DomainObject]](response, className)
  }

  def deleteUserRole: Action[JsValue] = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val entity = Json.fromJson[DomainObject](request.body).asEither
      entity match {
        case Right(value) =>
          val response: Future[Boolean] = for {
            results: Boolean <- domainService.deleteEntity(value)
          } yield results
          api.requestResponse[Boolean](response, className)
        case Left(error) => api.errorResponse(error, className)
      }
  }
}
