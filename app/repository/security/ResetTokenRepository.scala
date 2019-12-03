package repository.security

import domain.security.ResetToken
import repository.Repository
import repository.security.impl.cockroach.ResetTokenRepositoryImpl


trait  ResetTokenRepository extends Repository[ResetToken]{

}

object ResetTokenRepository{
  def apply: ResetTokenRepository = new ResetTokenRepositoryImpl()
}

