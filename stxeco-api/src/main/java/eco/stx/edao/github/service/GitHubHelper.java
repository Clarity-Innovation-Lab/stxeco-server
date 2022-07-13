package eco.stx.edao.github.service;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.annotation.TypeAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias(value = "GitHubHelper")
public class GitHubHelper {
	
	private GitHub gitHub;
    private static final Logger logger = LogManager.getLogger(GitHubHelper.class);
	@Autowired private Environment environment;
	
	public GitHub setup() throws IOException {
		if (this.gitHub == null) {
			logger.info("environment: ", environment);
			this.gitHub = new GitHubBuilder().withOAuthToken(environment.getProperty("GITHUB_PAT"),"radicleart").build();
		}
		GitHub.connect("radicleart", environment.getProperty("GITHUB_PAT"));
		return this.gitHub;
	}

}
