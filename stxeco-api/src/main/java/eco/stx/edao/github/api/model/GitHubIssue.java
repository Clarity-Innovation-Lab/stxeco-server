package eco.stx.edao.github.api.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@TypeAlias(value = "GitHubIssue")
@Document
public class GitHubIssue extends BaseIssue {
	

	public GitHubIssue() {
		super();
	}

}
