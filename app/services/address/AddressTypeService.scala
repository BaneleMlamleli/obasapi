package services.address

import domain.address.AddressType
import services.CrudService
import services.address.Impl.cockroachdb.AddressTypeServiceImpl

trait AddressTypeService extends CrudService[AddressType] {


}

  object AddressTypeService{

    def roach: AddressTypeService = new AddressTypeServiceImpl()

  }