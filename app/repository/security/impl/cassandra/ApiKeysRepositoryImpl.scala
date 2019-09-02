package repository.security.impl.cassandra

import com.outworkers.phantom.connectors.KeySpace
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import domain.security.ApiKeys
import repository.security.ApiKeysRepository
import repository.security.impl.cassandra.tables.ApiKeysTableImpl
import util.connections.DataConnection

import scala.concurrent.Future

class ApiKeysRepositoryImpl extends ApiKeysRepository {
  override def saveEntity(entity: ApiKeys) = ???

  override def getEntities: Future[Seq[ApiKeys]] = {
    ApiKeysDatabase.apiKeysTable.getEntities
  }

  override def getEntity(id: String): Future[Option[ApiKeys]] = {
    ApiKeysDatabase.apiKeysTable.getEntity(id)
  }

  override def deleteEntity(entity: ApiKeys): Future[Boolean] = {
    ApiKeysDatabase.apiKeysTable.deleteEntity(entity.id).map(result => result.isExhausted())
  }

  override def createTable: Future[Boolean] = {
    implicit def keyspace: KeySpace = DataConnection.keySpaceQuery.keySpace

    implicit def session: Session = DataConnection.connector.session

    ApiKeysDatabase.apiKeysTable.create.ifNotExists().future().map(result => result.head.isExhausted())
  }
}


class ApiKeysDatabase(override val connector: KeySpaceDef) extends Database[ApiKeysDatabase](connector) {

  object apiKeysTable extends ApiKeysTableImpl with connector.Connector

}

object ApiKeysDatabase extends ApiKeysDatabase(DataConnection.connector)