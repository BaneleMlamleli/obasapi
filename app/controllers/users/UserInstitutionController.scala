package controllers.users

import controllers.ApiResponse
import domain.users.UserInstitution
import javax.inject.Inject
import io.circe.generic.auto._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import services.login.LoginService
import services.users.UserInstitutionService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserInstitutionController @Inject()
(cc: ControllerComponents, api: ApiResponse) extends AbstractController(cc){
  type DomainObject = UserInstitution

  def className: String = "UserInstitutionController"
  def domainService: UserInstitutionService = UserInstitutionService.roach
  def loginService: LoginService = LoginService.apply


  def create: Action[JsValue] = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val entity = Json.fromJson[DomainObject](request.body).asEither
      entity match {
        case Right(value) =>
          val response: Future[Option[UserInstitution]] = for {
            results: Option[UserInstitution] <- domainService.saveEntity(value)
          } yield results
          api.requestResponse[Option[UserInstitution]](response, className)
        case Left(error) => api.errorResponse(error, className)
      }
  }

  def update: Action[JsValue] = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val entity = Json.fromJson[DomainObject](request.body).asEither
      entity match {
        case Right(value) =>
          val response: Future[Option[UserInstitution]] = for {
            _ <- loginService.checkLoginStatus(request)
            results: Option[UserInstitution] <- domainService.saveEntity(value)
          } yield results
          api.requestResponse[Option[UserInstitution]](response, className)
        case Left(error) => api.errorResponse(error, className)
      }
  }

  def getUserInstitutions(userId: String): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val response: Future[Seq[DomainObject]] = for {
        results <- domainService.getEntitiesForUser(userId)
      } yield results
      api.requestResponse[Seq[DomainObject]](response, className)
  }

  def getAllUserInstitution: Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val response: Future[Seq[DomainObject]] = for {
        results <- domainService.getEntities
      } yield results
      api.requestResponse[Seq[DomainObject]](response, className)
  }

  def deleteUserInstitution: Action[JsValue] = Action.async(parse.json) {
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