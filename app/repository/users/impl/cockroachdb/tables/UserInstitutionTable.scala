package repository.users.impl.cockroachdb.tables

import domain.users.UserInstitution
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape
import util.connections.PgDBConnection
import util.connections.PgDBConnection.driver

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class UserInstitutionTable(tag: Tag) extends Table[UserInstitution](tag, "user_institution") {
  def userInstitutionId: Rep[String] = column[String]("user_institution_id", O.PrimaryKey)

  def name: Rep[String] = column[String]("name")

  def * : ProvenShape[UserInstitution] = (userInstitutionId, name) <> ((UserInstitution.apply _).tupled, UserInstitution.unapply)
}

object UserInstitutionTable extends TableQuery(new UserInstitutionTable(_)) {
  def db: driver.api.Database = PgDBConnection.db

  def getEntity(userInstitutionId: String): Future[Option[UserInstitution]] = {
    db.run(this.filter(_.userInstitutionId === userInstitutionId).result).map(_.headOption)
  }

  def saveEntity(userInstitution: UserInstitution): Future[Option[UserInstitution]] = {
    db.run(
      (this returning this).insertOrUpdate(userInstitution)
    )
  }

  def getEntities: Future[Seq[UserInstitution]] = {
    db.run(UserInstitutionTable.result)
  }

  def deleteEntity(userInstitutionId: String): Future[Int] = {
    db.run(this.filter(_.userInstitutionId === userInstitutionId).delete)
  }

  def createTable = {
    db.run(
      UserInstitutionTable.schema.createIfNotExists
    ).isCompleted
  }

}