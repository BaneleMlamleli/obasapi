package repository.demographics

import domain.demographics.Race
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration._


class RaceRepositoryTest extends FunSuite {

  val entity = Race("1","AFRICAN")
  val repository = RaceRepository
  test("createEntity"){
    val result = Await.result(repository.roach.saveEntity(entity), 2 minutes)
    assert(result.nonEmpty)

  }

  test("readEntity"){
    val result = Await.result(repository.roach.getEntity(entity.raceId), 2 minutes)
    assert(result.head.raceId==entity.raceId)
  }

  test("getEntities"){
    val result = Await.result(repository.roach.getEntities, 2 minutes)
    assert(result.nonEmpty)
  }

  test("updateEntity") {
    val result = Await.result(repository.roach.saveEntity(entity), 2 minutes)
    assert(result.isEmpty)

  }


  test("deleteEntities"){
    Await.result(repository.roach.deleteEntity(entity), 2 minutes)
    val result = Await.result(repository.roach.getEntity(entity.raceId), 2 minutes)
    assert(result.isEmpty)
  }


}
