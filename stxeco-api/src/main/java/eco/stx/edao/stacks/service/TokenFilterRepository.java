package eco.stx.edao.stacks.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eco.stx.edao.stacks.service.domain.TokenFilter;


@Repository
public interface TokenFilterRepository extends MongoRepository<TokenFilter, String> {

	public TokenFilter findByContractIdAndAssetHash(String contractId, String assetHash);

}
