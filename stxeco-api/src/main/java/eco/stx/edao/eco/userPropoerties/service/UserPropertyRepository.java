package eco.stx.edao.eco.userPropoerties.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eco.stx.edao.eco.userPropoerties.service.domain.UserProperty;

@Repository
public interface UserPropertyRepository extends MongoRepository<UserProperty, String> {
	
	public List<UserProperty> findByStxAddress(String stxAddress);
	public Optional<UserProperty> findByStxAddressAndFunctionName(String stxAddress, String functionName);

}
