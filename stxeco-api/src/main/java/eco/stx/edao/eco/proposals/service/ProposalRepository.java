package eco.stx.edao.eco.proposals.service;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eco.stx.edao.eco.proposals.service.domain.Proposal;

@Repository
public interface ProposalRepository extends MongoRepository<Proposal, String> {

    public List<Proposal> findByProposer(String proposer);
    public Proposal findByContractId(String contractId);
}
