package repository.users.impl.cockroachdb

import domain.users.User
import repository.users.impl.cockroachdb.tables.UserTable
import repository.users.UserRepository

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserRepositoryImpl extends UserRepository {

  override def saveEntity(entity: User): Future[Option[User]] = {
    UserTable.saveEntity(entity)
  }

  override def getEntities: Future[Seq[User]] = {
    UserTable.getEntities
  }

  override def getEntity(email: String): Future[Option[User]] = {
    UserTable.getEntity(email)
  }

  override def deleteEntity(entity: User): Future[Boolean] = {
    UserTable.deleteEntity(entity.email) map (value => value.isValidInt)
  }

  override def createTable: Future[Boolean] = {
    Future.successful(UserTable.createTable)
  }

}
