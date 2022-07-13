package eco.stx.edao.stacks.service;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import eco.stx.edao.stacks.service.domain.metadata.Sip016NFTMetaData;

@Repository
public interface NFTMetaDataRepository extends MongoRepository<Sip016NFTMetaData, String> {

	@Query(value = "{'properties.assetHash' : ?0}")
	public Sip016NFTMetaData findByAssetHash(String assetHash);

	public Sip016NFTMetaData findByContractIdAndNftIndex(String contractId, Long nftIndex);

	public List<Sip016NFTMetaData> findByContractId(String contractId);

	@Query(value = "{'properties.collectionId' : ?0}")
	public List<Sip016NFTMetaData> findByCollectionId(String collectionId);

	@Query(value = "{'contractAsset.owner' : ?0}", fields = "{ 'contractAsset.assetName' : 1 }", sort = "{'contractAsset.assetName' : 1}")
	public List<Sip016NFTMetaData> findByOwner(String owner);

	public void deleteByContractIdAndNftIndex(String contractId, Long nftIndex);
}
