package repository.institutions

import domain.institutions.School
import repository.Repository
import repository.institutions.impl.cockroachdb.SchoolRepositoryImpl

trait SchoolRepository extends Repository[School] {

}

object SchoolRepository{
  def roach: SchoolRepository = new SchoolRepositoryImpl()
}
