package repository.users.impl.cockroachdb

import domain.users.UserAddress
import repository.users.UserAddressRepository
import repository.users.impl.cockroachdb.tables.UserAddressTable

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserAddressRepositoryImpl  extends UserAddressRepository{

  override def saveEntity(entity: UserAddress): Future[Option[UserAddress]] = {
    UserAddressTable.saveEntity(entity)
  }

  override def getEntities: Future[Seq[UserAddress]] = {
    UserAddressTable.getEntities
  }

  override def getEntity(userAddressId: String): Future[Option[UserAddress]] = {
    UserAddressTable.getEntity(userAddressId)
  }

  override def deleteEntity(entity: UserAddress): Future[Boolean] = {
    UserAddressTable.deleteEntity(entity.userAddressId)map(value=> value.isValidInt)
  }

  override def createTable: Future[Boolean] = {
    Future.successful(UserAddressTable.createTable)
  }
}


