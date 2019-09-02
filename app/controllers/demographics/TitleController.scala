package controllers.demographics



import controllers.ApiResponse
import domain.demographics.Title
import javax.inject.Inject
import io.circe.generic.auto._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import services.address.AddressTypeService
import services.application.ApplicantTypeService
import services.application.Impl.cockroachdb.ApplicantTypeServiceImpl
import services.demographics.Impl.cockroachdb.TitleServiceImpl
import services.demographics.{RaceService, RoleService, TitleService}
import services.login.LoginService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future



class TitleController @Inject()
(cc: ControllerComponents, api: ApiResponse) extends AbstractController(cc) {
  type DomainObject = Title

  def className: String = "TitleController"

  def domainService: TitleService = TitleService.roach

  def loginService: LoginService = LoginService.apply

  def create: Action[JsValue] = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val entity = Json.fromJson[DomainObject](request.body).asEither
      entity match {
        case Right(value) =>
          val response: Future[Option[Title]] = for {
            results: Option[Title] <- domainService.saveEntity(value)
          } yield results
          api.requestResponse[Option[Title]](response, className)
        case Left(error) => api.errorResponse(error, className)
      }
  }

  def update: Action[JsValue] = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val entity = Json.fromJson[DomainObject](request.body).asEither
      entity match {
        case Right(value) =>
          val response: Future[Option[Title]] = for {
            _ <- loginService.checkLoginStatus(request)
            results: Option[Title] <- domainService.saveEntity(value)
          } yield results
          api.requestResponse[Option[Title]](response, className)
        case Left(error) => api.errorResponse(error, className)
      }
  }


  def getTitleById(titleId: String): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val response: Future[Option[DomainObject]] = for {
        results <- domainService.getEntity(titleId)
      } yield results
      api.requestResponse[Option[DomainObject]](response, className)
  }

  def getAllTitle: Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val response: Future[Seq[DomainObject]] = for {
        results <- domainService.getEntities
      } yield results
      api.requestResponse[Seq[DomainObject]](response, className)
  }

  def deleteTitle: Action[JsValue] = Action.async(parse.json) {
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
