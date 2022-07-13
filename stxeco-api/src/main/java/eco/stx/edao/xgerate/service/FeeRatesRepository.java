package eco.stx.edao.xgerate.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eco.stx.edao.xgerate.service.domain.FeeRate;

@Repository
public interface FeeRatesRepository extends MongoRepository<FeeRate, String> {

}
