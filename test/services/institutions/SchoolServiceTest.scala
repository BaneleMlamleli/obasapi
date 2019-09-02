package services.institutions

import domain.institutions.School
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration._


class SchoolServiceTest extends FunSuite{
  val entity = School("1","JvR","13 Bree Street","Western Cape","")
  val roachService = SchoolService
  test("createEntity"){
    val result = Await.result(roachService.roach.saveEntity(entity), 2 minutes)
    assert(result.nonEmpty)

  }

  test("readEntity"){
    val result = Await.result(roachService.roach.getEntity(entity.schoolId), 2 minutes)
    assert(result.head.schoolId==entity.schoolId)
  }


  test("getEntities") {
    val result = Await.result(roachService.roach.getEntities, 2 minutes)
    assert(result.nonEmpty)
  }

  test("updateEntity") {
    val result = Await.result(roachService.roach.saveEntity(entity), 2 minutes)
    assert(result.isEmpty)

  }


  test("deleteEntities"){
    Await.result(roachService.roach.deleteEntity(entity), 2 minutes)
    val result = Await.result(roachService.roach.getEntity(entity.schoolId), 2 minutes)
    assert(result.isEmpty)

  }
}
