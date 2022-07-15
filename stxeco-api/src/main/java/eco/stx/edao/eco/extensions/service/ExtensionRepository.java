package eco.stx.edao.eco.extensions.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import eco.stx.edao.eco.extensions.service.domain.Extension;

@Repository
public interface ExtensionRepository extends MongoRepository<Extension, String> {

    @Query(value = "{ 'contract.contractId' : ?#{[0]} }")
    public Extension findByContractId(String contractId);
}
