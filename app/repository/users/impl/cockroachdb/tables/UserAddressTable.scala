package repository.users.impl.cockroachdb.tables

import domain.users.UserAddress
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape
import util.connections.PgDBConnection
import util.connections.PgDBConnection.driver

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class UserAddressTable(tag: Tag) extends Table[UserAddress](tag, "user_address") {
  def userAddressId: Rep[String] = column[String]("user_address_id", O.PrimaryKey)

  def physicalAddress: Rep[String] = column[String]("physical_address")

  def postalCode: Rep[String] = column[String]("postal_code")

  def * : ProvenShape[UserAddress] = (userAddressId, physicalAddress, postalCode) <> ((UserAddress.apply _).tupled, UserAddress.unapply)
}

object UserAddressTable extends TableQuery(new UserAddressTable(_)) {
  def db: driver.api.Database = PgDBConnection.db

  def getEntity(userAddressId: String): Future[Option[UserAddress]] = {
    db.run(this.filter(_.userAddressId === userAddressId).result).map(_.headOption)
  }

  def saveEntity(userAddress: UserAddress): Future[Option[UserAddress]] = {
    db.run(
      (this returning this).insertOrUpdate(userAddress)
    )

  }

  def getEntities: Future[Seq[UserAddress]] = {
    db.run(UserAddressTable.result)
  }

  def deleteEntity(userAddressId: String): Future[Int] = {
    db.run(this.filter(_.userAddressId === userAddressId).delete)
  }

  def createTable = {
    db.run(
      UserAddressTable.schema.createIfNotExists
    ).isCompleted
  }

}