package controllers.demographics


import controllers.ApiResponse
import domain.demographics.Roles
import javax.inject.Inject
import io.circe.generic.auto._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import services.demographics.RoleService
import services.login.LoginService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RolesController @Inject()
(cc: ControllerComponents, api: ApiResponse) extends AbstractController(cc) {
  type DomainObject = Roles

  def className: String = "RolesController"

  def domainService: RoleService = RoleService.roach

  def loginService: LoginService = LoginService.apply

  def create: Action[JsValue] = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val entity = Json.fromJson[DomainObject](request.body).asEither
      entity match {
        case Right(value) =>
          val response: Future[Option[Roles]] = for {
            results: Option[Roles] <- domainService.saveEntity(value)
          } yield results
          api.requestResponse[Option[Roles]](response, className)
        case Left(error) => api.errorResponse(error, className)
      }
  }

  def update: Action[JsValue] = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val entity = Json.fromJson[DomainObject](request.body).asEither
      entity match {
        case Right(value) =>
          val response: Future[Option[Roles]] = for {
            _ <- loginService.checkLoginStatus(request)
            results: Option[Roles] <- domainService.saveEntity(value)
          } yield results
          api.requestResponse[Option[Roles]](response, className)
        case Left(error) => api.errorResponse(error, className)
      }
  }


  def getRoleById(id: String): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val response: Future[Option[DomainObject]] = for {
        results <- domainService.getEntity(id)
      } yield results
      api.requestResponse[Option[DomainObject]](response, className)
  }

  def getAllRoles: Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val response: Future[Seq[DomainObject]] = for {
        results <- domainService.getEntities
      } yield results
      api.requestResponse[Seq[DomainObject]](response, className)
  }

  def deleteRole: Action[JsValue] = Action.async(parse.json) {
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
