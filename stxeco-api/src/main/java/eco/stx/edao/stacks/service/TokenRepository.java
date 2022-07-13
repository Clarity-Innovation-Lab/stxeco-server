package eco.stx.edao.stacks.service;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import eco.stx.edao.stacks.service.domain.Token;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

	public Long countByContractId(String contractId);
		
    @Query(value = "{ 'tokenInfo.assetHash' : ?#{[0]}, 'tokenInfo.edition' : ?#{[1]} }")
    // public Page<Token> findByAssetHashAndEdition(String assetHash, Long edition, Pageable pageable);
	public List<Token> findByAssetHashAndEdition(String assetHash, Long edition);

    @Query(value = "{ 'contractId' : ?#{[0]}, 'tokenInfo.edition' : ?#{[1]} }")
	public List<Token> findByContractIdAndEdition(String contractId, Long edition);
    
	public List<Token> findByContractIdAndNftIndex(String contractId, Long nftIndex);
	
	public List<Token> findByContractIdAndOwner(String contractId, String owner);
	
	public List<Token> findByContractId(String contractId);

	public void deleteByContractId(String contractId);
	
	public void deleteByContractIdAndNftIndex(String contractId, Long nftIndex);
}
