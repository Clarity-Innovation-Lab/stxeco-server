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
@TypeAlias(value = "PinataHelper")
public class GitHubHelper {
	
	private GitHub gitHub;
    private static final Logger logger = LogManager.getLogger(GitHubHelper.class);
	@Autowired private Environment environment;
	
	public GitHub setup() throws IOException {
		String token = environment.getProperty("GITHUB_PAT");
		// logger.info("environment: " + token);
		if (this.gitHub == null) {
			this.gitHub = new GitHubBuilder().withOAuthToken(token,"radicleart").build();
		}
		GitHub.connect("radicleart", token);
		return this.gitHub;
	}

}
