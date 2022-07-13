package eco.stx.edao.stacks.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eco.stx.edao.stacks.service.domain.AppMapContract;

@Repository
public interface AppMapContractRepository extends MongoRepository<AppMapContract, String> {

	public AppMapContract findByAdminContractAddressAndAdminContractName(String adminContractAddress, String adminContractName);
}
