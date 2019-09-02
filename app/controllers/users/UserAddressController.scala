package controllers.users

import controllers.ApiResponse
import domain.users.UserAddress
import javax.inject.Inject
import io.circe.generic.auto._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import services.login.LoginService
import services.users.UserAddressService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserAddressController @Inject()
(cc: ControllerComponents, api: ApiResponse) extends AbstractController(cc){
  type DomainObject = UserAddress

  def className: String = "UserAddressController"
  def domainService: UserAddressService = UserAddressService.apply
  def loginService: LoginService = LoginService.apply


  def create: Action[JsValue] = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val entity = Json.fromJson[DomainObject](request.body).asEither
      entity match {
        case Right(value) =>
          val response: Future[Option[UserAddress]] = for {
            results: Option[UserAddress] <- domainService.saveEntity(value)
          } yield results
          api.requestResponse[Option[UserAddress]](response, className)
        case Left(error) => api.errorResponse(error, className)
      }
  }

  def update: Action[JsValue] = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val entity = Json.fromJson[DomainObject](request.body).asEither
      entity match {
        case Right(value) =>
          val response: Future[Option[UserAddress]] = for {
            _ <- loginService.checkLoginStatus(request)
            results: Option[UserAddress] <- domainService.saveEntity(value)
          } yield results
          api.requestResponse[Option[UserAddress]](response, className)
        case Left(error) => api.errorResponse(error, className)
      }
  }

  def getUserAddressById(userAddressId: String): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val response: Future[Option[DomainObject]] = for {
        results <- domainService.getEntity(userAddressId)
      } yield results
      api.requestResponse[Option[DomainObject]](response, className)
  }

  def getAllUserAddress: Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      val response: Future[Seq[DomainObject]] = for {
        results <- domainService.getEntities
      } yield results
      api.requestResponse[Seq[DomainObject]](response, className)
  }

  def deleteUserAddress: Action[JsValue] = Action.async(parse.json) {
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
