package repository.login

import domain.login.LoginToken
import repository.Repository
import repository.login.impl.cockroach.LoginTokenRepositoryImpl


trait LoginTokenRepository extends Repository[LoginToken] {

}

object LoginTokenRepository {
  def apply: LoginTokenRepository = new LoginTokenRepositoryImpl()
}

