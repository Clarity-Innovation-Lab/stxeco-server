package eco.stx.edao.contracts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import eco.stx.edao.contracts.service.domain.ContractEvent;

@Repository
public interface ContractEventRepository extends MongoRepository<ContractEvent, String> {

	@Query(value = "{ 'contract_log.contract_id' : ?#{[0]} }", delete = true)
	void deleteByContract_id(String contract_id);

	@Query(value = "{ 'contract_log.value.hex' : ?#{[0]} }")
	ContractEvent findOneByHex(String hex);

	@Query(value = "{ 'voteEvent.voter' : ?#{[0]} }")
	List<ContractEvent> findByVoter(String voter);

	@Query(value = "{ 'contract_log.contract_id' : ?#{[0]} }")
	List<ContractEvent> findByContract_id(String contract_id);

	@Query(value = "{ 'voteEvent.proposal' : ?#{[0]} }")
	List<ContractEvent> findByProposal(String proposal);

	@Query(value = "{ 'contract_log.contract_id' : ?#{[0]}, 'voteEvent.proposal' : ?#{[1]} }")
	List<ContractEvent> findByContract_idAndProposal(String contract_id, String proposal);

	@Query(value = "{ 'voteEvent.proposal' : ?#{[0]}, 'voteEvent.voter' : ?#{[1]} }")
	List<ContractEvent> findByProposalAndVoter(String proposal, String voter);

	// Count queries
	@Query(value = "{ 'contract_log.contract_id' : ?#{[0]}, 'voteEvent.proposal' : ?#{[1]} }", count = true)
	Optional<Long> countByContract_idAndProposal(String contract_id, String proposal);

	@Query(value = "{ 'voteEvent.proposal' : ?#{[0]} }", count = true)
	Optional<Long> countByProposal(String proposal);

	@Query(value = "{ 'contract_log.contract_id' : ?#{[0]} }", count = true)
	Optional<Long> countByContract_id(String contract_id);

}
