package eco.stx.edao.twoone.method2;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TwoOneEventRepository extends MongoRepository<TwoOneEvent, String> {

	// db.twoOneEvent.aggregate([{"$group": {"_id": "$sender_address", "count":{"$sum": 1}}}])
	@Aggregation(value = "{ [{'$group': {'_id': '$sender_address', 'count':{'$sum': 1}}}] }")
	TwoOneEvent groupBySender();
	
	//db.twoOneEvent.distinct('sender_address').length
	//@Aggregation(value = "{ [{'$group': {'_id': '$sender_address', 'count':{'$sum': 1}}}] }")
	//Method1VoteCount countBySender();

	
	@Query(value = "{ 'contract_log.contract_id' : ?#{[0]} }", delete = true)
	void deleteByContract_id(String contract_id);

	@Query(value = "{ 'contract_log.value.hex' : ?#{[0]} }")
	TwoOneEvent findOneByHex(String hex);

	@Query(value = "{ 'voteEvent.voter' : ?#{[0]} }")
	List<TwoOneEvent> findByVoter(String voter);

	@Query(value = "{ 'contract_log.contract_id' : ?#{[0]} }")
	List<TwoOneEvent> findByContract_id(String contract_id);

	@Query(value = "{ 'contract_log.contract_id' : ?#{[0]}, 'voteEvent.proposal' : ?#{[1]} }")
	List<TwoOneEvent> findByContract_idAndProposal(String contract_id, String proposal);

	// Count queries
	@Query(value = "{ 'contract_log.contract_id' : ?#{[0]} }", count = true)
	Optional<Long> countByContract_id(String contract_id);

}
