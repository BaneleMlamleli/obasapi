package services.security.impl.cockcroachdb

import java.time.LocalDateTime

import domain.security.ApiKeys
import org.apache.commons.lang3.RandomStringUtils
import org.jose4j.jwk.{EcJwkGenerator, EllipticCurveJsonWebKey, JsonWebKey, PublicJsonWebKey}
import org.jose4j.keys.EllipticCurves
import repository.security.ApiKeysRepository
import services.security.ApiKeysService
import util.APPKeys

import scala.concurrent.Future

class ApiKeysServiceImpl extends ApiKeysService{
  override def saveEntity(entity: ApiKeys): Future[Option[ApiKeys]] = {
    ApiKeysRepository.apply.saveEntity(entity)
  }

  override def getEntities: Future[Seq[ApiKeys]] = {
    ApiKeysRepository.apply.getEntities
  }

  override def getEntity(id: String): Future[Option[ApiKeys]] = {
    ApiKeysRepository.apply.getEntity(id)
  }

  override def deleteEntity(entity: ApiKeys): Future[Boolean] = {
    ApiKeysRepository.apply.deleteEntity(entity)
  }

  override def createTable: Future[Boolean] = {
    ApiKeysRepository.apply.createTable
  }

  override def generateResetToken(): String = {
    val length: Int = 32
    val useLetters: Boolean = true
    val useNumbers: Boolean = true
    RandomStringUtils.random(length, useLetters, useNumbers).toLowerCase
  }

  override def getPublicJsonWebKey(publicApiKey: Option[ApiKeys]): PublicJsonWebKey = {
    val key: String = publicApiKey match {
      case Some(apiKeys: ApiKeys) =>  apiKeys.value
      case None => ""
    }
    PublicJsonWebKey.Factory.newPublicJwk(key)
  }

  override def generateJsonPublicKey(phrase: String): String = {
    val key: EllipticCurveJsonWebKey = EcJwkGenerator.generateJwk(EllipticCurves.P256)
    key.setKeyId(phrase)
    key.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE)
  }

  override def initKey: Future[Option[ApiKeys]] = {
    val key = generateJsonPublicKey(APPKeys.PUBLICKEY)
    val keys = ApiKeys(APPKeys.PUBLICKEY,key,APPKeys.ACTIVE, LocalDateTime.now())
    saveEntity(keys)
  }

}
