package repository.users.impl.cockroachdb.tables

import java.time.LocalDateTime

import domain.login.ChangePassword
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape
import util.connections.PgDBConnection
import util.connections.PgDBConnection.driver

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Used for DDL (to create table in DB) with composite key
 * @param tag
 */
class UserChangePasswordTableCreate(tag: Tag) extends Table[ChangePassword](tag, "user_change_password") {
  def userId: Rep[String] = column[String]("user_id")

  def oldPassword: Rep[String] = column[String]("old_password")

  def newPassword: Rep[String] = column[String]("new_password")

  def dateTimeChanged: Rep[LocalDateTime] = column[LocalDateTime]("datetime_changed")

  override def * : ProvenShape[ChangePassword] = (userId, oldPassword, newPassword, dateTimeChanged) <> ((ChangePassword.apply _).tupled, ChangePassword.unapply)

  def pk = primaryKey("pk_user_change_password", (userId, dateTimeChanged))
}

object UserChangePasswordTableCreate extends TableQuery(new UserChangePasswordTableCreate(_)) {
  def db: driver.api.Database = PgDBConnection.db

  def createTable: Boolean = {
    db.run(
      UserChangePasswordTableCreate.schema.createIfNotExists
    ).isCompleted
  }

}

/**
 * Used for DML
 * @param tag
 */
class UserChangePasswordTable(tag: Tag) extends Table[ChangePassword](tag, "user_change_password")  {

  def userId: Rep[String] = column[String]("user_id", O.PrimaryKey)

  def oldPassword: Rep[String] = column[String]("old_password")

  def newPassword: Rep[String] = column[String]("new_password")

  def dateTimeChanged: Rep[LocalDateTime] = column[LocalDateTime]("datetime_changed", O.PrimaryKey)

  override def * : ProvenShape[ChangePassword] = (userId, oldPassword, newPassword, dateTimeChanged) <> ((ChangePassword.apply _).tupled, ChangePassword.unapply)
}

object UserChangePasswordTable extends TableQuery(new UserChangePasswordTable(_)) {
  def db: driver.api.Database = PgDBConnection.db

  def getEntity(userId: String): Future[Option[ChangePassword]] = {
    db.run(this.filter(_.userId === userId).result).map(_.headOption)
  }

  def saveEntity(changePassword: ChangePassword): Future[Option[ChangePassword]] = {
    db.run(
      (this returning this).insertOrUpdate(changePassword)
    )
  }

  def getEntities: Future[Seq[ChangePassword]] = {
    db.run(UserChangePasswordTable.result)
  }
}
