package eco.stx.edao.eco.daoProperties.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eco.stx.edao.eco.daoProperties.service.domain.DaoProperty;

@Repository
public interface DaoPropertyRepository extends MongoRepository<DaoProperty, String> {

}
