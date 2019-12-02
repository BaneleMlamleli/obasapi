package services.address

import domain.address.ContactType
import services.CrudService
import services.address.Impl.cockroachdb.ContactTypeServiceImpl

trait ContactTypeService extends CrudService [ContactType ]{


}

object ContactTypeService{
    def roach: ContactTypeService = new ContactTypeServiceImpl()

  }