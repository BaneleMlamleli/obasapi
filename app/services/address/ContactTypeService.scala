package services.address

import domain.address.ContactType
import services.CrudService
import services.address.ContactTypeService
import services.address.Impl.ContactTypeServiceImpl

trait ContactTypeService extends CrudService [ContactType ]{


}

  object ContactTypeService{

    def apply: ContactTypeServiceImpl = new ContactTypeServiceImpl()

  }