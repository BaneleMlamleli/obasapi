package repository.application

import domain.application.ApplicationStatus
import repository.Repository
import repository.application.impl.cockcroachdb.ApplicationStatusRepositoryImpl

trait ApplicationStatusRepository extends Repository[ApplicationStatus]{

}
object ApplicationStatusRepository{
  def roach: ApplicationStatusRepository = new ApplicationStatusRepositoryImpl()
}