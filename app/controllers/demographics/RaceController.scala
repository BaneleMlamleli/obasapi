package controllers.demographics



import controllers.ApiResponse
import domain.demographics.Race
import javax.inject.Inject
import io.circe.generic.auto._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import services.address.AddressTypeService
import services.application.ApplicantTypeService
import services.application.Impl.cockroachdb.ApplicantTypeServiceImpl
import services.demographics.Impl.cockroachdb.RaceServiceImpl
import services.demographics.{RaceService, RoleService}
import services.login.LoginService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future



class RaceController @Inject()
(cc: ControllerComponents, api: ApiResponse) extends AbstractController(cc) {
  type DomainObject = Race

  def className: String = "RaceController"

  def domainService: RaceService = RaceService.roach

  def loginService: LoginService = LoginService.apply

  def create: Action[JsValue] = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val entity = Json.fromJson[DomainObject](request.body).asEither
      entity match {
        case Right(value) =>
          val response: Future[Option[Race]] = for {
            results: Option[Race] <- domainService.saveEntity(value)
          } yield results
          api.requestResponse[Option[Race]](response, className)
        case Left(error) => api.errorResponse(error, className)
      }
  }

  def update: Action[JsValue] = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val entity = Json.fromJson[DomainObject](request.body).asEither
      entity match {
        case Right(value) =>
          val response: Future[Option[Race]] = for {
            _ <- loginService.checkLoginStatus(request)
            results: Option[Race] <- domainService.saveEntity(value)
          } yield results
          api.requestResponse[Option[Race]](response, className)
        case Left(error) => api.errorResponse(error, className)
      }
  }

  def getRaceById(raceId: String): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val response: Future[Option[DomainObject]] = for {
        results <- domainService.getEntity(raceId)
      } yield results
      api.requestResponse[Option[DomainObject]](response, className)
  }

  def getAllRace: Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val response: Future[Seq[DomainObject]] = for {
        results <- domainService.getEntities
      } yield results
      api.requestResponse[Seq[DomainObject]](response, className)
  }

  def deleteRace: Action[JsValue] = Action.async(parse.json) {
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
