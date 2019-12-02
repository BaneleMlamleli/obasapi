package repository.users.impl.cockroachdb

import domain.users.UserInstitution
import repository.users.impl.cockroachdb.tables.UserInstitutionTable
import repository.users.UserInstitutionRepository

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserInstitutionRepositoryImpl  extends UserInstitutionRepository{

  override def saveEntity(entity: UserInstitution): Future[Option[UserInstitution]] = {
    UserInstitutionTable.saveEntity(entity)
  }

  override def getEntities: Future[Seq[UserInstitution]] = {
    UserInstitutionTable.getEntities
  }

  override def getEntity(userInstitutionId: String): Future[Option[UserInstitution]] = {
    UserInstitutionTable.getEntity(userInstitutionId)
  }

  override def deleteEntity(entity: UserInstitution): Future[Boolean] = {
    UserInstitutionTable.deleteEntity(entity.userId)map(value=> value.isValidInt)
  }

  override def createTable: Future[Boolean] = {
    Future.successful(UserInstitutionTable.createTable)
  }
}


