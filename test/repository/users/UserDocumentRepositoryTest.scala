package repository.users

import domain.users.UserDocument
import org.scalatest.FunSuite


import scala.concurrent.Await
import scala.concurrent.duration._


class UserDocumentRepositoryTest extends FunSuite{
  val entity = UserDocument("13","12")
  val repository = UserDocumentRepository
  test("createEntity"){
    val result = Await.result(repository.roach.saveEntity(entity), 2 minutes)
    assert(result.nonEmpty)

  }

  test("readEntity"){
    val result = Await.result(repository.roach.getEntity(entity.userId), 2 minutes)
    assert(result.head.userId==entity.userId)
  }

  test("getEntities") {
    val result = Await.result(repository.roach.getEntities, 2 minutes)
    assert(result.nonEmpty)
  }

 test("updateEntity") {
  val result = Await.result(repository.roach.saveEntity(entity), 2 minutes)
  assert(result.isEmpty)

}


  test("deleteEntities"){
    Await.result(repository.roach.deleteEntity(entity), 2 minutes)
    val result = Await.result(repository.roach.getEntity(entity.userId), 2 minutes)
    assert(result.isEmpty)

  }
}