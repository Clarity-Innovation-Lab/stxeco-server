package eco.stx.edao.github.api.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@TypeAlias(value = "GitHubPullRequest")
@Document
public class GitHubPullRequest extends BaseIssue {


	public GitHubPullRequest() {
		super();
	}

}
