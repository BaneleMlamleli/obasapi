package repository.users.impl.cockroachdb.tables

import domain.users.UserSubjects
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape
import util.connections.PgDBConnection
import util.connections.PgDBConnection.driver

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class UserSubjectsTable(tag: Tag) extends Table[UserSubjects](tag, "user_subjects") {
  def userSubjectId: Rep[String] = column[String]("user_subject_id", O.PrimaryKey)

  def name: Rep[String] = column[String]("name")

  def description: Rep[String] = column[String]("description")

  def term: Rep[String] = column[String]("term")


  def * : ProvenShape[UserSubjects] = (userSubjectId, name, description, term) <> ((UserSubjects.apply _).tupled, UserSubjects.unapply)
}

object UserSubjectsTable extends TableQuery(new UserSubjectsTable(_)) {
  def db: driver.api.Database = PgDBConnection.db

  def getEntity(userSubjectId: String): Future[Option[UserSubjects]] = {
    db.run(this.filter(_.userSubjectId === userSubjectId).result).map(_.headOption)
  }

  def saveEntity(userSubjects: UserSubjects): Future[Option[UserSubjects]] = {
    db.run(
      (this returning this).insertOrUpdate(userSubjects)
    )
  }

  def getEntities: Future[Seq[UserSubjects]] = {
    db.run(UserSubjectsTable.result)
  }

  def deleteEntity(userSubjectId: String): Future[Int] = {
    db.run(this.filter(_.userSubjectId === userSubjectId).delete)
  }

  def createTable = {
    db.run(
      UserSubjectsTable.schema.createIfNotExists
    ).isCompleted
  }

}