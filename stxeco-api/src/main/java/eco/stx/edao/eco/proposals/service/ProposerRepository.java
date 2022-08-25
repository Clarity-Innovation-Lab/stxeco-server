package eco.stx.edao.eco.proposals.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eco.stx.edao.eco.proposals.service.domain.Proposer;

@Repository
public interface ProposerRepository extends MongoRepository<Proposer, String> {

    public Proposer findByStxAddress(String stxAddress);
}
