package eco.stx.edao.github.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eco.stx.edao.github.api.model.GitHubIssue;

@Repository
public interface JsonIssueRepository extends MongoRepository<GitHubIssue, String> {

    public GitHubIssue findByState(String state);
}
